package com.graph.db.file.term;

import static com.graph.db.util.Constants.COLON;
import static com.graph.db.util.FileUtil.logLineNumber;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.Parser;
import com.graph.db.domain.input.term.RawTerm;
import com.graph.db.domain.output.TermOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.term.subscriber.TermToDescendantTermsSubscriber;
import com.graph.db.file.term.subscriber.TermToParentTermSubscriber;
import com.graph.db.output.HeaderGenerator;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.ManagedEventBus;

/**
 * Nodes
 * - Term
 * 
 * Relationships
 * - TermToParentTerm
 * - TermToDescendantTerms
 */
public class TermParser implements Parser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TermParser.class);
	
	private final String fileName;
	private final String outputFolder;
	
	private final ManagedEventBus eventBus;
	private final List<? extends AutoCloseable> subscribers;

	public TermParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
		
		eventBus = new ManagedEventBus(getClass().getSimpleName());
		subscribers = createSubscribers();
	}
	
	private List<? extends AutoCloseable> createSubscribers() {
		GenericSubscriber<TermOutput> termSubscriber = new GenericSubscriber<>(outputFolder, getClass(), OutputFileType.TERM);
		GenericSubscriber<RawTerm> termToParentTerm = new TermToParentTermSubscriber(outputFolder, getClass());
		GenericSubscriber<RawTerm> termToDescendantTerms = new TermToDescendantTermsSubscriber(outputFolder, getClass());
		return Arrays.asList(termSubscriber, termToParentTerm, termToDescendantTerms);
	}
	
	@Override
	public void execute() {
		registerSubscribers();
		try (LineNumberReader reader = new LineNumberReader(new FileReader(fileName))) {
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
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		closeEventBus();
		closeSubscribers();
		
		generateHeaderFiles();
	}
	
	private void registerSubscribers() {
		subscribers.forEach(subscriber -> eventBus.register(subscriber));
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

	private void closeEventBus() {
		try {
			eventBus.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void closeSubscribers() {
		subscribers.forEach(subscriber -> {
			try {
				subscriber.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	private void generateHeaderFiles() {
		EnumSet<OutputFileType> outputFileTypes = EnumSet.of(OutputFileType.TERM,
				OutputFileType.TERM_TO_DESCENDANT_TERMS, OutputFileType.TERM_TO_PARENT_TERM);
		new HeaderGenerator().generateHeaders(outputFolder, outputFileTypes);
	}
	
	@Override
	public EnumSet<OutputFileType> getNonHeaderOutputFileTypes() {
		return EnumSet.of(OutputFileType.TERM, OutputFileType.TERM_TO_PARENT_TERM,
				OutputFileType.TERM_TO_DESCENDANT_TERMS);
	}
	
	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=termFile, $2=outputFolder");
		}
		new TermParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}
}
