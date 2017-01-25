package com.graph.db.file;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration2.Configuration;

import com.graph.db.util.ManagedEventBus;
import com.graph.db.util.PropertiesHolder;

public abstract class AbstractParser implements Parser {
	
	protected final Configuration config;
	protected final String outputFolder;
	protected final ManagedEventBus eventBus;
	protected final List<? extends AutoCloseable> subscribers;
	
	public AbstractParser() {
		config = PropertiesHolder.getInstance();
		
		this.outputFolder = getOutputFolder();
		
		eventBus = new ManagedEventBus(getParserClass().getSimpleName());
		subscribers = createSubscribers();
	}

	protected String getOutputFolder() {
		return config.getString("output.folder");
	}
	
	protected void closeEventBus() {
		try {
			eventBus.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void registerSubscribers() {
		subscribers.forEach(subscriber -> eventBus.register(subscriber));
	}

	protected void closeSubscribers() {
		subscribers.forEach(subscriber -> {
			try {
				subscriber.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	protected abstract List<? extends AutoCloseable> createSubscribers();
	
	protected abstract Class<?> getParserClass();
}
