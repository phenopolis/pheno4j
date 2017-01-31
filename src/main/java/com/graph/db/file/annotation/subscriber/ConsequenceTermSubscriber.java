package com.graph.db.file.annotation.subscriber;

import org.apache.commons.collections.CollectionUtils;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.ConsequenceTermOutput;
import com.graph.db.file.SetBasedGenericSubscriber;
import com.graph.db.output.OutputFileType;

public class ConsequenceTermSubscriber extends SetBasedGenericSubscriber<GeneticVariant, ConsequenceTermOutput> {
	
	public ConsequenceTermSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.CONSEQUENCE_TERM);
	}
	
	@Override
	public void processRow(GeneticVariant variant) {
    	for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
    		if (CollectionUtils.isNotEmpty(transcriptConsequence.getConsequence_terms())) {
    			for (String consequenceTerm : transcriptConsequence.getConsequence_terms()) {
    				set.add(new ConsequenceTermOutput(consequenceTerm));
    			}
    		}
		}
	}
}
