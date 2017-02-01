package com.graph.db.domain.output;

import java.util.Map;

import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class TranscriptToGeneOutput {
	
	@RelationshipStart(mapping = Neo4jMapping.Transcript)
	private final String transcript_id;
	
	@RelationshipEnd(mapping = Neo4jMapping.Gene)
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
