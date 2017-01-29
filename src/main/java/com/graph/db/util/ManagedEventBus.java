package com.graph.db.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

//see https://github.com/groupon/jesos/blob/master/src/main/java/com/groupon/mesos/util/ManagedEventBus.java
/**
 * As the event bus does not allow controlled shutdown, add the ability to
 * "poison" the event bus and wait for the pill to pass through it. It is
 * assumed that the pill is the last event that the bus processes and therefore
 * when it is received, no additional events can be processed.
 */
public class ManagedEventBus implements Closeable {
	private final AsyncEventBus eventBus;
	private final AtomicBoolean finished = new AtomicBoolean(false);
	private final AtomicReference<PoisonPill> pillHolder = new AtomicReference<>(new PoisonPill());

	private final ExecutorService executor;

	public ManagedEventBus(final String name) {
		checkNotNull(name, "name is null");
		this.executor = Executors.newScheduledThreadPool(10,
				new ThreadFactoryBuilder().setDaemon(true).setNameFormat("eventbus-" + name + "-%d").build());
		this.eventBus = new AsyncEventBus(executor, new EventBusExceptionHandler(name));
	}

	public void register(final Object listener) {
		checkState(!finished.get(), "event bus is shut down");
		eventBus.register(listener);
	}

	public void post(final Object event) {
		checkState(!finished.get(), "event bus is shut down");
		eventBus.post(event);
	}

	@Override
	public void close() throws IOException {
		if (!finished.getAndSet(true)) {
			eventBus.register(this);

			final PoisonPill pill = pillHolder.getAndSet(null);
			if (pill != null) {
				eventBus.post(pill);
				try {
					pill.awaitTermination(1, TimeUnit.DAYS);

					// The poison pill made it through the event bus, so
					// all events that were present before are either delivered
					// or in flight. Shut down the executor now.
					executor.shutdown();
					executor.awaitTermination(1, TimeUnit.SECONDS);
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		}
	}

	@Subscribe
	public void receivePoisonPill(final PoisonPill poisonPill) {
		poisonPill.trigger();
	}

	public static class PoisonPill {
		private final SettableFuture<Void> future = SettableFuture.create();

		public void trigger() {
			future.set(null);
		}

		public void awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
			try {
				future.get(timeout, unit);
			} catch (TimeoutException | ExecutionException e) {
				return; // do nothing.
			}
		}
	}

	/**
	 * Simple exception handler that, unlike the default handler, does not
	 * swallow the exception causing the error.
	 */
	public static class EventBusExceptionHandler implements SubscriberExceptionHandler {
		private static final Logger LOG = LoggerFactory.getLogger(EventBusExceptionHandler.class);

		private final String name;

		public EventBusExceptionHandler(final String name) {
			this.name = checkNotNull(name, "name is null");
		}

		@Override
		public void handleException(final Throwable e, final SubscriberExceptionContext context) {
			if ((e instanceof ClassCastException) && e.getMessage().contains(PoisonPill.class.getName())) {
				LOG.debug("Poision Pill processed on: {}", context.getSubscriber().getClass().getSimpleName());
			} else {
				String msg = String.format("Could not call %s/%s on bus %s", context.getSubscriber().getClass().getSimpleName(),
						context.getSubscriberMethod().getName(), name);
				LOG.error(msg, e);
			}
		}
	}
}