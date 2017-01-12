package com.graph.db.file.term;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.graph.db.domain.input.term.RawTerm;

public class TermParserTest {
	
	@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	private TermParser termParser;
	
	@Before
	public void before() throws IOException {
		File tempFolder = testFolder.newFolder(getClass().getSimpleName());
		termParser = new TermParser(null, tempFolder.getAbsolutePath());
	}

	@Test
	public void termProducedFromListOfStrings() {
		List<String> linesForTerm = Arrays.asList("[Term]", "id: HP:0000001", "name: All",
				"comment: Root of all terms in the Human Phenotype Ontology.");
		RawTerm rawTerm = termParser.createRawTermFromLines(linesForTerm);
		
		assertThat(rawTerm.getIsA(), is(empty()));
		assertThat(rawTerm.getName(), is("All"));
		assertThat(rawTerm.getTermId(), is("HP:0000001"));
	}

}
