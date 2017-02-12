package com.graph.db.file;

import static com.graph.db.util.FileUtil.createFolderIfNotPresent;
import static com.graph.db.util.FileUtil.getLineNumberReaderForFile;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.util.ManagedEventBus;
import com.graph.db.util.PropertiesHolder;

public abstract class AbstractParser implements Parser {
	
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	protected final static Configuration config = PropertiesHolder.getInstance();
	protected final String outputFolder;
	protected ManagedEventBus eventBus;
	private final List<? extends AutoCloseable> subscribers;
	
	public AbstractParser() {
		this.outputFolder = getOutputFolder();
		createFolderIfNotPresent(outputFolder);
		
		eventBus = new ManagedEventBus(getParserClass().getSimpleName());
		subscribers = createSubscribers();
	}

	@Override
	public void execute() {
		LOGGER.info("Entering execute");
		registerSubscribers();
		
		Collection<File> inputFiles = getInputFiles();
		for (File inputFile : inputFiles) {
			LOGGER.info("Processing file: {}", inputFile);
			
			try (LineNumberReader reader = getLineNumberReaderForFile(inputFile);) {
				processDataForFile(reader);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		closeEventBus();
		closeSubscribers();
		LOGGER.info("Finished");
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
	
	public void setEventBus(ManagedEventBus eventBus) {
		this.eventBus = eventBus;
	}

	protected abstract void processDataForFile(LineNumberReader reader) throws IOException;

	protected abstract List<? extends AutoCloseable> createSubscribers();
	
	protected abstract Class<?> getParserClass();
}
