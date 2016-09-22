package com.graph.db.file.annotation.subscriber;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.eventbus.Subscribe;
import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.domain.OutputFile;

public class AnnotationToVariantSubscriber implements AutoCloseable {
	
	private CsvDozerBeanWriter beanWriter;

	public AnnotationToVariantSubscriber(String outputFolder) {
		try {
			FileWriter writer = new FileWriter(outputFolder + File.separator + "AnnotationToVariant.csv");
			beanWriter = new CsvDozerBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		beanWriter.configureBeanMapping(Annotation.class, OutputFile.ANNOTATION_TO_VARIANT.getHeader());
	}
	
    @Subscribe
    public void processAnnotation(Annotation annotation) {
    	try {
			beanWriter.write(annotation);
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
	}
}
