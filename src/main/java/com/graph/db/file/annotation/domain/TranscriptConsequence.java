package com.graph.db.file.annotation.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TranscriptConsequence {

	private String gene_id;
	private String gene_symbol;
	private String variant_id;
	private String cadd;
	
	public String getGene_id() {
		return gene_id;
	}

	public String getGene_symbol() {
		return gene_symbol;
	}

	public String getVariant_id() {
		return variant_id;
	}

	public void setVariant_id(String variant_id) {
		this.variant_id = variant_id;
	}
	
	public String getCadd() {
		return cadd;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.gene_id)
				.append(this.gene_symbol)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof TranscriptConsequence) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final TranscriptConsequence otherObject = (TranscriptConsequence) obj;
		return new EqualsBuilder()
				.append(this.gene_id, otherObject.gene_id)
				.append(this.gene_symbol, otherObject.gene_symbol)
				.isEquals();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
