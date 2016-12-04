package com.graph.db.domain.output;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.graph.db.file.annotation.domain.TranscriptConsequence;

public class TranscriptOutput {
	
	private final String transcript_id;
	
	public TranscriptOutput(Map<String, String> map) {
		this.transcript_id = map.get("transcript_id");
	}

	public TranscriptOutput(TranscriptConsequence consequence) {
		this.transcript_id = consequence.getTranscript_id();
	}

	public String getTranscript_id() {
		return transcript_id;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.transcript_id)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof TranscriptOutput) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final TranscriptOutput otherObject = (TranscriptOutput) obj;
		return new EqualsBuilder()
				.append(this.transcript_id, otherObject.transcript_id)
				.isEquals();
	}

}
