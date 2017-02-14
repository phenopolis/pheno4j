package com.graph.db.domain.input.annotation;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	private TranscriptConsequence(String variant_id, String gene_id, String hgvsc, Set<String> consequenceTerms) {
		this.variant_id = variant_id;
		this.gene_id = gene_id;
		this.hgvsc = hgvsc;
		if (isNotEmpty(consequenceTerms)) {
			this.consequence_terms = new HashSet<>(consequenceTerms);
		}
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
	
	public static class TranscriptConsequenceBuilder {
		private String gene_id;
		private String variant_id;
		private String hgvsc;
		private final Set<String> consequenceTerms = new HashSet<>();
		
		public TranscriptConsequenceBuilder geneId(String gene_id) {
			this.gene_id = gene_id;
			return this;
		}
		
		public TranscriptConsequenceBuilder variantId(String variant_id) {
			this.variant_id = variant_id;
			return this;
		}
		
		public TranscriptConsequenceBuilder hgvsc(String hgvsc) {
			this.hgvsc = hgvsc;
			return this;
		}
		
		public TranscriptConsequenceBuilder addConsequenceTerm(String consequenceTerm) {
			consequenceTerms.add(consequenceTerm);
			return this;
		}
		
		public TranscriptConsequence build() {
			return new TranscriptConsequence(variant_id, gene_id, hgvsc, consequenceTerms);
		}
	}
}
