package com.graph.db.domain.output;

public class TermToDescendantTermsOutput {

	private final String parent;
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
