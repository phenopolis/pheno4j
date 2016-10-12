package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.AnnotatedVariant;
import com.graph.db.file.annotation.domain.GeneSymbolToGeneId;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.output.OutputFileType;

public class GeneSymbolToGeneIdSubscriber extends GenericSubscriber<AnnotatedVariant> {
	
	private final Set<GeneSymbolToGeneId> set = ConcurrentHashMap.newKeySet();

	public GeneSymbolToGeneIdSubscriber(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		super(outputFolder, parserClass, outputFileType);
	}

	@Override
	public void processAnnotation(AnnotatedVariant annotatedVariant) {
    	for (TranscriptConsequence transcriptConsequence : annotatedVariant.getTranscript_consequences()) {
			set.add(new GeneSymbolToGeneId(transcriptConsequence.getGene_symbol(), transcriptConsequence.getGene_id()));
		}
	}
	
	@Override
	public void close() {
		try {
			for (GeneSymbolToGeneId geneToAnnotatedGenetranscriptConsequence : set) {
				beanWriter.write(geneToAnnotatedGenetranscriptConsequence);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}
	
}
