package com.graph.db.domain.output;

import com.graph.db.domain.input.annotation.TranscriptConsequence;

public class GeneticVariantToTranscriptVariantOutput {
	
	private final String variant_id;
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
