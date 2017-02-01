package com.graph.db.domain.output;

import java.util.Map;

import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class GeneToTermOutput {
	
	@RelationshipStart(mapping = Neo4jMapping.Gene)
	private final String gene_id;
	@RelationshipEnd(mapping = Neo4jMapping.Term)
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
