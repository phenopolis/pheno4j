package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.AnnotatedVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.file.gene.domain.Gene;
import com.graph.db.output.OutputFileType;

public class GeneSymbolFromAnnotationSubscriber extends GenericSubscriber<AnnotatedVariant> {
	
	private final Set<Gene> set = ConcurrentHashMap.newKeySet();

	public GeneSymbolFromAnnotationSubscriber(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		super(outputFolder, parserClass, outputFileType);
	}

	@Override
	public void processAnnotation(AnnotatedVariant annotatedVariant) {
    	for (TranscriptConsequence transcriptConsequence : annotatedVariant.getTranscript_consequences()) {
			set.add(new Gene(transcriptConsequence.getGene_symbol()));
		}
	}
	
	@Override
	public void close() {
		try {
			for (Gene gene : set) {
				beanWriter.write(gene);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}
	
}
