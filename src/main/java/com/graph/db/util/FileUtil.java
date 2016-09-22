package com.graph.db.util;

import static com.graph.db.util.Constants.POISON_PILL;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtil {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    
	private FileUtil() {}
	
	public static void writeOutCsvFile(String outputFolder, String fileName, Collection<String> collection) {
		LOGGER.info("Writing out: {}", fileName);
		try {
			FileUtils.writeLines(new File(outputFolder + File.separator + fileName), collection);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File[] getAllJsonFiles(String folder) {
		File dir = new File(folder);
		FilenameFilter filter = (directory, name) -> name.toLowerCase().endsWith(".json");
		return dir.listFiles(filter);
	}
	
	public static void logLineNumber(LineNumberReader reader, int threshold) {
		if ((reader.getLineNumber() % threshold) == 0) {
			LOGGER.info("Processed {} lines", reader.getLineNumber());
		}
	}
	
	/**
	 * Consumer of the queue will terminate when it processes the "poison pill"
	 */
	public static void sendPoisonPillToQueue(BlockingQueue<String> queue) {
		try {
			queue.put(POISON_PILL);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
