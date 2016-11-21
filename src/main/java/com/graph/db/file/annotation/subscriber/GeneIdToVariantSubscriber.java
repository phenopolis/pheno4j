package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.output.OutputFileType;

//TODO this needs to become GeneToGeneticVariant
public class GeneIdToVariantSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public GeneIdToVariantSubscriber(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		super(outputFolder, parserClass, outputFileType);
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
