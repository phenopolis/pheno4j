package com.graph.db.file.gene.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class GeneToTerm {
	
	private final String geneSymbol;
	private final String termId;
	
	public GeneToTerm(String geneSymbol, String termId) {
		this.geneSymbol = geneSymbol;
		this.termId = termId;
	}

	public String getGeneSymbol() {
		return geneSymbol;
	}

	public String getTermId() {
		return termId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
