package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

import com.graph.db.file.AbstractSubscriber;
import com.graph.db.file.annotation.domain.AnnotatedVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.file.annotation.output.OutputFileType;

public class GeneToVariantSubscriber extends AbstractSubscriber<AnnotatedVariant> {

	public GeneToVariantSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.ANNOTATED_GENE_TO_VARIANT;
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
