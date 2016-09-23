package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.eventbus.Subscribe;
import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.file.annotation.output.OutputFileType;

public class GeneSubscriber extends AbstractSubscriber {
	
	private final Set<TranscriptConsequence> genes = ConcurrentHashMap.newKeySet();
	
	public GeneSubscriber(String outputFolder) {
		super(outputFolder);
	}
	
	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.GENE;
	}
	
    @Override
	@Subscribe
    public void processAnnotation(Annotation annotation) {
		for (TranscriptConsequence transcriptConsequence : annotation.getTranscript_consequences()) {
			genes.add(transcriptConsequence);
		}
    }

	@Override
	public void close() {
		try {
			beanWriter.writeHeader(OutputFileType.GENE.getHeader());
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
