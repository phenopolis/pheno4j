package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.eventbus.Subscribe;
import com.graph.db.file.AbstractSubscriber;
import com.graph.db.file.annotation.domain.AnnotatedVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.file.annotation.output.OutputFileType;

public class AnnotatedGeneSubscriber extends AbstractSubscriber<AnnotatedVariant> {
	
	private final Set<TranscriptConsequence> genes = ConcurrentHashMap.newKeySet();
	
	public AnnotatedGeneSubscriber(String outputFolder) {
		super(outputFolder);
	}
	
	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.ANNOTATED_GENE;
	}
	
    @Override
	@Subscribe
    public void processAnnotation(AnnotatedVariant annotatedVariant) {
		for (TranscriptConsequence transcriptConsequence : annotatedVariant.getTranscript_consequences()) {
			genes.add(transcriptConsequence);
		}
    }

	@Override
	public void close() {
		try {
			for (TranscriptConsequence transcriptConsequence : genes) {
				beanWriter.write(transcriptConsequence);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				beanWriter.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
