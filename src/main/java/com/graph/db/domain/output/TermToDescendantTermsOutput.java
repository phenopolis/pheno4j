package com.graph.db.domain.output;

import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class TermToDescendantTermsOutput {

	@RelationshipStart(mapping = Neo4jMapping.Term)
	private final String parent;
	
	@RelationshipEnd(mapping = Neo4jMapping.Term)
	private final String child;

	public TermToDescendantTermsOutput(String parent, String child) {
		this.parent = parent;
		this.child = child;
	}

	public String getParent() {
		return parent;
	}

	public String getChild() {
		return child;
	}
}
