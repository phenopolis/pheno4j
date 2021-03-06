package com.graph.db.file.annotation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.util.ManagedEventBus;

public class AnnotationParserTest {
	
	private AnnotationParser parser;
	private ManagedEventBus eventBusMock;
	private LineNumberReader readerMock;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() {
		parser = new AnnotationParser("fileName");
		
		eventBusMock = PowerMockito.mock(ManagedEventBus.class);
		parser.setEventBus(eventBusMock);
		
		readerMock = PowerMockito.mock(LineNumberReader.class);
	}

	@Test
	public void whenNoFileNameIsProvidedThenExceptionIsThrown() {
		thrown.expect(RuntimeException.class);
	    thrown.expectMessage("fileName cannot be empty");
	    
		parser = new AnnotationParser("");
	}
	
	@Test
	public void whenTranscriptConsequenceNullThenSetToEmptySet() throws IOException {
		when(readerMock.readLine())
			.thenReturn("{\"transcript_consequences\":[]}")
			.thenReturn(null);
	
		parser.processDataForFile(readerMock);
		
		ArgumentCaptor<GeneticVariant> argument = ArgumentCaptor.forClass(GeneticVariant.class);
		verify(eventBusMock).post(argument.capture());
		assertEquals(Collections.emptySet(), argument.getValue().getTranscript_consequences());
	}
	
	@Test
	public void whenTranscriptConsequencesLoadedThenVariantIdIsPropagatedToThem() throws IOException {
		when(readerMock.readLine())
			.thenReturn("{\"variant_id\":\"22-16157603-G-C\", \"transcript_consequences\":[{\"transcript_id\":\"ENST00000383038\"}]}")
			.thenReturn(null);
	
		parser.processDataForFile(readerMock);
		
		ArgumentCaptor<GeneticVariant> argument = ArgumentCaptor.forClass(GeneticVariant.class);
		verify(eventBusMock).post(argument.capture());
		assertEquals("22-16157603-G-C", argument.getValue().getTranscript_consequences().iterator().next().getVariant_id());
	}
}
