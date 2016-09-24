package com.graph.db.file.term.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RawTerm {
	
	private final String termId;
	private final String name;
	private final List<String> isA;
	
    public RawTerm(String termId, String name, List<String> isA) {
		this.termId = termId;
		this.name = name;
		this.isA = isA;
	}

	public String getTermId() {
		return termId;
	}

	public String getName() {
		return name;
	}

	public List<String> getIsA() {
		return isA;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,
                ToStringStyle.SHORT_PREFIX_STYLE)
	            .append("termId", termId)
	            .append("name", name)
	            .append("isA", isA)
                .toString();
    }
}
