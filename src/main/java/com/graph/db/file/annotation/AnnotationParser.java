package com.graph.db.file.annotation;

import static com.graph.db.util.FileUtil.getAllJsonFiles;
import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.graph.db.Processor;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.domain.AnnotatedVariant;
import com.graph.db.file.annotation.output.HeaderGenerator;
import com.graph.db.file.annotation.output.OutputFileType;
import com.graph.db.file.annotation.subscriber.AnnotatedGeneSubscriber;
import com.graph.db.file.annotation.subscriber.GeneToAnnotatedGeneSubscriber;
import com.graph.db.file.annotation.subscriber.GeneToVariantSubscriber;

public class AnnotationParser implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationParser.class);
	
	private final String inputFolder;
	private final String outputFolder;

	private final Gson gson;
	
	private final ExecutorService threadPool;
	private final EventBus eventBus;
	private final AnnotatedGeneSubscriber annotatedGeneSubscriber;
	private final GeneToVariantSubscriber geneToVariantSubscriber;
	private final GenericSubscriber<Object> annotatedVariantSubscriber;
	private final GenericSubscriber<Object> variantToAnnotatedVariantSubscriber;
	private final GeneToAnnotatedGeneSubscriber geneToAnnotatedGeneSubscriber;

	public AnnotationParser(String inputFolder, String outputFolder) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
		
		gson = createGson();
        
		threadPool = Executors.newFixedThreadPool(10);
		eventBus = new AsyncEventBus(threadPool);
        annotatedGeneSubscriber = new AnnotatedGeneSubscriber(outputFolder, OutputFileType.ANNOTATED_GENE);
        geneToVariantSubscriber = new GeneToVariantSubscriber(outputFolder, OutputFileType.ANNOTATED_GENE_TO_VARIANT);
        annotatedVariantSubscriber = new GenericSubscriber<Object>(outputFolder, OutputFileType.ANNOTATED_VARIANT);
        variantToAnnotatedVariantSubscriber = new GenericSubscriber<Object>(outputFolder, OutputFileType.VARIANT_TO_ANNOTATED_VARIANT);
        geneToAnnotatedGeneSubscriber = new GeneToAnnotatedGeneSubscriber(outputFolder, OutputFileType.GENE_TO_ANNOTATED_GENE);
	}

	private Gson createGson() {
		GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(AnnotatedVariant.class, new CustomJsonDeserializer());
        return b.create();
	}
	
	@Override
	public void execute() {
		File[] jsonFiles = getAllJsonFiles(inputFolder);

		registerSubscribers();
		
		for (File jsonFile : jsonFiles) {
			LOGGER.info("Processing file: {}", jsonFile);
			
			try (LineNumberReader reader = new LineNumberReader(new FileReader(jsonFile));) {
				String line;
				
				while (( line = reader.readLine()) != null) {
					logLineNumber(reader, 1000);
					
					AnnotatedVariant annotatedVariant = gson.fromJson(line, AnnotatedVariant.class);
					eventBus.post(annotatedVariant);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		shutDownThreadPool();
		closeSubscribers();
		
		generateHeaderFiles();
	}

	private void registerSubscribers() {
		eventBus.register(annotatedGeneSubscriber);
		eventBus.register(geneToVariantSubscriber);
		eventBus.register(annotatedVariantSubscriber);
		eventBus.register(variantToAnnotatedVariantSubscriber);
		eventBus.register(geneToAnnotatedGeneSubscriber);
	}

	private void shutDownThreadPool() {
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void closeSubscribers() {
		annotatedGeneSubscriber.close();
		geneToVariantSubscriber.close();
		annotatedVariantSubscriber.close();
		variantToAnnotatedVariantSubscriber.close();
		geneToAnnotatedGeneSubscriber.close();
	}

	private void generateHeaderFiles() {
		EnumSet<OutputFileType> outputFileTypes = EnumSet.of(OutputFileType.ANNOTATED_VARIANT, OutputFileType.VARIANT_TO_ANNOTATED_VARIANT,
				OutputFileType.ANNOTATED_GENE, OutputFileType.ANNOTATED_GENE_TO_VARIANT, OutputFileType.GENE_TO_ANNOTATED_GENE);
		new HeaderGenerator().generateHeaders(outputFolder, outputFileTypes);
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=inputFolder, $2=outputFolder");
		}
		new AnnotationParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}
}
