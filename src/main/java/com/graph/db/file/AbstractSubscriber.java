package com.graph.db.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.graph.db.output.OutputFileType;
import com.graph.db.util.Constants;

/**
 * Subclasses are responsible for annotating the appropriate method
 * with @Subscribe
 */
public abstract class AbstractSubscriber<T> implements AutoCloseable {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	protected final OutputFileType outputFileType;
	protected final String fileName;
	protected final CsvDozerBeanWriter beanWriter;

	public AbstractSubscriber(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		this.outputFileType = outputFileType;
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
