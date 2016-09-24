package com.graph.db.file.gene;

import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.graph.db.Processor;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.output.HeaderGenerator;
import com.graph.db.file.annotation.output.OutputFileType;
import com.graph.db.file.gene.domain.Gene;
import com.graph.db.file.gene.subscriber.GeneToTermSubscriber;

public class GeneParser implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneParser.class);

	private final String fileName;
	private final String outputFolder;
	
	private final Gson gson = new Gson();
	private final EventBus eventBus;
	private final GenericSubscriber<Object> geneSubscriber;
	private final GeneToTermSubscriber geneToTermSubscriber;
	
	public GeneParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
		
		eventBus = new EventBus();
		geneSubscriber = new GenericSubscriber<Object>(outputFolder, OutputFileType.GENE);
		geneToTermSubscriber = new GeneToTermSubscriber(outputFolder, OutputFileType.GENE_TO_TERM);
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
		eventBus.register(geneSubscriber);
		eventBus.register(geneToTermSubscriber);
	}

	private void closeSubscribers() {
		geneSubscriber.close();
		geneToTermSubscriber.close();
	}

	private void generateHeaderFiles() {
		EnumSet<OutputFileType> outputFileTypes = EnumSet.of(OutputFileType.GENE, OutputFileType.GENE_TO_TERM);
		new HeaderGenerator().generateHeaders(outputFolder, outputFileTypes);
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=geneFile, $2=outputFolder");
		}
		new GeneParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}

}
