package com.graph.db.file.annotation.subscriber;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.TranscriptOutput;
import com.graph.db.file.SetBasedGenericSubscriber;
import com.graph.db.output.OutputFileType;

public class TranscriptSubscriber extends SetBasedGenericSubscriber<GeneticVariant, TranscriptOutput> {
	
	public TranscriptSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.TRANSCRIPT);
	}
	
	@Override
	public void processRow(GeneticVariant object) {
		for (TranscriptConsequence consequence : object.getTranscript_consequences()) {
			TranscriptOutput output = new TranscriptOutput(consequence);
			set.add(output);
		}
	}
}
