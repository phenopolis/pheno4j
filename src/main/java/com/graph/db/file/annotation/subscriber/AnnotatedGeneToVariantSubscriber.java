package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.AnnotatedVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.file.annotation.output.OutputFileType;

public class AnnotatedGeneToVariantSubscriber extends GenericSubscriber<AnnotatedVariant> {
	
	public AnnotatedGeneToVariantSubscriber(String outputFolder, OutputFileType outputFileType) {
		super(outputFolder, outputFileType);
	}

	@Override
	public void processAnnotation(AnnotatedVariant annotatedVariant) {
    	try {
			for (TranscriptConsequence transcriptConsequence : annotatedVariant.getTranscript_consequences()) {
				beanWriter.write(transcriptConsequence);
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
	
}
