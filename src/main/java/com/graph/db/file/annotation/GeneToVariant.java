package com.graph.db.file.annotation;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.POISON_PILL;
import static com.graph.db.util.FileUtil.getAllJsonFiles;
import static com.graph.db.util.FileUtil.getTransformedVariantId;
import static com.graph.db.util.FileUtil.logLineNumber;
import static com.graph.db.util.FileUtil.writeOutCsvFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.graph.db.Processor;
import com.graph.db.util.QueueToFileConsumer;

public class GeneToVariant implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GeneToVariant.class);
	
	private final JsonParser jsonParser = new JsonParser();

	private final String inputFolder;
	private final String outputFolder;
	
	public GeneToVariant(String inputFolder, String outputFolder) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
	}
	
	@Override
	public void execute() {
		File[] jsonFiles = getAllJsonFiles(inputFolder);
		
		Set<String> genes = new HashSet<>();
		BlockingQueue<String> geneToVariantBlockingQueue = new ArrayBlockingQueue<>(1024);
		Runnable geneToVariantBlockingQueueConsumer = new QueueToFileConsumer(geneToVariantBlockingQueue, outputFolder, "GeneToVariant.csv");
		new Thread(geneToVariantBlockingQueueConsumer).start();
		
		for (File jsonFile : jsonFiles) {
			LOGGER.info("Processing file: {}", jsonFile);
			
			try (LineNumberReader reader = new LineNumberReader(new FileReader(jsonFile));) {
				String line;
				while ((line = reader.readLine()) != null) {
					logLineNumber(reader, 1000);
					JsonObject jsonObject = jsonParser.parse(line).getAsJsonObject();
					JsonElement variantIdJsonElement = jsonObject.get("variant_id");
					String variantId = getTransformedVariantId(variantIdJsonElement, jsonFile.getName());
					JsonArray genesArray = jsonObject.get("genes").getAsJsonArray();
					for (JsonElement jsonElement : genesArray) {
						String gene = jsonElement.getAsString();
						genes.add(gene);
						geneToVariantBlockingQueue.put(gene + COMMA + variantId);
					}
				}
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		try {
			geneToVariantBlockingQueue.put(POISON_PILL);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		writeOutCsvFile(outputFolder, "Gene.csv", genes);
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=inputFolder, $2=outputFolder");
		}
		new GeneToVariant(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}
}
