package com.graph.db.util;

import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.Constants.HYPHEN;
import static com.graph.db.util.Constants.UNDERSCORE;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

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
		FilenameFilter filter = (directory, name) -> !directory.isDirectory() && name.toLowerCase().endsWith(".json");
		return dir.listFiles(filter);
	}
	
	public static void logLineNumber(LineNumberReader reader, int threshold) {
		if (reader.getLineNumber() % threshold == 0) {
			LOGGER.info("Processed {} lines", reader.getLineNumber());
		}
	}
	
	public static String getTransformedVariantId(JsonElement jsonElement, String fileName) {
		String variantIdWithHyphens = jsonElement.getAsString();
		int matches = StringUtils.countMatches(variantIdWithHyphens, HYPHEN);
		if (matches != 3) {
			throw new RuntimeException(variantIdWithHyphens + " does not have 3 hyphens in file: " + fileName);
		}
		return DOUBLE_QUOTE + StringUtils.replace(variantIdWithHyphens, HYPHEN, UNDERSCORE) + DOUBLE_QUOTE;
	}

}
