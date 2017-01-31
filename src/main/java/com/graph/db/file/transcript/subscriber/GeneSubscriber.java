package com.graph.db.file.transcript.subscriber;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.eventbus.Subscribe;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.GeneOutput;
import com.graph.db.file.AbstractSubscriber;
import com.graph.db.output.OutputFileType;

public class GeneSubscriber extends AbstractSubscriber {
	
	private static final OutputFileType GENE = OutputFileType.GENE;
	
	private final Set<GeneOutput> set = ConcurrentHashMap.newKeySet();

	public GeneSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, GENE);
	}
	
	@Subscribe
	public void processRow(Map<String, String> object) {
		GeneOutput geneOutput = new GeneOutput(object);
		set.add(geneOutput);
	}
	
	@Subscribe
	public void processRow(GeneticVariant geneticVariant) {
		for (TranscriptConsequence transcriptConsequence : geneticVariant.getTranscript_consequences()) {
			GeneOutput geneOutput = new GeneOutput(transcriptConsequence);
			set.add(geneOutput);
		}
	}
	
	@Override
	public void preClose() {
		try {
			for (GeneOutput s : set) {
				beanWriter.write(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
