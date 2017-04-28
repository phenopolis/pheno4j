package com.graph.db.file.vcf;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.Constants.HYPHEN;
import static com.graph.db.util.Constants.TAB;
import static com.graph.db.util.FileUtil.createFolderIfNotPresent;
import static com.graph.db.util.FileUtil.getLineNumberReaderForFile;
import static com.graph.db.util.FileUtil.logLineNumber;
import static com.graph.db.util.FileUtil.sendPoisonPillToQueue;
import static com.graph.db.util.FileUtil.writeOutCsvFile;
import static com.graph.db.util.FileUtil.writeOutCsvHeader;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.file.Parser;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.PropertiesHolder;

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
 * - HetVariantToPerson
 * - HomVariantToPerson
 */
public class VcfParser implements Parser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VcfParser.class);
	
	private static final Configuration config = PropertiesHolder.getInstance();
	
	private final BlockingQueue<String> hetVariantToPersonBlockingQueue = new ArrayBlockingQueue<>(1024);
	private final BlockingQueue<String> homVariantToPersonBlockingQueue = new ArrayBlockingQueue<>(1024);
	private final Map<Integer, String> indexToPerson = new HashMap<>();

	private final String outputFolder;
	private final String fileName;
	
	public VcfParser() {
		this(config.getString("vcfParser.input.fileName"), config.getString("output.folder"));
	}
	
	public VcfParser(String fileName, String outputFolder) {
		this.fileName = fileName;
		this.outputFolder = outputFolder;
		
		createFolderIfNotPresent(outputFolder);
		if (StringUtils.isBlank(fileName)) {
			throw new RuntimeException("fileName cannot be empty");
		}
	}
	
	@Override
	public Collection<File> getInputFiles() {
		throw new NotImplementedException();
	}
	
	@Override
	public void execute() {
		try (LineNumberReader reader = getLineNumberReaderForFile(new File(fileName))) {
			boolean found = false;
			int personStartColumn = Integer.MAX_VALUE;
			
			Runnable hetVariantToPersonBlockingQueueConsumer = new QueueToFileConsumer(OutputFileType.HET_VARIANT_TO_PERSON, hetVariantToPersonBlockingQueue, outputFolder, getClass());
			Runnable homVariantToPersonBlockingQueueConsumer = new QueueToFileConsumer(OutputFileType.HOM_VARIANT_TO_PERSON, homVariantToPersonBlockingQueue, outputFolder, getClass());
			new Thread(hetVariantToPersonBlockingQueueConsumer).start();
			new Thread(homVariantToPersonBlockingQueueConsumer).start();
	        
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
					
					if (doesNotContainComma(altField) && isNotStar(altField)) {
						String variantId = DOUBLE_QUOTE + chrom + HYPHEN + pos + HYPHEN + ref + HYPHEN + altField + DOUBLE_QUOTE;
						
						RecursiveAction task = new RowAction(variantId, split, personStartColumn, split.length);
						forkJoinPool.invoke(task);
					}
				}
			}
			sendPoisonPillToQueue(hetVariantToPersonBlockingQueue);
			sendPoisonPillToQueue(homVariantToPersonBlockingQueue);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		writeOutHeaders();
	}

	private boolean doesNotContainComma(String alt) {
		return !StringUtils.contains(alt, COMMA);
	}

	private boolean isNotStar(String alt) {
		return !"*".equals(alt);
	}
	
	private void writeOutHeaders() {
		writeOutCsvHeader(outputFolder, "HetVariantToPerson", Arrays.asList(":START_ID(GeneticVariant),:END_ID(Person)"));
		writeOutCsvHeader(outputFolder, "HomVariantToPerson", Arrays.asList(":START_ID(GeneticVariant),:END_ID(Person)"));
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
					boolean isHom = "1/1".equals(substringBeforeFirstColon);
					boolean isHet = "0/1".equals(substringBeforeFirstColon);
					
					if (isHet) {
						sendDataToQueue(hetVariantToPersonBlockingQueue, i);
					} else if (isHom) {
						sendDataToQueue(homVariantToPersonBlockingQueue, i);
					}
				}
			}
		}

		private void sendDataToQueue(BlockingQueue<String> queue, int i) {
			try {
				queue.put(variantId + COMMA + indexToPerson.get(i));
			} catch (InterruptedException e) {
				LOGGER.error("Interrupted: ", e);
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public static void main(String[] args) {
		new VcfParser().execute();
		LOGGER.info("Finished");
	}
}
