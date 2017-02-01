package com.graph.db.domain.output;

import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class GeneticVariantToTranscriptVariantOutput {
	
	@RelationshipStart(mapping = Neo4jMapping.GeneticVariant)
	private final String variant_id;
	
	@RelationshipEnd(mapping = Neo4jMapping.TranscriptVariant)
	private final String hgvsc;
	
	public GeneticVariantToTranscriptVariantOutput(TranscriptConsequence transcriptConsequence) {
		this.variant_id = transcriptConsequence.getVariant_id();
		this.hgvsc = transcriptConsequence.getHgvsc();
	}

	public String getVariant_id() {
		return variant_id;
	}

	public String getHgvsc() {
		return hgvsc;
	}
}
