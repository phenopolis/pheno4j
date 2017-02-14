package com.graph.db.file.person.subscriber;

import java.util.List;
import java.util.Map;

import com.graph.db.domain.output.PersonToTermOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.person.PersonParser;
import com.graph.db.output.OutputFileType;

public class PersonToTermSubscriber extends GenericSubscriber<Map<String, Object>> {
	
	private final String listKey;

	public PersonToTermSubscriber(String outputFolder, Class<?> parserClass, OutputFileType type,
			String listKey) {
		super(outputFolder, parserClass, type);
		this.listKey = listKey;
	}

	@Override
	public void processRow(Map<String, Object> map) {
		String personId = (String) map.get(PersonParser.PERSON_KEY);
		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) map.get(listKey);
		
		for (String term : list) {
			PersonToTermOutput output = new PersonToTermOutput(personId, term);
			write(output);
		}
	}
}
