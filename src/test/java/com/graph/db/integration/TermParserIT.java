package com.graph.db.integration;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.graph.db.file.term.TermParser;
import com.graph.db.util.FileUtil;

public class TermParserIT {
	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	private File tempFolder;
	
	@Before
	public void before() throws IOException {
		tempFolder = testFolder.newFolder(getClass().getSimpleName());
		
		assertThat(getNumberOfFilesInFolder(tempFolder), equalTo(0));
		
		File file = getTestFile("hp.obo");
		TermParser termParser = new TermParser(file.getAbsolutePath()){
			@Override
			protected String getOutputFolder() { return tempFolder.toString(); };
		};
		termParser.execute();
	}
	
	private File getTestFile(String filename) {
		ClassLoader classLoader = getClass().getClassLoader();
		return new File(classLoader.getResource(filename).getFile());
	}

	private int getNumberOfFilesInFolder(File tempFolder) throws IOException {
		return (int) Files.list(Paths.get(tempFolder.getAbsolutePath())).count();
	}
	
	private void assertRowCountForFile(String fileName, int expectedSize) {
		List<String> lines = FileUtil.getLines(tempFolder + File.separator + fileName);
		assertThat(lines, hasSize(expectedSize));
	}

	@Test
	public void correctNumberOfFilesProduced() throws IOException {
		assertThat(getNumberOfFilesInFolder(tempFolder), equalTo(6));
	}
	
	@Test
	public void correctFileNamesProduced() throws IOException {
		List<String> fileNames = Files.list(Paths.get(tempFolder.getAbsolutePath()))
				.map(x -> x.getFileName().toString())
				.collect(toList());
		
		assertThat(fileNames, hasItem("Term-header.csv"));
		assertThat(fileNames, hasItem("Term-TermParser.csv"));
		assertThat(fileNames, hasItem("TermToParentTerm-header.csv"));
		assertThat(fileNames, hasItem("TermToParentTerm-TermParser.csv"));
		assertThat(fileNames, hasItem("TermToDescendantTerms-header.csv"));
		assertThat(fileNames, hasItem("TermToDescendantTerms-TermParser.csv"));
	}

	@Test
	public void termHeaderHasCorrectNumberOfRows() {
		assertRowCountForFile("Term-header.csv", 1);
	}
	
	@Test
	public void termHasCorrectNumberOfRows() {
		assertRowCountForFile("Term-TermParser.csv", 11785);
	}
	
	@Test
	public void termToParentTermHeaderHasCorrectNumberOfRows() {
		assertRowCountForFile("TermToParentTerm-header.csv", 1);
	}
	
	@Test
	public void termToParentTermHasCorrectNumberOfRows() {
		assertRowCountForFile("TermToParentTerm-TermParser.csv", 15459);
	}
	
	@Test
	public void termToDescendantTermsHeaderHasCorrectNumberOfRows() {
		assertRowCountForFile("TermToDescendantTerms-header.csv", 1);
	}
	
	@Test
	public void termToDescendantTermsHasCorrectNumberOfRows() {
		assertRowCountForFile("TermToDescendantTerms-TermParser.csv", 135948);
	}

}
