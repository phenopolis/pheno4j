package com.graph.db.file.transcript.subscriber;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.graph.db.domain.output.GeneOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.OutputFileType;

public class GeneSubscriber extends GenericSubscriber<Map<String, String>> {
	
	private static final OutputFileType GENE = OutputFileType.GENE;
	
	private final Set<GeneOutput> set = ConcurrentHashMap.newKeySet();

	public GeneSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, GENE);
	}
	
	@Override
	public void processRow(Map<String, String> object) {
		GeneOutput geneOutput = new GeneOutput(object);
		set.add(geneOutput);
	}
	
	@Override
	public void close() {
		try {
			for (GeneOutput s : set) {
				beanWriter.write(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}

}
