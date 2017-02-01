package com.graph.db.domain.output;

import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class TermToParentTermOutput {
	
	@RelationshipStart(mapping = Neo4jMapping.Term)
	private final String child;
	
	@RelationshipEnd(mapping = Neo4jMapping.Term)
	private final String parent;

	public TermToParentTermOutput(String child, String parent) {
		this.child = child;
		this.parent = parent;
	}

	public String getChild() {
		return child;
	}

	public String getParent() {
		return parent;
	}
}
