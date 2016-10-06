package com.graph.db.file.person;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.Constants.SEMI_COLON;
import static com.graph.db.util.FileUtil.getLines;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.Parser;
import com.graph.db.util.FileUtil;

/**
 * Creates following files:
 * 
 * Person to Observed Term
 * Person to Non Observed Term
 * Person to Gene
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
		List<String> lines = getLines(fileName);
		
		Set<String> personToObservedTerm = new HashSet<>();
		Set<String> personToNonObservedTerm = new HashSet<>();
		Set<String> personToGene = new HashSet<>();
		for (String line : lines) {
			String[] fields = line.split(COMMA);
			String personId = StringUtils.wrap(fields[0], DOUBLE_QUOTE);
			String observedTermsField = fields[2];
			String nonObservedTermsField = fields[3];
			String genesField = fields[4];
			
			splitAndAddToSet(personToObservedTerm, personId, observedTermsField);
			splitAndAddToSet(personToNonObservedTerm, personId, nonObservedTermsField);
			splitAndAddToSet(personToGene, personId, genesField);
		}
		writeOutHeaderAndRows("PersonToObservedTerm", "Term", personToObservedTerm);
		writeOutHeaderAndRows("PersonToNonObservedTerm", "Term", personToNonObservedTerm);
		writeOutHeaderAndRows("PersonToGene", "Gene", personToGene);
	}

	private void splitAndAddToSet(Set<String> map, String personId, String field) {
		String[] fields = StringUtils.split(field, SEMI_COLON);
		for (String cell : fields) {
			String wrappedCell = StringUtils.wrap(cell, DOUBLE_QUOTE);
			map.add(StringUtils.join(Arrays.asList(personId, wrappedCell), COMMA));
		}
	}
	
	private void writeOutHeaderAndRows(String fileTag, String targetEntity, Set<String> set) {
		FileUtil.writeOutCsvFile(outputFolder, fileTag + "-header.csv", Arrays.asList(":START_ID(Person),:END_ID(" + targetEntity + ")"));
		FileUtil.writeOutCsvFile(outputFolder, fileTag + ".csv", set);
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=termFile, $2=outputFolder");
		}
		new PersonParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}

}
