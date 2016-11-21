package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.output.OutputFileType;

//TODO almost a duplicate of TranscriptVariantSubscriber
public class TranscriptToTranscriptVariantSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public TranscriptToTranscriptVariantSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.TRANSCRIPT_TO_TRANSCRIPT_VARIANT);
	}

	@Override
	public void processAnnotation(GeneticVariant variant) {
    	try {
			for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
				if (StringUtils.isNoneBlank(transcriptConsequence.getHgvsc())) {
					beanWriter.write(transcriptConsequence);
				}
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
	
}
