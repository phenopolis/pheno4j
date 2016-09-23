package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.file.annotation.output.OutputFileType;

public class GeneToVariantSubscriber extends AbstractSubscriber {

	public GeneToVariantSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected Class<?> getBeanClass() {
		return TranscriptConsequence.class;
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.GENE_TO_VARIANT;
	}
	
	@Override
	public void processAnnotation(Annotation annotation) {
    	try {
			for (TranscriptConsequence transcriptConsequence : annotation.getTranscript_consequences()) {
				beanWriter.write(transcriptConsequence);
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
	
}
