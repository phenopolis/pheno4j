package com.graph.db.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.eventbus.Subscribe;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.Constants;
import com.graph.db.util.ManagedEventBus.PoisonPill;

public class GenericSubscriber<T> implements AutoCloseable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericSubscriber.class);
	
	private final String fileName;
	protected CsvDozerBeanWriter beanWriter;

	public GenericSubscriber(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		fileName = outputFolder + File.separator + outputFileType.getFileTag() + Constants.HYPHEN + parserClass.getSimpleName() + ".csv";
		FileWriter writer = createFileWriter();
		beanWriter = new CsvDozerBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		beanWriter.configureBeanMapping(outputFileType.getBeanClass(), outputFileType.getHeader());
	}
	
	private FileWriter createFileWriter() {
		try {
			return new FileWriter(fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

    @Subscribe
    public void processAnnotation(T object) {
    	if (object instanceof PoisonPill) {
    		return;
    	}
    	try {
			beanWriter.write(object);
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    }

	@Override
	public void close() {
		try {
			beanWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		LOGGER.info("Wrote out: {}", fileName);
	}

}
