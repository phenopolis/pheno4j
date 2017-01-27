package com.graph.db.file.vcf;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.Constants.HYPHEN;
import static com.graph.db.util.Constants.TAB;
import static com.graph.db.util.FileUtil.getLineNumberReaderForFile;
import static com.graph.db.util.FileUtil.logLineNumber;
import static com.graph.db.util.FileUtil.sendPoisonPillToQueue;
import static com.graph.db.util.FileUtil.writeOutCsvFile;
import static com.graph.db.util.FileUtil.writeOutCsvHeader;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.file.LegacyParser;
import com.graph.db.output.OutputFileType;

/**
 * Writes data to the file as it is available
 * Reads the compressed file
 * Processes each row in a fork join pool
 */
/**
 * Nodes
 * - Person
 * 
 * Relationships
 * - GeneticVariantToPerson
 */
public class VcfParser extends LegacyParser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VcfParser.class);
	
	private final BlockingQueue<String> geneticVariantToPersonBlockingQueue = new ArrayBlockingQueue<>(1024);
	private final Map<Integer, String> indexToPerson = new HashMap<>();

	private final String fileName;
	
	public VcfParser() {
		this.fileName = config.getString("vcfParser.input.fileName");
		if (StringUtils.isBlank(fileName)) {
			throw new RuntimeException("fileName cannot be empty");
		}
	}
	
	public VcfParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
	}
	
	@Override
	public void execute() {
		try (LineNumberReader reader = getLineNumberReaderForFile(fileName)) {
			boolean found = false;
			int personStartColumn = Integer.MAX_VALUE;
			
			String fileTag = StringUtils.substringBefore(new File(fileName).getName(), ".");
			
	        Runnable geneticVariantToPersonBlockingQueueConsumer = new QueueToFileConsumer(geneticVariantToPersonBlockingQueue, outputFolder, "GeneticVariantToPerson-" + fileTag + ".csv");
	        new Thread(geneticVariantToPersonBlockingQueueConsumer).start();
	        
	        ForkJoinPool forkJoinPool = new ForkJoinPool();
	        
	        String line;
			while ((line = reader.readLine()) != null) {
				logLineNumber(reader, 10000);
				if ((found == false) && line.startsWith("#CHROM")) {
					String[] split = line.split(TAB);
					for (int i = 0; i < split.length; i++) {
						String string = split[i];
						
						if (string.equals("FORMAT")) {
							personStartColumn = i + 1;
						}
						if (i >= personStartColumn) {
							indexToPerson.put(i, string);
						}
					}
					LOGGER.info("Found header at line: {}", reader.getLineNumber());
					writeOutCsvFile(outputFolder, getClass(),  "Person", indexToPerson.values());
					found = true;
					continue;
				}
				
				if (found) {
					String[] split = line.split(TAB);
					
					String chrom = split[0];
					String pos = split[1];
					String ref = split[3];
					String altField = split[4];
					
					String[] alts = altField.split(COMMA);
					for (String alt : alts) {
						if (isNotStar(alt)) {
							String variantId = DOUBLE_QUOTE + chrom + HYPHEN + pos + HYPHEN + ref + HYPHEN + alt + DOUBLE_QUOTE;
							
							RecursiveAction task = new RowAction(variantId, split, personStartColumn, split.length);
							forkJoinPool.invoke(task);
						}
					}
				}
			}
			sendPoisonPillToQueue(geneticVariantToPersonBlockingQueue);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		writeOutHeaders();
	}

	private boolean isNotStar(String alt) {
		return !"*".equals(alt);
	}
	
	private void writeOutHeaders() {
		writeOutCsvHeader(outputFolder, "Person", Arrays.asList("personId:ID(Person)"));
		writeOutCsvHeader(outputFolder, "GeneticVariantToPerson", Arrays.asList(":START_ID(GeneticVariant),:END_ID(Person)"));
	}

	private class RowAction extends RecursiveAction {
	    private static final long serialVersionUID = 1L;
	    private final String variantId;
	    private final String[] array;
	    private final int low;
	    private final int high;
	    
	    final int splitSize = 512;
	    
	    public RowAction(String variantId, String[] array, int low, int high) {
	        this.variantId = variantId;
			this.low = low;
	        this.high = high;
	        this.array = array;
	    }

		@Override
		protected void compute() {
			if ((high - low) > splitSize) {
				int mid = (low + high) >>> 1;
				invokeAll(Arrays.asList(new RowAction(variantId, array, low, mid), new RowAction(variantId, array, mid, high)));
			} else {
				for (int i = low; i < high; i++) {
					String cell = array[i];
					String substringBeforeFirstColon = StringUtils.substringBefore(cell, ":");
					
					if ("0/1".equals(substringBeforeFirstColon) || "1/1".equals(substringBeforeFirstColon)) {
						try {
							geneticVariantToPersonBlockingQueue.put(variantId + COMMA + indexToPerson.get(i));
						} catch (InterruptedException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}
	}
	
	@Override
	public EnumSet<OutputFileType> getNonHeaderOutputFileTypes() {
		return EnumSet.of(OutputFileType.PERSON, OutputFileType.GENETIC_VARIANT_TO_PERSON);
	}
	
	public static void main(String[] args) {
		new VcfParser().execute();
		LOGGER.info("Finished");
	}

}
