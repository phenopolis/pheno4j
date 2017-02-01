package com.graph.db.domain.output;

import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class TranscriptToTranscriptVariantOutput {
	
	@RelationshipStart(mapping = Neo4jMapping.Transcript)
	private final String transcript_id;
	
	@RelationshipEnd(mapping = Neo4jMapping.TranscriptVariant)
	private final String hgvsc;
	
	public TranscriptToTranscriptVariantOutput(TranscriptConsequence transcriptConsequence) {
		this.transcript_id = transcriptConsequence.getTranscript_id();
		this.hgvsc = transcriptConsequence.getHgvsc();
	}

	public String getTranscript_id() {
		return transcript_id;
	}

	public String getHgvsc() {
		return hgvsc;
	}
}
