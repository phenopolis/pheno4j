package com.graph.db.file;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.util.ManagedEventBus;
import com.graph.db.util.PropertiesHolder;

public abstract class AbstractParser implements Parser {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	protected final Configuration config;
	protected final String outputFolder;
	protected final ManagedEventBus eventBus;
	private final List<? extends AutoCloseable> subscribers;
	
	public AbstractParser() {
		config = PropertiesHolder.getInstance();
		
		this.outputFolder = getOutputFolder();
		
		eventBus = new ManagedEventBus(getParserClass().getSimpleName());
		subscribers = createSubscribers();
	}
	
	@Override
	public void execute() {
		LOGGER.info("Entering execute");
		registerSubscribers();
		
		processData();
		
		closeEventBus();
		closeSubscribers();
	}
	
	protected String getOutputFolder() {
		return config.getString("output.folder");
	}
	
	private void registerSubscribers() {
		subscribers.forEach(subscriber -> eventBus.register(subscriber));
	}
	
	private void closeEventBus() {
		try {
			eventBus.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void closeSubscribers() {
		subscribers.forEach(subscriber -> {
			try {
				subscriber.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	protected abstract void processData();

	protected abstract List<? extends AutoCloseable> createSubscribers();
	
	protected abstract Class<?> getParserClass();
}
