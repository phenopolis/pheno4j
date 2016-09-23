package com.graph.db.file.annotation.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GeneToAnnotatedGene {
	
	private final String geneSymbol;
	private final String annotatedGeneSymbol;
	
	public GeneToAnnotatedGene(String geneSymbol, String annotatedGeneSymbol) {
		this.geneSymbol = geneSymbol;
		this.annotatedGeneSymbol = annotatedGeneSymbol;
	}
	
	public String getGeneSymbol() {
		return geneSymbol;
	}
	
	public String getAnnotatedGeneSymbol() {
		return annotatedGeneSymbol;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.geneSymbol)
				.append(this.annotatedGeneSymbol)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof GeneToAnnotatedGene) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final GeneToAnnotatedGene otherObject = (GeneToAnnotatedGene) obj;
		return new EqualsBuilder()
				.append(this.geneSymbol, otherObject.geneSymbol)
				.append(this.annotatedGeneSymbol, otherObject.annotatedGeneSymbol)
				.isEquals();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
