package com.graph.db.domain.output;

import com.graph.db.file.term.domain.RawTerm;

public class TermOutput {
	
	private final String termId;
	private final String name;
	
	public TermOutput(RawTerm rawTerm) {
		this.termId = rawTerm.getTermId();
		this.name = rawTerm.getName();
	}

	public String getTermId() {
		return termId;
	}

	public String getName() {
		return name;
	}

}
