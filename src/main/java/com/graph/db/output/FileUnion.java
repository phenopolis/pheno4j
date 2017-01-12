package com.graph.db.output;

import static com.graph.db.util.FileUtil.createUnionedFileName;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
	
	public static final List<OutputFileType> SOURCE_OUTPUTFILETYPES = Arrays.asList(
			OutputFileType.GENE,
			OutputFileType.TRANSCRIPT,
			OutputFileType.PERSON);

	private final String inputFolder;
	private final String outputFolder;
	
	public FileUnion(String inputFolder, String outputFolder) {
		this.inputFolder = inputFolder;
		this.outputFolder = outputFolder;
	}
	
	private void execute() {
		for (OutputFileType sourceFile : SOURCE_OUTPUTFILETYPES) {
			LOGGER.info("Processing: {}", sourceFile);
			Map<String, String> keyToRow = new HashMap<>();
			
			List<File> allFilesForFileTag = getAllFilesForFileTag(inputFolder, sourceFile.getFileTag());
			for (File file : allFilesForFileTag) {
				LOGGER.info("Processing: {}", file.getName());
				List<String> lines = FileUtil.getLines(file);
				putRowsIntoMap(keyToRow, lines);
			}
			
			writeOutMergedFile(keyToRow, sourceFile.getFileTag());
		}
	}

	private List<File> getAllFilesForFileTag(String folder, String fileTag) {
		File dir = new File(folder);
		FilenameFilter filter = (directory, name) -> name.startsWith(fileTag + Constants.HYPHEN) && !name.contains("header");
		File[] listFiles = dir.listFiles(filter);
		return Arrays.asList(listFiles);
	}
	
	private void putRowsIntoMap(Map<String, String> keyToRow, List<String> sourceFileLines) {
		for (String row : sourceFileLines) {
			String[] cells = StringUtils.split(row, Constants.COMMA);
			keyToRow.putIfAbsent(cells[0], row);
		}
	}
	
	private void writeOutMergedFile(Map<String, String> keyToRow, String fileTag) {
		String pathname = createUnionedFileName(outputFolder, fileTag);
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
