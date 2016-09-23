package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.graph.db.file.AbstractSubscriber;
import com.graph.db.file.annotation.domain.AnnotatedVariant;
import com.graph.db.file.annotation.domain.GeneToAnnotatedGene;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.file.annotation.output.OutputFileType;

public class GeneToAnnotatedGeneSubscriber extends AbstractSubscriber<AnnotatedVariant> {
	
	private final Set<GeneToAnnotatedGene> set = ConcurrentHashMap.newKeySet();

	public GeneToAnnotatedGeneSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.GENE_TO_ANNOTATED_GENE;
	}
	
	@Override
	public void processAnnotation(AnnotatedVariant annotatedVariant) {
    	for (TranscriptConsequence transcriptConsequence : annotatedVariant.getTranscript_consequences()) {
			String gene_symbol = transcriptConsequence.getGene_symbol();
			set.add(new GeneToAnnotatedGene(gene_symbol, gene_symbol));
		}
	}
	
	@Override
	public void close() {
		try {
			for (GeneToAnnotatedGene geneToAnnotatedGenetranscriptConsequence : set) {
				beanWriter.write(geneToAnnotatedGenetranscriptConsequence);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}
	
}
