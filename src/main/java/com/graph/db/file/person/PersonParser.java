package com.graph.db.file.person;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.SEMI_COLON;
import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.file.AbstractParser;
import com.graph.db.file.AbstractSubscriber;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.person.subscriber.PersonSubscriber;
import com.graph.db.file.person.subscriber.PersonToTermSubscriber;
import com.graph.db.output.OutputFileType;

/**
 * Nodes
 * - Person
 * 
 * Relationships
 * - PersonToObservedTerm
 * - PersonToNonObservedTerm
 */
public class PersonParser extends AbstractParser {
	
	public static final String PERSON_KEY = "eid";
	private static final String NON_OBSERVED_TERMS_KEY = "non_observed_features";
	private static final String OBSERVED_TERMS_KEY = "observed_features";

	private final String fileName;
	
	public PersonParser() {
		this.fileName = config.getString("personParser.input.fileName");
	}
	
	public PersonParser(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public Collection<File> getInputFiles() {
		return Collections.singleton(new File(fileName));
	}
	
	@Override
	protected List<AbstractSubscriber> createSubscribers() {
		GenericSubscriber<Map<String, Object>> personSubscriber = new PersonSubscriber(outputFolder, getParserClass());
		GenericSubscriber<Map<String, Object>> personToObservedTermSubscriber = new PersonToTermSubscriber(outputFolder, getParserClass(), OutputFileType.PERSON_TO_OBSERVED_TERM, OBSERVED_TERMS_KEY);
		GenericSubscriber<Map<String, Object>> personToNonObservedTermSubscriber = new PersonToTermSubscriber(outputFolder, getParserClass(), OutputFileType.PERSON_TO_NON_OBSERVED_TERM, NON_OBSERVED_TERMS_KEY);
        
		return Arrays.asList(personSubscriber, personToObservedTermSubscriber, personToNonObservedTermSubscriber);
	}

	@Override
	public void processDataForFile(LineNumberReader reader) throws IOException {
		String[] header = null;
		String line;
		
		while (( line = reader.readLine()) != null) {
			logLineNumber(reader, 1000);
			
			if (header == null) {
				header = StringUtils.split(line, COMMA);
				continue;
			}
			
			String[] columns = StringUtils.split(line, COMMA);
			if (header.length == columns.length) {
				Map<String, Object> map = splitColumnsIntoKeyValuePairs(header, columns);
				eventBus.post(map);
			}
		}
	}
	
	private Map<String, Object> splitColumnsIntoKeyValuePairs(String[] header, String[] columns) {
		Map<String, Object> result = new HashMap<>();
		for (int i = 0; i < header.length; i++) {
			String key = header[i];
			if (OBSERVED_TERMS_KEY.equals(key) || NON_OBSERVED_TERMS_KEY.equals(key)) {
				String[] value = StringUtils.split(columns[i], SEMI_COLON);
				result.put(key, value);
			} else {
				result.put(key, columns[i]);
			}
		}
		return result;
	}

	@Override
	public Class<?> getParserClass() {
		return PersonParser.class;
	}

	public static void main(String[] args) {
		new PersonParser().execute();
	}
}
