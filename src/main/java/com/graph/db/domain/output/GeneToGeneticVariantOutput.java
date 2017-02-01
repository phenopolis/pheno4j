package com.graph.db.domain.output;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class GeneToGeneticVariantOutput {
	
	@RelationshipStart(mapping = Neo4jMapping.Gene)
	private final String gene_id;
	
	@RelationshipEnd(mapping = Neo4jMapping.GeneticVariant)
	private final String variant_id;
	
	public GeneToGeneticVariantOutput(TranscriptConsequence transcriptConsequence) {
		this.gene_id = transcriptConsequence.getGene_id();
		this.variant_id = transcriptConsequence.getVariant_id();
	}

	public String getGene_id() {
		return gene_id;
	}

	public String getVariant_id() {
		return variant_id;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.gene_id)
				.append(this.variant_id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof GeneToGeneticVariantOutput) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final GeneToGeneticVariantOutput otherObject = (GeneToGeneticVariantOutput) obj;
		return new EqualsBuilder()
				.append(this.gene_id, otherObject.gene_id)
				.append(this.variant_id, otherObject.variant_id)
				.isEquals();
	}
}
