package com.graph.db.domain.output;

import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class TranscriptVariantToConsequenceTermOutput {
	
	@RelationshipStart(mapping = Neo4jMapping.TranscriptVariant)
	private final String hgvsc;
	
	@RelationshipEnd(mapping = Neo4jMapping.ConsequenceTerm)
	private final String consequenceTerm;
	
	public TranscriptVariantToConsequenceTermOutput(String hgvsc, String consequenceTerm) {
		this.hgvsc = hgvsc;
		this.consequenceTerm = consequenceTerm;
	}

	public String getHgvsc() {
		return hgvsc;
	}

	public String getConsequenceTerm() {
		return consequenceTerm;
	}
}
