package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.output.OutputFileType;

public class ConsequenceTermSubscriber extends GenericSubscriber<GeneticVariant> {
	
	private final Set<String> set = ConcurrentHashMap.newKeySet();
	
	public ConsequenceTermSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.CONSEQUENCE_TERM);
	}
	
	@Override
	public void processAnnotation(GeneticVariant variant) {
    	for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
    		if (CollectionUtils.isNotEmpty(transcriptConsequence.getConsequence_terms())) {
    			set.addAll(transcriptConsequence.getConsequence_terms());
    		}
		}
	}
	
	@Override
	public void close() {
		try {
			for (String s : set) {
				beanWriter.write(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}
	
}
