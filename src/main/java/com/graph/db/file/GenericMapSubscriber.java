package com.graph.db.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.eventbus.Subscribe;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.Constants;

public class GenericMapSubscriber<T extends Map<String, ?>> implements AutoCloseable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericMapSubscriber.class);
	
	private final OutputFileType outputFileType;
	
	private final String fileName;
	protected CsvMapWriter beanWriter;

	public GenericMapSubscriber(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		this.outputFileType = outputFileType;
		fileName = outputFolder + File.separator + outputFileType.getFileTag() + Constants.HYPHEN + parserClass.getSimpleName() + ".csv";
		FileWriter writer;
		try {
			writer = new FileWriter(fileName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		beanWriter = new CsvMapWriter(writer, CsvPreference.STANDARD_PREFERENCE);
	}
	
    @Subscribe
    public void processAnnotation(T object) {
    	try {
			beanWriter.write(object, outputFileType.getHeader());
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
