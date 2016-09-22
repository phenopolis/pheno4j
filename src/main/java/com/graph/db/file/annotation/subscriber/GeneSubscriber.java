package com.graph.db.file.annotation.subscriber;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.eventbus.Subscribe;
import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.domain.OutputFile;
import com.graph.db.file.annotation.domain.TranscriptConsequence;

public class GeneSubscriber implements AutoCloseable {
	
	private CsvDozerBeanWriter beanWriter;

	private final Set<TranscriptConsequence> genes = ConcurrentHashMap.newKeySet();
	
	public GeneSubscriber(String outputFolder) {
		try {
			FileWriter writer = new FileWriter(outputFolder + File.separator + "Gene.csv");
			beanWriter = new CsvDozerBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		beanWriter.configureBeanMapping(TranscriptConsequence.class, OutputFile.GENE.getHeader());
	}
	
    @Subscribe
    public void processAnnotation(Annotation annotation) {
		for (TranscriptConsequence transcriptConsequence : annotation.getTranscript_consequences()) {
			genes.add(transcriptConsequence);
		}
    }

	@Override
	public void close() {
		try {
			//TODO move header writer somewhere else
			//beanWriter.writeHeader(OutputFile.GENE.getHeader());
			for (TranscriptConsequence transcriptConsequence : genes) {
				beanWriter.write(transcriptConsequence);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				beanWriter.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
