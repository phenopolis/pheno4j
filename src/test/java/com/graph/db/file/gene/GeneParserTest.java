package com.graph.db.file.gene;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.LineNumberReader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;

import com.graph.db.domain.output.GeneToTermOutput;
import com.graph.db.util.ManagedEventBus;

public class GeneParserTest {

	private GeneParser parser;
	private ManagedEventBus eventBusMock;
	private LineNumberReader readerMock;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() {
		parser = new GeneParser("fileName");
		
		eventBusMock = PowerMockito.mock(ManagedEventBus.class);
		parser.setEventBus(eventBusMock);
		
		readerMock = PowerMockito.mock(LineNumberReader.class);
	}
	
	@Test
	public void whenNoFileIsProvidedThenExceptionIsThrown() {
		thrown.expect(RuntimeException.class);
	    thrown.expectMessage("fileName cannot be empty");
	    
		parser = new GeneParser("");
	}
	
	@Test
	public void whenContentDifferentLengthToHeaderThenRowIsSkipped() throws IOException {
		when(readerMock.readLine())
			.thenReturn("diseaseId	gene-symbol	gene-id(entrez)	HPO-ID	HPO-term-name	gene_id")
			.thenReturn("OMIM:614652")
			.thenReturn(null);
		
		parser.processDataForFile(readerMock);
		
		//no data is sent to the eventBus
		verify(eventBusMock, times(0)).post(any());
	}
	
	@Test
	public void whenCorrectDataIsReadThenItIsPosted() throws IOException {
		when(readerMock.readLine())
			.thenReturn("diseaseId	gene-symbol	gene-id(entrez)	HPO-ID	HPO-term-name	gene_id")
			.thenReturn("OMIM:614652	PDSS2	57107	HP:0002133	Status epilepticus	ENSG00000164494")
			.thenReturn(null);
		
		parser.processDataForFile(readerMock);
		
		//1 event is sent to the eventBus
		verify(eventBusMock, times(1)).post(any());
		
		ArgumentCaptor<GeneToTermOutput> argument = ArgumentCaptor.forClass(GeneToTermOutput.class);
		verify(eventBusMock).post(argument.capture());
		assertEquals("ENSG00000164494", argument.getValue().getGene_id());
		assertEquals("HP:0002133", argument.getValue().getTermId());
	}
}
