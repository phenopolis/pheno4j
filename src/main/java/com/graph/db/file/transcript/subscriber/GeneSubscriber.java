package com.graph.db.file.transcript.subscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.graph.db.file.GenericMapSubscriber;
import com.graph.db.output.OutputFileType;

public class GeneSubscriber extends GenericMapSubscriber<Map<String, String>> {
	
	private static final OutputFileType GENE = OutputFileType.GENE;
	
	private final Set<Map<String, String>> set = ConcurrentHashMap.newKeySet();

	public GeneSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, GENE);
	}
	
	@Override
	public void processAnnotation(Map<String, String> object) {
		Map<String, String> map = new HashMap<>();
		for (String key : GENE.getHeader()) {
			map.put(key, object.get(key));
		}
		
		set.add(map);
	}
	
	@Override
	public void close() {
		try {
			for (Map<String, String> s : set) {
				beanWriter.write(s, GENE.getHeader());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}

}
