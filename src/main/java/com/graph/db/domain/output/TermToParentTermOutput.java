package com.graph.db.domain.output;

public class TermToParentTermOutput {
	
	private final String child;
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
