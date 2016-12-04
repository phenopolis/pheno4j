package com.graph.db.domain.output;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.graph.db.file.annotation.domain.TranscriptConsequence;

public class TranscriptVariantOutput {

	private final String hgvsc;
	private final String impact;
	private final String gene_symbol_source;
	private final Integer cdna_end;
	private final Integer cdna_start;
	private final Double cadd;
	private final String variant_allele;

	public TranscriptVariantOutput(TranscriptConsequence transcriptConsequence) {
		this.hgvsc = transcriptConsequence.getHgvsc();
		this.impact = transcriptConsequence.getImpact();
		this.gene_symbol_source = transcriptConsequence.getGene_symbol_source();
		this.cdna_end = transcriptConsequence.getCdna_end();
		this.cdna_start = transcriptConsequence.getCdna_start();
		this.variant_allele = transcriptConsequence.getVariant_allele();

		if (NumberUtils.isNumber(transcriptConsequence.getCadd())) {
			this.cadd = Double.valueOf(transcriptConsequence.getCadd());
		} else {
			this.cadd = null;
		}
	}
	
	public static String[] getFields() {
		List<String> fields = new ArrayList<>();
		for (Field field : TranscriptVariantOutput.class.getDeclaredFields()) {
			fields.add(field.getName());
		}
		return fields.toArray(new String[0]);
	}
	
	public String getHgvsc() {
		return hgvsc;
	}

	public String getImpact() {
		return impact;
	}

	public String getGene_symbol_source() {
		return gene_symbol_source;
	}

	public Integer getCdna_end() {
		return cdna_end;
	}

	public Integer getCdna_start() {
		return cdna_start;
	}

	public Double getCadd() {
		return cadd;
	}

	public String getVariant_allele() {
		return variant_allele;
	}

}
