package com.graph.db.file.gene;

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

import com.google.common.eventbus.EventBus;
import com.graph.db.Parser;
import com.graph.db.domain.output.GeneToTermOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.HeaderGenerator;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.Constants;

/**
 * Relationships
 * - GeneToTerm
 */
public class GeneParser implements Parser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneParser.class);

	private final String fileName;
	private final String outputFolder;
	
	private final EventBus eventBus;
	private final List<? extends AutoCloseable> subscribers;
	
	public GeneParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
		
		eventBus = new EventBus();
		subscribers = createSubscribers();
	}
	
	private List<? extends AutoCloseable> createSubscribers() {
		GenericSubscriber<GeneToTermOutput> geneToTermSubscriber = new GenericSubscriber<>(outputFolder, getClass(), OutputFileType.GENE_TO_TERM);
		return Arrays.asList(geneToTermSubscriber);
	}

	@Override
	public void execute() {
		registerSubscribers();
		
		try (LineNumberReader reader = createLineNumberReaderForGzipFile(fileName);) {
			String[] header = null;
			String line;
			
			while (( line = reader.readLine()) != null) {
				logLineNumber(reader, 1000);
				
				if (header == null) {
					header = StringUtils.split(line, Constants.TAB);
				}
				
				String[] columns = StringUtils.split(line, Constants.TAB);
				
				if (header.length == columns.length) {
					Map<String, String> map = splitColumnsIntoKeyValuePairs(header, columns);
					GeneToTermOutput output = new GeneToTermOutput(map);
					eventBus.post(output);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		closeSubscribers();
		
		generateHeaderFiles();
	}

	private Map<String, String> splitColumnsIntoKeyValuePairs(String[] header, String[] columns) {
		Map<String, String> result = new HashMap<>();
		for (int i = 0; i < header.length; i++) {
				result.put(header[i], columns[i]);
		}
		return result;
	}

	private void registerSubscribers() {
		subscribers.forEach(subscriber -> eventBus.register(subscriber));
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
		EnumSet<OutputFileType> outputFileTypes = EnumSet.of(OutputFileType.GENE_TO_TERM);
		new HeaderGenerator().generateHeaders(outputFolder, outputFileTypes);
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=inputFile, $2=outputFolder");
		}
		new GeneParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}

}
