package com.graph.db.file.transcript;

import static com.graph.db.util.FileUtil.getLineNumberReaderForFile;
import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.file.AbstractParser;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.transcript.subscriber.GeneSubscriber;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.Constants;

/**
 * Nodes
 * - Transcript
 * - Gene
 * 
 * Relationships
 * - TranscriptToGene
 */
public class TranscriptParser extends AbstractParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TranscriptParser.class);

	private final String fileName;
	
	public TranscriptParser() {
		this.fileName = config.getString("transcriptParser.input.fileName");
		if (StringUtils.isBlank(fileName)) {
			throw new RuntimeException("fileName cannot be empty");
		}
	}
	
	public TranscriptParser(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	protected List<? extends AutoCloseable> createSubscribers() {
		GeneSubscriber geneSubscriber = new GeneSubscriber(outputFolder, getParserClass());
		GenericSubscriber<Object> transcriptSubscriber = new GenericSubscriber<>(outputFolder, getParserClass(), OutputFileType.TRANSCRIPT);
		GenericSubscriber<Object> transcriptToGeneSubscriber = new GenericSubscriber<>(outputFolder, getParserClass(), OutputFileType.TRANSCRIPT_TO_GENE);
		return Arrays.asList(geneSubscriber, transcriptSubscriber, transcriptToGeneSubscriber);
	}

	@Override
	public void execute() {
		registerSubscribers();
		try (LineNumberReader reader = getLineNumberReaderForFile(fileName);) {
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

	@Override
	public Class<?> getParserClass() {
		return TranscriptParser.class;
	}

	public static void main(String[] args) {
		new TranscriptParser().execute();
		LOGGER.info("Finished");
	}
}
