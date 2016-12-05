package com.graph.db.domain.output;

import com.graph.db.domain.input.annotation.TranscriptConsequence;

public class TranscriptToTranscriptVariantOutput {
	
	private final String transcript_id;
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
