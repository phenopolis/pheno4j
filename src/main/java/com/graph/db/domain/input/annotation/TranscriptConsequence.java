package com.graph.db.domain.input.annotation;

import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TranscriptConsequence {

	private String variant_id;
	
	private String impact;
	private String gene_symbol_source;
	private String gene_symbol;
	private Integer cdna_end;
	private String gene_id;
	private String hgvsc;
	private Integer cdna_start;
	private String transcript_id;
	private String cadd;
	private String variant_allele;
	private Integer strand;
	private Integer canonical;
	private Set<String> consequence_terms;
	
	public TranscriptConsequence() {
	}
	
	public TranscriptConsequence(String variant_id, String hgvsc) {
		this.variant_id = variant_id;
		this.hgvsc = hgvsc;
	}

	public String getVariant_id() {
		return variant_id;
	}

	public String getImpact() {
		return impact;
	}

	public String getGene_symbol_source() {
		return gene_symbol_source;
	}

	public String getGene_symbol() {
		return gene_symbol;
	}

	public Integer getCdna_end() {
		return cdna_end;
	}

	public String getGene_id() {
		return gene_id;
	}

	public String getHgvsc() {
		return hgvsc;
	}

	public Integer getCdna_start() {
		return cdna_start;
	}

	public String getTranscript_id() {
		return transcript_id;
	}

	public String getCadd() {
		return cadd;
	}

	public void setCadd(String cadd) {
		this.cadd = cadd;
	}

	public String getVariant_allele() {
		return variant_allele;
	}

	public Integer getStrand() {
		return strand;
	}

	public Integer getCanonical() {
		return canonical;
	}

	public Set<String> getConsequence_terms() {
		return consequence_terms;
	}

	public void setVariant_id(String variant_id) {
		this.variant_id = variant_id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.hgvsc)
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
				.append(this.hgvsc, otherObject.hgvsc)
				.isEquals();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
