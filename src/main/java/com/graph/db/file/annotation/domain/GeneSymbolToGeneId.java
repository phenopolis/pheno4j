package com.graph.db.file.annotation.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GeneSymbolToGeneId {
	
	private final String geneSymbol;
	private final String geneId;
	
	public GeneSymbolToGeneId(String geneSymbol, String geneId) {
		this.geneSymbol = geneSymbol;
		this.geneId = geneId;
	}
	
	public String getGeneSymbol() {
		return geneSymbol;
	}
	
	public String getGeneId() {
		return geneId;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.geneSymbol)
				.append(this.geneId)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof GeneSymbolToGeneId) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final GeneSymbolToGeneId otherObject = (GeneSymbolToGeneId) obj;
		return new EqualsBuilder()
				.append(this.geneSymbol, otherObject.geneSymbol)
				.append(this.geneId, otherObject.geneId)
				.isEquals();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
