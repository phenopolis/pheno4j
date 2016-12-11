package com.graph.db.file.transcript;

import static com.graph.db.util.FileUtil.createLineNumberReaderForGzipFile;
import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.Parser;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.transcript.subscriber.GeneSubscriber;
import com.graph.db.output.HeaderGenerator;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.Constants;
import com.graph.db.util.ManagedEventBus;

/**
 * Nodes
 * - Transcript
 * - Gene
 * 
 * Relationships
 * - TranscriptToGene
 */
public class TranscriptParser implements Parser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TranscriptParser.class);

	private final String fileName;
	private final String outputFolder;
	
	private final ManagedEventBus eventBus;
	private final List<? extends AutoCloseable> subscribers;
	
	public TranscriptParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
		
		eventBus = new ManagedEventBus(getClass().getSimpleName());
		subscribers = createSubscribers();
	}
	
	private List<? extends AutoCloseable> createSubscribers() {
		GeneSubscriber geneSubscriber = new GeneSubscriber(outputFolder, getClass());
		GenericSubscriber<Object> transcriptSubscriber = new GenericSubscriber<>(outputFolder, getClass(), OutputFileType.TRANSCRIPT);
		GenericSubscriber<Object> transcriptToGeneSubscriber = new GenericSubscriber<>(outputFolder, getClass(), OutputFileType.TRANSCRIPT_TO_GENE);
		return Arrays.asList(geneSubscriber, transcriptSubscriber, transcriptToGeneSubscriber);
	}

	@Override
	public void execute() {
		registerSubscribers();
		try (LineNumberReader reader = createLineNumberReaderForGzipFile(fileName);) {
			String line;
			
			while (( line = reader.readLine()) != null) {
				logLineNumber(reader, 1000);
				
				if (isNotCommentRow(line)) {
					String[] columns = StringUtils.split(line, Constants.TAB);
					if (isTranscriptRow(columns)) {
						String[] cells = StringUtils.split(columns[8], Constants.SEMI_COLON);
						
						Map<String, String> map = splitCellsIntoKeyValuePairs(cells);
						eventBus.post(map);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		closeEventBus();
		closeSubscribers();
		
		generateHeaderFiles();
	}

	private Map<String, String> splitCellsIntoKeyValuePairs(String... cells) {
		Map<String, String> result = new HashMap<>();
		for (String cell : cells) {
			String[] keyAndValue = StringUtils.split(StringUtils.trimToEmpty(cell), Constants.SPACE);
			
			String originalValue = StringUtils.replace(keyAndValue[1], Constants.DOUBLE_QUOTE, StringUtils.EMPTY);
			String newValue = getValueForKey(keyAndValue[0], originalValue);
			
			result.put(keyAndValue[0], newValue);
		}
		return result;
	}

	private String getValueForKey(String key, String originalValue) {
		final String value;
		switch (key) {
		case "gene_id":
		case "transcript_id":
			value = StringUtils.substringBefore(originalValue, ".");
			break;
		default:
			value = originalValue;
			break;
		}
		return value;
	}

	private boolean isNotCommentRow(String line) {
		return !line.startsWith("##");
	}
	
	private boolean isTranscriptRow(String[] cells) {
		return "transcript".equals(cells[2]);
	}

	private void registerSubscribers() {
		subscribers.forEach(subscriber -> eventBus.register(subscriber));
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
		EnumSet<OutputFileType> outputFileTypes = EnumSet.of(OutputFileType.GENE, OutputFileType.TRANSCRIPT, OutputFileType.TRANSCRIPT_TO_GENE);
		new HeaderGenerator().generateHeaders(outputFolder, outputFileTypes);
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=inputFile, $2=outputFolder");
		}
		new TranscriptParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}

}
