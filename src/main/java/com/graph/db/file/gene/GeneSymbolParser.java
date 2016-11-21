package com.graph.db.file.gene;

import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.graph.db.Parser;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.gene.domain.Gene;
import com.graph.db.file.gene.subscriber.GeneSymbolToTermSubscriber;
import com.graph.db.output.HeaderGenerator;
import com.graph.db.output.OutputFileType;

/**
 * Relationships
 * - GeneSymbolToTerm
 */
public class GeneSymbolParser implements Parser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneSymbolParser.class);

	private final String fileName;
	private final String outputFolder;
	
	private final Gson gson = new Gson();
	private final EventBus eventBus;
	private final List<GenericSubscriber<?>> subscribers;
	
	public GeneSymbolParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
		
		eventBus = new EventBus();
		subscribers = createSubscribers(outputFolder);
	}
	
	private List<GenericSubscriber<?>> createSubscribers(String outputFolder2) {
		GeneSymbolToTermSubscriber geneSymbolToTermSubscriber = new GeneSymbolToTermSubscriber(outputFolder, getClass(), OutputFileType.GENE_SYMBOL_TO_TERM);
		return Arrays.asList(geneSymbolToTermSubscriber);
	}

	@Override
	public void execute() {
		registerSubscribers();
		try (LineNumberReader reader = new LineNumberReader(new FileReader(fileName));) {
			String line;
			
			while (( line = reader.readLine()) != null) {
				logLineNumber(reader, 1000);
				
				Gene gene = gson.fromJson(line, Gene.class);
				eventBus.post(gene);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		closeSubscribers();
		
		generateHeaderFiles();
	}

	private void registerSubscribers() {
		subscribers.forEach(subscriber -> eventBus.register(subscriber));
	}

	private void closeSubscribers() {
		subscribers.forEach(subscriber -> subscriber.close());
	}

	private void generateHeaderFiles() {
		EnumSet<OutputFileType> outputFileTypes = EnumSet.of(OutputFileType.GENE_SYMBOL_TO_TERM);
		new HeaderGenerator().generateHeaders(outputFolder, outputFileTypes);
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=geneFile, $2=outputFolder");
		}
		new GeneSymbolParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}
}
