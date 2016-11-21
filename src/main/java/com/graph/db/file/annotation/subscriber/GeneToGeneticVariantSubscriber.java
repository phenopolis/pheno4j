package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

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
    	try {
			for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
				beanWriter.write(transcriptConsequence);
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
	
}
