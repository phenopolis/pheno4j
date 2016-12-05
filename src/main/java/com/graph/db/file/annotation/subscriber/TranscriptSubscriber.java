package com.graph.db.file.annotation.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.TranscriptOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.OutputFileType;

public class TranscriptSubscriber extends GenericSubscriber<GeneticVariant> {
	
	private final Set<TranscriptOutput> set = ConcurrentHashMap.newKeySet();

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
	
	@Override
	public void close() {
		try {
			for (TranscriptOutput s : set) {
				beanWriter.write(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}

}
