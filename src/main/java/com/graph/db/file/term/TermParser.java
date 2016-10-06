package com.graph.db.file.term;

import static com.graph.db.util.Constants.COLON;
import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.FileUtil.getLines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.Parser;
import com.graph.db.file.term.domain.RawTerm;
import com.graph.db.util.FileUtil;

public class TermParser implements Parser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TermParser.class);
	
	private final String fileName;
	private final String outputFolder;

	public TermParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
	}

	@Override
	public void execute() {
		List<String> lines = getLines(fileName);
		List<Pair<Integer, Integer>> pairs = getPairsOfIndexes(lines);
		List<RawTerm> rawTerms = getRawTerms(lines, pairs);
		
		writeOutTerms(rawTerms);
		writeOutTermRelationships(rawTerms);
	}
	
	private List<Pair<Integer, Integer>> getPairsOfIndexes(List<String> lines) {
		List<Pair<Integer, Integer>> pairs = new ArrayList<>();
		for (int i = 0; i < lines.size();) {
			int start = indexOfNextTerm(lines, i);
			int finish = indexOfNextTerm(lines, start+1);
			
			if (finish == -1) {
				finish = lines.size() - 1;
				i = lines.size();
			} else {
				i = finish;
			}
			pairs.add(new ImmutablePair<Integer, Integer>(start, finish));
		}
		return pairs;
	}
	
	private int indexOfNextTerm(List<String> list, int index) {
		for (int i = index; i < list.size(); i++) {
			if ("[Term]".equals(list.get(i))) {
				return i;
			}
		}
		return -1;
	}
	
	private List<RawTerm> getRawTerms(List<String> lines, List<Pair<Integer, Integer>> pairs) {
		List<RawTerm> terms = new ArrayList<>();
		for (Pair<Integer, Integer> pair : pairs) {
			List<String> subList = lines.subList(pair.getLeft()+1, pair.getRight());
			
			String termId = null;
			String name = null;
			List<String> is = new ArrayList<>();
			for (String line : subList) {
				if (StringUtils.isNotBlank(line)) {
					String key = StringUtils.substringBefore(line, COLON);
					switch (key) {
					case "id":
						termId = StringUtils.trim(StringUtils.substringAfter(line, COLON));
						break;
					case "is_a":
						String isA = StringUtils.substringBetween(line, COLON, "!");
						is.add(StringUtils.trim(isA));
						break;
					case "name":
						name = StringUtils.trim(StringUtils.substringAfter(line, COLON));
						name = StringUtils.replace(name, "\"", "\"\"");
						break;
					}
				}
			}
			RawTerm term = new RawTerm(termId, name, is);
			terms.add(term);
		}
		return terms;
	}
	
	private void writeOutTerms(List<RawTerm> rawTerms) {
		List<String> csvStrings = new ArrayList<>();
		for (RawTerm rawTerm : rawTerms) {
			String csvString = rawTerm.getTermId() + COMMA + StringUtils.wrap(rawTerm.getName(), DOUBLE_QUOTE);
			csvStrings.add(csvString);
		}
		FileUtil.writeOutCsvFile(outputFolder, "Term-header.csv", Arrays.asList("termId:ID(Term),name"));
		FileUtil.writeOutCsvFile(outputFolder, "Term.csv", csvStrings);
	}
	
	private void writeOutTermRelationships(List<RawTerm> rawTerms) {
		List<String> csvStrings = new ArrayList<>();
		for (RawTerm rawTerm : rawTerms) {
			for (String isA : rawTerm.getIsA()) {
				String csvString = rawTerm.getTermId() + COMMA + isA;
				csvStrings.add(csvString);
			}
		}
		FileUtil.writeOutCsvFile(outputFolder, "TermToTerm-header.csv", Arrays.asList(":START_ID(Term),:END_ID(Term)"));
		FileUtil.writeOutCsvFile(outputFolder, "TermToTerm.csv", csvStrings);
	}
	
	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=termFile, $2=outputFolder");
		}
		new TermParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}
}
