package com.graph.db.domain.output;

import static com.graph.db.util.Constants.COMMA;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.annotation.Id;

public class TranscriptOutput {
	
	@Id(name = "transcript_id")
	private final String transcript_id;
	private final Integer canonical;
	private final Integer strand;
	private final String ensembl_protein_id;
	private final String swissprot_protein_id;
	
	public TranscriptOutput(Map<String, String> map) {
		this.transcript_id = map.get("transcript_id");
		this.canonical = null;//TODO
		this.strand = null;
		this.ensembl_protein_id = null;
		this.swissprot_protein_id = null;
	}

	public TranscriptOutput(TranscriptConsequence consequence) {
		this.transcript_id = consequence.getTranscript_id();
		this.canonical = consequence.getCanonical();
		this.strand = consequence.getStrand();
		this.ensembl_protein_id = consequence.getProtein_id();
		
		if (ArrayUtils.isNotEmpty(consequence.getSwissprot())) {
			this.swissprot_protein_id = String.join(COMMA, consequence.getSwissprot());
		} else {
			this.swissprot_protein_id = null;
		}
	}

	public String getTranscript_id() {
		return transcript_id;
	}
	
	public Integer getCanonical() {
		return canonical;
	}

	public Integer getStrand() {
		return strand;
	}

	public String getEnsembl_protein_id() {
		return ensembl_protein_id;
	}

	public String getSwissprot_protein_id() {
		return swissprot_protein_id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.transcript_id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof TranscriptOutput) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final TranscriptOutput otherObject = (TranscriptOutput) obj;
		return new EqualsBuilder()
				.append(this.transcript_id, otherObject.transcript_id)
				.isEquals();
	}
}
