package com.graph.db.file.term;

import static com.graph.db.util.Constants.COLON;
import static com.graph.db.util.FileUtil.logLineNumber;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.domain.input.term.RawTerm;
import com.graph.db.domain.output.TermOutput;
import com.graph.db.file.AbstractParser;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.term.subscriber.TermToDescendantTermsSubscriber;
import com.graph.db.file.term.subscriber.TermToParentTermSubscriber;
import com.graph.db.output.OutputFileType;

/**
 * Nodes
 * - Term
 * 
 * Relationships
 * - TermToParentTerm
 * - TermToDescendantTerms
 */
public class TermParser extends AbstractParser {
	
	private final String fileName;
	
	public TermParser() {
		this(config.getString("termParser.input.fileName"));
	}
	
	public TermParser(String fileName) {
		this.fileName = fileName;
		if (StringUtils.isBlank(fileName)) {
			throw new RuntimeException("fileName cannot be empty");
		}
	}
	
	@Override
	public Collection<File> getInputFiles() {
		return Collections.singleton(new File(fileName));
	}
	
	@Override
	protected List<? extends AutoCloseable> createSubscribers() {
		GenericSubscriber<TermOutput> termSubscriber = new GenericSubscriber<>(outputFolder, getParserClass(), OutputFileType.TERM);
		GenericSubscriber<RawTerm> termToParentTerm = new TermToParentTermSubscriber(outputFolder, getParserClass());
		GenericSubscriber<RawTerm> termToDescendantTerms = new TermToDescendantTermsSubscriber(outputFolder, getParserClass());
		return Arrays.asList(termSubscriber, termToParentTerm, termToDescendantTerms);
	}
	
	@Override
	public void processDataForFile(LineNumberReader reader) throws IOException {
		String line;
		
		while (( line = reader.readLine()) != null) {
			logLineNumber(reader, 1000);
			
			if ("[Term]".equals(line)) {
				List<String> linesForTerm = new ArrayList<>();
				while (( line = reader.readLine()) != null) {
					if (EMPTY.equals(line)) {
						break;
					} else {
						linesForTerm.add(line);
					}
				}
				
				RawTerm rawTerm = createRawTermFromLines(linesForTerm);
				eventBus.post(rawTerm);
			}
		}
	}
	
	protected RawTerm createRawTermFromLines(List<String> linesForTerm) {
		String termId = null;
		String name = null;
		List<String> is = new ArrayList<>();
		for (String line : linesForTerm) {
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
		return new RawTerm(termId, name, is);
	}

	@Override
	public Class<?> getParserClass() {
		return TermParser.class;
	}
	
	public static void main(String[] args) {
		new TermParser().execute();
	}
}
