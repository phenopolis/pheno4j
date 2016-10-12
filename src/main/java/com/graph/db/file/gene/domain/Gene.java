package com.graph.db.file.gene.domain;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Gene {
	
	private final String gene;
	private List<String> hpo;
	
	public Gene(String gene) {
		this.gene = gene;
	}
	
	public String getGene() {
		return gene;
	}

	public List<String> getHpo() {
		return hpo;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.gene)
				.append(this.hpo)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof Gene) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final Gene otherObject = (Gene) obj;
		return new EqualsBuilder()
				.append(this.gene, otherObject.gene)
				.append(this.hpo, otherObject.hpo)
				.isEquals();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
