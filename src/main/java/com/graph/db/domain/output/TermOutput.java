package com.graph.db.domain.output;

import com.graph.db.domain.input.term.RawTerm;
import com.graph.db.domain.output.annotation.Id;

public class TermOutput {
	
	@Id(name = "termId")
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
