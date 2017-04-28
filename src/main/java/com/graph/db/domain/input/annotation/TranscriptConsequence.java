package com.graph.db.domain.input.annotation;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TranscriptConsequence {

	private String variant_id;
	
	private Double exac_af;
	private Double exac_af_adj;
	private Integer exac_af_afr;
	private Double exac_af_amr;
	private Integer exac_af_consanguineous;
	private Integer exac_af_eas;
	private Integer exac_af_female;
	private Integer exac_af_fin;
	private Double exac_af_male;
	private Integer exac_af_nfe;
	private Integer exac_af_oth;
	private Double exac_af_popmax;
	private Double exac_af_sas;
	private Integer cadd_phred;
	private Double cadd_raw;
	private String hgvsc;
	private String hgvsp;
	private String impact;
	private Set<String> consequence_terms;
	private String transcript_id;
	private String intron;
	private String exon;
	private String gene_id;
	private String gene_symbol;
	private Integer hgnc_id;
	private Integer canonical;
	private Integer strand;
	private String protein_id;
	private String[] swissprot;

//    private String ccds;
//    private Integer cds_start;
//    private Domain domains[];
//    private String amino_acids;
//    private String carol;
//    private String sift_prediction;
//    private String biotype;
//    private Integer gene_pheno;
//    private Integer cdna_end;
//    private String[] trembl;
//    private Integer sift_score;
//    private String codons;
//    private String polyphen_prediction;
//    private Double polyphen_score;
//    private Integer protein_end;
//    private Integer cdna_start;
//    private Integer cds_end;
//    private String condel;
//    private Integer protein_start;
//    private String[] uniparc;
//    private Integer exacpli;
//    private String variant_allele;
//    private String gene_symbol_source;
	
	public TranscriptConsequence() {
	}
	
	public TranscriptConsequence(String variant_id, String gene_id, String hgvsc, Set<String> consequenceTerms) {
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

	public Double getExac_af() {
		return exac_af;
	}

	public Double getExac_af_adj() {
		return exac_af_adj;
	}

	public Integer getExac_af_afr() {
		return exac_af_afr;
	}

	public Double getExac_af_amr() {
		return exac_af_amr;
	}

	public Integer getExac_af_consanguineous() {
		return exac_af_consanguineous;
	}

	public Integer getExac_af_eas() {
		return exac_af_eas;
	}

	public Integer getExac_af_female() {
		return exac_af_female;
	}

	public Integer getExac_af_fin() {
		return exac_af_fin;
	}

	public Double getExac_af_male() {
		return exac_af_male;
	}

	public Integer getExac_af_nfe() {
		return exac_af_nfe;
	}

	public Integer getExac_af_oth() {
		return exac_af_oth;
	}

	public Double getExac_af_popmax() {
		return exac_af_popmax;
	}

	public Double getExac_af_sas() {
		return exac_af_sas;
	}

	public Integer getCadd_phred() {
		return cadd_phred;
	}

	public Double getCadd_raw() {
		return cadd_raw;
	}

	public String getHgvsc() {
		return hgvsc;
	}

	public String getHgvsp() {
		return hgvsp;
	}

	public String getImpact() {
		return impact;
	}

	public Set<String> getConsequence_terms() {
		return consequence_terms;
	}

	public String getTranscript_id() {
		return transcript_id;
	}

	public String getIntron() {
		return intron;
	}

	public String getExon() {
		return exon;
	}

	public String getGene_id() {
		return gene_id;
	}

	public String getGene_symbol() {
		return gene_symbol;
	}

	public Integer getHgnc_id() {
		return hgnc_id;
	}

	public Integer getCanonical() {
		return canonical;
	}

	public Integer getStrand() {
		return strand;
	}

	public String getProtein_id() {
		return protein_id;
	}

	public String[] getSwissprot() {
		return swissprot;
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
}
