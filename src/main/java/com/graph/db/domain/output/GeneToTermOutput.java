package com.graph.db.domain.output;

import java.util.Map;

public class GeneToTermOutput {
	
	private final String gene_id;
	private final String termId;
	
	public GeneToTermOutput(Map<String, String> map) {
		this.gene_id = map.get("gene_id");
		this.termId = map.get("HPO-ID");
	}

	public String getGene_id() {
		return gene_id;
	}

	public String getTermId() {
		return termId;
	}

}
