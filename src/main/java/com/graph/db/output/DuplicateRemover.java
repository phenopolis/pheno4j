package com.graph.db.output;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.Parser;
import com.graph.db.util.Constants;

public class DuplicateRemover implements Parser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DuplicateRemover.class);
	
	private final String inputFolder;
	
	private static final List<String> FILE_TAGS = Arrays.asList("GeneSymbol");

	public DuplicateRemover(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	@Override
	public void execute() {
		for (String fileTag : FILE_TAGS) {
			File[] filesForFileTag = getAllFilesForFileTag(inputFolder, fileTag);
			Set<String> rows = new HashSet<>();
			for (File file : filesForFileTag) {
				List<String> lines = readLines(file);
				rows.addAll(lines);
			}
			writeOutFile(fileTag, rows);
		}
	}

	private void writeOutFile(String fileTag, Set<String> rows) {
		try {
			String pathname = inputFolder + File.separator + fileTag + ".csv";
			LOGGER.info("Writing out: {}", pathname);
			FileUtils.writeLines(new File(pathname), rows);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> readLines(File file) {
		try {
			return FileUtils.readLines(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private File[] getAllFilesForFileTag(String folder, String fileTag) {
		String x = fileTag + Constants.HYPHEN;
		File dir = new File(folder);
		FilenameFilter filter = (directory, name) -> (name.startsWith(x)
				&& !name.contains("header"));
		return dir.listFiles(filter);
	}
	
	public static void main(String[] args) {
		if ((args != null) && (args.length != 1)) {
			throw new RuntimeException("Incorrect args: $1=inputFolder");
		}
		new DuplicateRemover(args[0]).execute();
		LOGGER.info("Finished");
	}

}
