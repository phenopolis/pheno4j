package com.graph.db.domain.input.annotation;

import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GeneticVariant {
	
    private Custom_annotations custom_annotations;
    private Integer end;
    private Set<TranscriptConsequence> transcript_consequences;
    private String input;
    private String seq_region_name;
    private String allele_string;
    private Integer start;
    private String most_severe_consequence;
    private Integer strand;
    private String variant_class;
    private String assembly_name;
    private String variant_id;
    
    public Custom_annotations getCustom_annotations() {
		return custom_annotations;
	}

	public Integer getEnd() {
		return end;
	}

	public Set<TranscriptConsequence> getTranscript_consequences() {
		return transcript_consequences;
	}

	public String getInput() {
		return input;
	}

	public String getSeq_region_name() {
		return seq_region_name;
	}

	public String getAllele_string() {
		return allele_string;
	}

	public Integer getStart() {
		return start;
	}

	public String getMost_severe_consequence() {
		return most_severe_consequence;
	}

	public Integer getStrand() {
		return strand;
	}

	public String getVariant_class() {
		return variant_class;
	}

	public String getAssembly_name() {
		return assembly_name;
	}

	public String getVariant_id() {
		return variant_id;
	}

	public void setTranscript_consequences(Set<TranscriptConsequence> transcript_consequences) {
		this.transcript_consequences = transcript_consequences;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
