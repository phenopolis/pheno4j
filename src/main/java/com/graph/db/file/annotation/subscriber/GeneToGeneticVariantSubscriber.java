package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.output.OutputFileType;

public class GeneToGeneticVariantSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public GeneToGeneticVariantSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.GENE_TO_GENETIC_VARIANT);
	}

	@Override
	public void processAnnotation(GeneticVariant variant) {
		Set<Pair<String, String>> set = new HashSet<>();
		for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
			set.add(Pair.of(transcriptConsequence.getGene_id(), transcriptConsequence.getVariant_id()));
		}
		
    	try {
			for (Pair<String, String> pair : set) {
				beanWriter.write(pair);
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
}
