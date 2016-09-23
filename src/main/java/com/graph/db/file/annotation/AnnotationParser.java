package com.graph.db.file.annotation;

import static com.graph.db.util.FileUtil.getAllJsonFiles;
import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
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
import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.output.HeaderGenerator;
import com.graph.db.file.annotation.subscriber.VariantToAnnotationSubscriber;
import com.graph.db.file.annotation.subscriber.GeneSubscriber;
import com.graph.db.file.annotation.subscriber.GeneToVariantSubscriber;

public class AnnotationParser implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationParser.class);
	
	private final String inputFolder;
	private final String outputFolder;

	private final Gson gson;
	
	private final ExecutorService threadPool;
	private final EventBus eventBus;
	private final GeneSubscriber geneSubscriber;
	private final GeneToVariantSubscriber geneToVariantSubscriber;
	private final VariantToAnnotationSubscriber variantToAnnotationSubscriber;


	public AnnotationParser(String inputFolder, String outputFolder) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
		
		gson = createGson();
        
		threadPool = Executors.newFixedThreadPool(10);
		eventBus = new AsyncEventBus(threadPool);
        geneSubscriber = new GeneSubscriber(outputFolder);
        geneToVariantSubscriber = new GeneToVariantSubscriber(outputFolder);
        variantToAnnotationSubscriber = new VariantToAnnotationSubscriber(outputFolder);
	}

	private Gson createGson() {
		GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Annotation.class, new CustomJsonDeserializer());
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
					
					Annotation annotation = gson.fromJson(line, Annotation.class);
					eventBus.post(annotation);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		shutDownThreadPool();
		closeSubscribers();
		
		new HeaderGenerator().generateHeaders(outputFolder);
	}

	private void registerSubscribers() {
		eventBus.register(geneSubscriber);
		eventBus.register(geneToVariantSubscriber);
		eventBus.register(variantToAnnotationSubscriber);
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
		geneSubscriber.close();
		geneToVariantSubscriber.close();
		variantToAnnotationSubscriber.close();
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=inputFolder, $2=outputFolder");
		}
		new AnnotationParser(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}
}
