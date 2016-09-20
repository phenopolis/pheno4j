package com.graph.db.file.annotation;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.Constants.POISON_PILL;
import static com.graph.db.util.FileUtil.getAllJsonFiles;
import static com.graph.db.util.FileUtil.getTransformedVariantId;
import static com.graph.db.util.FileUtil.logLineNumber;
import static com.graph.db.util.FileUtil.writeOutCsvFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.graph.db.Processor;
import com.graph.db.util.QueueToFileConsumer;

public class ExacToVariant implements Processor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExacToVariant.class);
	
	private final JsonParser jsonParser = new JsonParser();

	private String inputFolder;
	private String outputFolder;
	
	public ExacToVariant(String inputFolder, String outputFolder) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
	}
	
	@Override
	public void execute() {
		File[] jsonFiles = getAllJsonFiles(inputFolder);
		
		Map<String, Integer> headers = getHeaders(jsonFiles);
		writeOutCsvFile(outputFolder, "Exac-header.csv", Arrays.asList(StringUtils.join(headers.keySet(), COMMA)));
		
		BlockingQueue<String> exacBlockingQueue = new ArrayBlockingQueue<>(1024);
		QueueToFileConsumer exacBlockingQueueConsumer = new QueueToFileConsumer(exacBlockingQueue, outputFolder, "Exac.csv");
		new Thread(exacBlockingQueueConsumer).start();
		
		BlockingQueue<String> variantToExacBlockingQueue = new ArrayBlockingQueue<>(1024);
		QueueToFileConsumer variantToExacBlockingQueueConsumer = new QueueToFileConsumer(variantToExacBlockingQueue, outputFolder, "VariantToExac.csv");
		new Thread(variantToExacBlockingQueueConsumer).start();
		
		for (File jsonFile : jsonFiles) {
			LOGGER.info("Processing file: {}", jsonFile);
			
			try (LineNumberReader reader = new LineNumberReader(new FileReader(jsonFile));) {
				String line;
				while (( line = reader.readLine()) != null) {
					logLineNumber(reader, 1000);
					Object[] values = new Object[headers.size()];
					
					JsonObject jsonObject = jsonParser.parse(line).getAsJsonObject();
					JsonElement exacElement = jsonObject.get("EXAC");
					if(!exacElement.isJsonNull()) {
						JsonObject exacObject = exacElement.getAsJsonObject();
						for (Entry<String, JsonElement> entry : exacObject.entrySet()) {
							String key = entry.getKey();
							Integer index = headers.get(key);
							String value;
							if ("variant_id".equals(key) ) {
								value = getTransformedVariantId(entry.getValue(), jsonFile.getName());
								variantToExacBlockingQueue.put(value + COMMA + value);
							} else if("synonym_variant_id".equals(key)) {
								value = getTransformedVariantId(entry.getValue(), jsonFile.getName());
							} else {
								value = DOUBLE_QUOTE + entry.getValue().getAsString() + DOUBLE_QUOTE;
							}
							values[index] = value;
						}
						String join = StringUtils.join(values, COMMA);
						exacBlockingQueue.put(join);
					}
				}
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		try {
			exacBlockingQueue.put(POISON_PILL);
			variantToExacBlockingQueue.put(POISON_PILL);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private Map<String, Integer> getHeaders(File[] jsonFiles) {
		Set<String> headers = new LinkedHashSet<>();
		headers.add("variant_id");
		headers.add("synonym_variant_id");
		
		for (File jsonFile : jsonFiles) {
			LOGGER.info("Processing file: {}", jsonFile);
			
			try (LineNumberReader reader = new LineNumberReader(new FileReader(jsonFile));) {
				String line;
				while ((line = reader.readLine()) != null) {
					logLineNumber(reader, 1000);
					JsonObject jsonObject = jsonParser.parse(line).getAsJsonObject();
					JsonElement exacElement = jsonObject.get("EXAC");
					if(!exacElement.isJsonNull()) {
						JsonObject exacObject = exacElement.getAsJsonObject();
						for (Entry<String, JsonElement> c : exacObject.entrySet()) {
							String key = c.getKey();
							headers.add(key);
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		ArrayList<String> list = new ArrayList<String>(headers);
		Map<String, Integer> result = new LinkedHashMap<>();
		for (int i = 0; i < list.size(); i++) {
			result.put(list.get(i), i);
		}
		return result;
	}

	public static void main(String[] args) {
		if (args != null && args.length != 2) {
			throw new RuntimeException("Incorrect args: $1=inputFolder, $2=outputFolder");
		}
		new ExacToVariant(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}
}
