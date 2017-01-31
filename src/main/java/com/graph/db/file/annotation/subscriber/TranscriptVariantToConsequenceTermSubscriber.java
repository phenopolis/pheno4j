package com.graph.db.file.annotation.subscriber;

import java.io.IOException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.TranscriptVariantToConsequenceTermOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.OutputFileType;

public class TranscriptVariantToConsequenceTermSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public TranscriptVariantToConsequenceTermSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM);
	}

	@Override
	public void processRow(GeneticVariant variant) {
        try {
			for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
				if (StringUtils.isNoneBlank(transcriptConsequence.getHgvsc()) && CollectionUtils.isNotEmpty(transcriptConsequence.getConsequence_terms())) {
					for (String consequenceTerm : transcriptConsequence.getConsequence_terms()) {
						TranscriptVariantToConsequenceTermOutput output = new TranscriptVariantToConsequenceTermOutput(transcriptConsequence.getHgvsc(), consequenceTerm);
						beanWriter.write(output);
					}
				}
			}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
	}
}
