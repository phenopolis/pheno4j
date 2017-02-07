package com.graph.db.file.person.subscriber;

import java.util.Map;

import com.graph.db.domain.output.PersonOutput;
import com.graph.db.file.SetBasedGenericSubscriber;
import com.graph.db.output.OutputFileType;

public class PersonSubscriber extends SetBasedGenericSubscriber<Map<String, Object>, PersonOutput> {
	
	public PersonSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.PERSON);
	}
	
	@Override
	public void processRow(Map<String, Object> map) {
		set.add(new PersonOutput(map));
	}
}
