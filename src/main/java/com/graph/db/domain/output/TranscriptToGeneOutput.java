package com.graph.db.domain.output;

import java.util.Map;

public class TranscriptToGeneOutput {
	
	private final String transcript_id;
	private final String gene_id;
	
	public TranscriptToGeneOutput(Map<String, String> map) {
		this.transcript_id = map.get("transcript_id");
		this.gene_id = map.get("gene_id");
	}

	public String getTranscript_id() {
		return transcript_id;
	}

	public String getGene_id() {
		return gene_id;
	}

}
