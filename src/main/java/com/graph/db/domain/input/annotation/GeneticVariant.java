package com.graph.db.domain.input.annotation;

import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GeneticVariant {
	
	private String variant_id;
	private Exac EXAC;
	private Set<TranscriptConsequence> transcript_consequences;
	
	private Integer HET_COUNT;
	private Integer WT_COUNT;
	private Integer HOM_COUNT;
	private Integer MISS_COUNT;
	private Double allele_freq;
	private Boolean hasExac;
	
	public String getVariant_id() {
		return variant_id;
	}
	
	public Exac getEXAC() {
		return EXAC;
	}

	public Set<TranscriptConsequence> getTranscript_consequences() {
		return transcript_consequences;
	}

	public void setTranscript_consequences(Set<TranscriptConsequence> transcript_consequences) {
		this.transcript_consequences = transcript_consequences;
	}

	public Integer getHET_COUNT() {
		return HET_COUNT;
	}

	public Integer getWT_COUNT() {
		return WT_COUNT;
	}

	public Integer getHOM_COUNT() {
		return HOM_COUNT;
	}

	public Integer getMISS_COUNT() {
		return MISS_COUNT;
	}

	public Double getAllele_freq() {
		return allele_freq;
	}
	
	public boolean isHasExac() {
		return hasExac;
	}

	public void setHasExac(Boolean hasExac) {
		this.hasExac = hasExac;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
