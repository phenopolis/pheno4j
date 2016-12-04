package com.graph.db.domain.output;

public class TranscriptVariantToConsequenceTermOutput {
	
	private final String hgvsc;
	private final String consequenceTerm;
	
	public TranscriptVariantToConsequenceTermOutput(String hgvsc, String consequenceTerm) {
		this.hgvsc = hgvsc;
		this.consequenceTerm = consequenceTerm;
	}

	public String getHgvsc() {
		return hgvsc;
	}

	public String getConsequenceTerm() {
		return consequenceTerm;
	}
	
}
