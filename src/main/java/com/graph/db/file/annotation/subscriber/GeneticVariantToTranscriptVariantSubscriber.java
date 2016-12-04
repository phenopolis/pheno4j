package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.domain.output.GeneticVariantToTranscriptVariantOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.output.OutputFileType;

public class GeneticVariantToTranscriptVariantSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public GeneticVariantToTranscriptVariantSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT);
	}

	@Override
	public void processRow(GeneticVariant variant) {
    	try {
			for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
				if (StringUtils.isNoneBlank(transcriptConsequence.getHgvsc())) {
					GeneticVariantToTranscriptVariantOutput output = new GeneticVariantToTranscriptVariantOutput(transcriptConsequence);
					beanWriter.write(output);
				}
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
	
}
