package com.graph.db.util;

import static com.graph.db.util.Constants.POISON_PILL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;
import com.graph.db.output.OutputFileType;

public final class FileUtil {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    
	private FileUtil() {}
	
	public static void writeOutCsvHeader(String outputFolder, String fileTag, Collection<String> collection) {
		String pathname = createHeaderFileName(outputFolder, fileTag);
		writeFile(collection, pathname);
	}

	public static void writeOutCsvFile(String outputFolder, Class<?> c, String fileName, Collection<String> collection) {
		String pathname = outputFolder + File.separator + fileName + Constants.HYPHEN + c.getSimpleName() + ".csv";
		writeFile(collection, pathname);
	}
	
	private static void writeFile(Collection<String> collection, String pathname) {
		LOGGER.info("Writing out: {}", pathname);
		try {
			FileUtils.writeLines(new File(pathname), collection);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File[] getAllJsonFiles(String folder) {
		File dir = new File(folder);
		FilenameFilter filter = (directory, name) -> name.toLowerCase().contains(".json");
		File[] files = dir.listFiles(filter);
		if (ArrayUtils.isEmpty(files)) {
			throw new RuntimeException("No files in folder: " + folder);
		}
		return files;
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
	
	public static List<String> getLines(String fileName) {
		return getLines(new File(fileName));
	}
	
	public static List<String> getLines(File file) {
		try {
			return FileUtils.readLines(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static LineNumberReader getLineNumberReaderForFile(String fileName) {
		return getLineNumberReaderForFile(new File(fileName));
	}
	
	public static LineNumberReader getLineNumberReaderForFile(File file) {
		if(isGzipFile(file)) {
			return createLineNumberReaderForGzipFile(file);
		} else {
			try {
				File f;
				if (!file.isFile()) {
					URL resource = Resources.getResource(file.toString());
					f = new File(resource.getFile());
				} else {
					f = file;
				}
				return new LineNumberReader(new FileReader(f));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private static boolean isGzipFile(File fileName) {
		return StringUtils.endsWith(fileName.getName(), "gz");
	}

	private static LineNumberReader createLineNumberReaderForGzipFile(File file) {
		InputStream gzipStream;
		try {
			final InputStream fileStream;
			if (file.isFile()) {
				fileStream = new FileInputStream(file);
			} else {
				URL resource = FileUtil.class.getResource(file.getName());
				fileStream = resource.openStream();
			}
			gzipStream = new GZIPInputStream(fileStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Reader decoder = new InputStreamReader(gzipStream);
		return new LineNumberReader(decoder);
	}
	
	public static String createHeaderFileName(String outputFolder, String fileTag) {
		return outputFolder + File.separator + fileTag + "-header.csv";
	}
	
	public static String createFileName(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		return outputFolder + File.separator + outputFileType.getFileTag() + Constants.HYPHEN + parserClass.getSimpleName() + ".csv";
	}
	
	public static String createUnionedFileName(String outputFolder, String fileTag) {
		return outputFolder + File.separator + fileTag + ".csv";
	}
	
	public static void createFolderIfNotPresent(String folder) {
		Path path = Paths.get(folder);
		if (!Files.isDirectory(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
