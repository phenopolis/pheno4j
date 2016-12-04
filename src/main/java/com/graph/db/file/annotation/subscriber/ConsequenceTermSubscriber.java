package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import com.graph.db.domain.output.ConsequenceTermOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.output.OutputFileType;

public class ConsequenceTermSubscriber extends GenericSubscriber<GeneticVariant> {
	
	private final Set<ConsequenceTermOutput> set = ConcurrentHashMap.newKeySet();
	
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
	
	@Override
	public void close() {
		try {
			for (ConsequenceTermOutput s : set) {
				beanWriter.write(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}
	
}
