package com.graph.db.file.term;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.graph.db.domain.input.term.RawTerm;

public class TermParserTest {
	
	private final TermParser termParser = new TermParser(null, null);

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
