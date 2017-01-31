package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.TranscriptVariantOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.OutputFileType;

public class TranscriptVariantSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public TranscriptVariantSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.TRANSCRIPT_VARIANT);
	}

	@Override
	public void processRow(GeneticVariant variant) {
    	try {
			for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
				if (StringUtils.isNoneBlank(transcriptConsequence.getHgvsc())) {
					TranscriptVariantOutput output = new TranscriptVariantOutput(transcriptConsequence);
					beanWriter.write(output);
				}
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
}
