package com.graph.db.domain.output;

import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.annotation.Id;

public class TranscriptVariantOutput {

	@Id(name = "hgvsc")
	private final String hgvsc;
	private final String hgvsp;
	private final String impact;
	private final String intron;
	private final String exon;
	
	public TranscriptVariantOutput(TranscriptConsequence transcriptConsequence) {
		this.hgvsc = transcriptConsequence.getHgvsc();
		this.hgvsp = transcriptConsequence.getHgvsp();
		this.impact = transcriptConsequence.getImpact();
		this.intron = transcriptConsequence.getIntron();
		this.exon = transcriptConsequence.getExon();
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

	public String getIntron() {
		return intron;
	}

	public String getExon() {
		return exon;
	}
}
