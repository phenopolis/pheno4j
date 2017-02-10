package com.graph.db.domain.output;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.annotation.Id;

public class GeneOutput {
	
	@Id(name = "gene_id")
	private final String gene_id;
	private final String gene_name;
	
	public GeneOutput(Map<String, String> map) {
		this.gene_id = map.get("gene_id");
		this.gene_name = map.get("gene_name");
	}

	public GeneOutput(TranscriptConsequence transcriptConsequence) {
		this.gene_id = transcriptConsequence.getGene_id();
		this.gene_name = transcriptConsequence.getGene_symbol();
	}

	public String getGene_id() {
		return gene_id;
	}

	public String getGene_name() {
		return gene_name;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.gene_id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof GeneOutput) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final GeneOutput otherObject = (GeneOutput) obj;
		return new EqualsBuilder()
				.append(this.gene_id, otherObject.gene_id)
				.isEquals();
	}
}
