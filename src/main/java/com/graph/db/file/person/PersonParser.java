package com.graph.db.file.person;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.Constants.SEMI_COLON;
import static com.graph.db.util.FileUtil.getLines;
import static com.graph.db.util.FileUtil.writeOutCsvFile;
import static com.graph.db.util.FileUtil.writeOutCsvHeader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.Parser;
import com.graph.db.util.Constants;

/**
 * Relationships
 * - PersonToObservedTerm
 * - PersonToNonObservedTerm
 */
public class PersonParser implements Parser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonParser.class);
	
	private final String fileName;
	private final String outputFolder;

	public PersonParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
	}
	
	@Override
	public void execute() {
		List<String> lines = getLines(fileName, true);
		
		Set<String> person = new HashSet<>();
		Set<String> personToObservedTerm = new HashSet<>();
		Set<String> personToNonObservedTerm = new HashSet<>();
		
		BiFunction<String, String, String> returnPersonIdAndValue = (personId, value) -> (StringUtils.join(Arrays.asList(personId, value), COMMA));
		
		for (String line : lines) {
			String[] fields = line.split(COMMA);
			String personId = wrapIfConstainsComma(fields[0]);
			String observedTermsField = fields[2];
			String nonObservedTermsField = fields[3];
			
			person.add(personId);
			splitAndAddToSet(personToObservedTerm, personId, observedTermsField, returnPersonIdAndValue);
			splitAndAddToSet(personToNonObservedTerm, personId, nonObservedTermsField, returnPersonIdAndValue);
		}
		
		writeOutCsvFile(outputFolder, getClass(), "Person", person);
		writeOutHeaderAndRows("PersonToObservedTerm", "Term", personToObservedTerm);
		writeOutHeaderAndRows("PersonToNonObservedTerm", "Term", personToNonObservedTerm);
	}

	private void splitAndAddToSet(Set<String> set, String personId, String field, BiFunction<String, String, String> function) {
		String[] fields = StringUtils.split(field, SEMI_COLON);
		for (String cell : fields) {
			cell = wrapIfConstainsComma(cell);
			set.add(function.apply(personId, cell));
		}
	}

	private String wrapIfConstainsComma(String cell) {
		return cell.contains(Constants.COMMA) ? StringUtils.wrap(cell, DOUBLE_QUOTE) : cell;
	}
	
	private void writeOutHeaderAndRows(String fileTag, String targetEntity, Set<String> set) {
		writeOutCsvHeader(outputFolder, fileTag, Arrays.asList(":START_ID(Person),:END_ID(" + targetEntity + ")"));
		writeOutCsvFile(outputFolder, getClass(), fileTag, set);
	}
	
	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=termFile, $2=outputFolder");
		}
		new PersonParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}

}
