package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.graph.db.domain.output.GeneToGeneticVariantOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.output.OutputFileType;

public class GeneToGeneticVariantSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public GeneToGeneticVariantSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.GENE_TO_GENETIC_VARIANT);
	}

	@Override
	public void processRow(GeneticVariant variant) {
		Set<GeneToGeneticVariantOutput> set = new HashSet<>();
		for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
			GeneToGeneticVariantOutput output = new GeneToGeneticVariantOutput(transcriptConsequence);
			set.add(output);
		}
		
    	try {
			for (GeneToGeneticVariantOutput pair : set) {
				beanWriter.write(pair);
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
}
