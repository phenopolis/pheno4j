package com.graph.db.output;

import static com.graph.db.util.FileUtil.createUnionedFileName;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.util.Constants;
import com.graph.db.util.FileUtil;
import com.graph.db.util.PropertiesHolder;

public class FileUnion {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUnion.class);
	
	public static final Set<OutputFileType> SOURCE_OUTPUTFILETYPES = EnumSet.of(
			OutputFileType.GENE,
			OutputFileType.TRANSCRIPT,
			OutputFileType.PERSON);

	private final String inputFolder;
	private final String outputFolder;

	public FileUnion() {
		PropertiesConfiguration config = PropertiesHolder.getInstance();
		this.inputFolder = config.getString("output.folder");
		this.outputFolder = config.getString("output.folder");
	}
	
	public void execute() {
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
		new FileUnion().execute();
		LOGGER.info("Finished");
	}
}
