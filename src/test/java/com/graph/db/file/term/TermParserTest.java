package com.graph.db.file.term;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;

import com.graph.db.domain.input.term.RawTerm;
import com.graph.db.util.ManagedEventBus;

public class TermParserTest {
	
	private TermParser parser;
	private ManagedEventBus eventBusMock;
	private LineNumberReader readerMock;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() {
		parser = new TermParser("fileName");
		
		eventBusMock = PowerMockito.mock(ManagedEventBus.class);
		parser.setEventBus(eventBusMock);
		
		readerMock = PowerMockito.mock(LineNumberReader.class);
	}
	
	@Test
	public void whenNoFileIsProvidedThenExceptionIsThrown() {
		thrown.expect(RuntimeException.class);
	    thrown.expectMessage("fileName cannot be empty");
	    
		parser = new TermParser("");
	}
	
	@Test
	public void whenTermBlockIsReadThenItIsPosted() throws IOException {
		when(readerMock.readLine())
			.thenReturn("[Term]")
			.thenReturn("id: HP:0000005")
			.thenReturn("name: Mode of inheritance")
			.thenReturn("alt_id: HP:0001453")
			.thenReturn("synonym: \"Inheritance\" EXACT []")
			.thenReturn("is_a: HP:0000001 ! All")
			.thenReturn(null);
		
		parser.processDataForFile(readerMock);
		
		ArgumentCaptor<RawTerm> argument = ArgumentCaptor.forClass(RawTerm.class);
		verify(eventBusMock).post(argument.capture());
		assertEquals(Arrays.asList("HP:0000001"), argument.getValue().getIsA());
		assertEquals("Mode of inheritance", argument.getValue().getName());
		assertEquals("HP:0000005", argument.getValue().getTermId());
	}
}
