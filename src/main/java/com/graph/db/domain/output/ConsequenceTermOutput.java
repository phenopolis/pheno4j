package com.graph.db.domain.output;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.graph.db.domain.output.annotation.Id;
import com.graph.db.output.Neo4jMapping;

public class ConsequenceTermOutput {
	
	@Id(name = "consequenceTerm", mapping = Neo4jMapping.ConsequenceTerm)
	private final String consequenceTerm;

	public ConsequenceTermOutput(String consequenceTerm) {
		this.consequenceTerm = consequenceTerm;
	}

	public String getConsequenceTerm() {
		return consequenceTerm;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.consequenceTerm)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof ConsequenceTermOutput) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final ConsequenceTermOutput otherObject = (ConsequenceTermOutput) obj;
		return new EqualsBuilder()
				.append(this.consequenceTerm, otherObject.consequenceTerm)
				.isEquals();
	}
}
