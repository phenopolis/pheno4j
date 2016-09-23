package com.graph.db.file.gene.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Gene {
	
	private String gene;
	private List<String> hpo;
	
	public String getGene() {
		return gene;
	}

	public List<String> getHpo() {
		return hpo;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
