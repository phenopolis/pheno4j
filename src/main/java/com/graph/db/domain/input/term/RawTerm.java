package com.graph.db.domain.input.term;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RawTerm {
	
	private final String termId;
	private final String name;
	private final List<String> isA;
	
    public RawTerm(String termId, String name, List<String> isA) {
		this.termId = termId;
		this.name = name;
		this.isA = isA != null ? isA : Collections.emptyList();
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
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.termId)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof RawTerm) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final RawTerm otherObject = (RawTerm) obj;
		return new EqualsBuilder()
				.append(this.termId, otherObject.termId)
				.isEquals();
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
