package com.graph.db.file.annotation.subscriber;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.TranscriptToTranscriptVariantOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.OutputFileType;

public class TranscriptToTranscriptVariantSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public TranscriptToTranscriptVariantSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.TRANSCRIPT_TO_TRANSCRIPT_VARIANT);
	}

	@Override
	public void processRow(GeneticVariant variant) {
		for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
			if (StringUtils.isNoneBlank(transcriptConsequence.getHgvsc())) {
				TranscriptToTranscriptVariantOutput output = new TranscriptToTranscriptVariantOutput(transcriptConsequence);
				write(output);
			}
		}
	}
}
