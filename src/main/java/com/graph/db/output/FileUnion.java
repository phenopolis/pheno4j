package com.graph.db.output;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.util.Constants;
import com.graph.db.util.FileUtil;

public class FileUnion {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUnion.class);
	
	private final List<String> SOURCE_FILES = Arrays.asList(
			"Gene-TranscriptParser.csv",
			"Transcript-TranscriptParser.csv");

	private final String inputFolder;
	private final String outputFolder;
	
	public FileUnion(String inputFolder, String outputFolder) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
	}
	
	private void execute() {
		for (String sourceFile : SOURCE_FILES) {
			LOGGER.info("Processing: {}", sourceFile);
			Map<String, String> keyToRow = new HashMap<>();
			
			List<String> sourceFileLines = FileUtil.getLines(inputFolder + File.separator + sourceFile);
			putRowsIntoMap(keyToRow, sourceFileLines);
			
			String fileTag = StringUtils.substringBefore(sourceFile, Constants.HYPHEN);
			List<File> allRemainingFilesForFileTag = getAllRemainingFilesForFileTag(inputFolder, sourceFile, fileTag);
			for (File file : allRemainingFilesForFileTag) {
				LOGGER.info("Processing: {}", file.getName());
				List<String> lines = FileUtil.getLines(file);
				putRowsIntoMap(keyToRow, lines);
			}
			
			writeOutMergedFile(keyToRow, fileTag);
		}
	}

	private List<File> getAllRemainingFilesForFileTag(String folder, String sourceFile, String fileTag) {
		File dir = new File(folder);
		FilenameFilter filter = (directory, name) -> name.startsWith(fileTag + Constants.HYPHEN) && !name.contains("header");
		File[] listFiles = dir.listFiles(filter);
		return removeSourceFile(listFiles, sourceFile);
	}
	
	private List<File> removeSourceFile(File[] listFiles, String sourceFile) {
		List<File> result = new ArrayList<>();
		for (File file : listFiles) {
			if (!file.getName().equals(sourceFile)) {
				result.add(file);
			}
		}
		return result;
	}

	private void putRowsIntoMap(Map<String, String> keyToRow, List<String> sourceFileLines) {
		for (String row : sourceFileLines) {
			String[] cells = StringUtils.split(row, Constants.COMMA);
			keyToRow.putIfAbsent(cells[0], row);
		}
	}
	
	private void writeOutMergedFile(Map<String, String> keyToRow, String fileTag) {
		String pathname = outputFolder + File.separator + fileTag + ".csv";
		LOGGER.info("Writing out file: {}", pathname);
		File outputFile = new File(pathname);
		try {
			FileUtils.writeLines(outputFile, keyToRow.values());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=inputFolder, $2=outputFolder");
		}
		new FileUnion(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}
}
