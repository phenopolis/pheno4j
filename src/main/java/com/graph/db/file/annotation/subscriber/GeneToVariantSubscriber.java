package com.graph.db.file.annotation.subscriber;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.eventbus.Subscribe;
import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.file.annotation.output.OutputFile;

public class GeneToVariantSubscriber implements AutoCloseable {
	
	private CsvDozerBeanWriter beanWriter;

	public GeneToVariantSubscriber(String outputFolder) {
		try {
			FileWriter writer = new FileWriter(outputFolder + File.separator + "GeneToVariant.csv");
			beanWriter = new CsvDozerBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		beanWriter.configureBeanMapping(TranscriptConsequence.class, OutputFile.GENE_TO_VARIANT.getHeader());
	}
	
    @Subscribe
    public void processAnnotation(Annotation annotation) {
    	try {
			for (TranscriptConsequence transcriptConsequence : annotation.getTranscript_consequences()) {
				beanWriter.write(transcriptConsequence);
			}
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
