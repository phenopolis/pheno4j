package com.graph.db.file.person;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.mockito.PowerMockito;

import com.google.common.collect.ImmutableMap;
import com.graph.db.util.ManagedEventBus;

public class PersonParserTest {

	private PersonParser parser;
	private ManagedEventBus eventBusMock;
	private LineNumberReader readerMock;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() {
		parser = new PersonParser("fileName");
		
		eventBusMock = PowerMockito.mock(ManagedEventBus.class);
		parser.setEventBus(eventBusMock);
		
		readerMock = PowerMockito.mock(LineNumberReader.class);
	}
	
	@Test
	public void whenNoFileIsProvidedThenExceptionIsThrown() {
		thrown.expect(RuntimeException.class);
	    thrown.expectMessage("fileName cannot be empty");
	    
		parser = new PersonParser("");
	}
	
	@Test
	public void whenContentDifferentLengthToHeaderThenRowIsSkipped() throws IOException {
		when(readerMock.readLine())
			.thenReturn("eid,gender,observed_features,non_observed_features,genes,solved")
			.thenReturn("person1")
			.thenReturn(null);
		
		parser.processDataForFile(readerMock);
		
		//no data is sent to the eventBus
		verify(eventBusMock, times(0)).post(any());
	}
	
	@Test
	public void whenCorrectDataIsReadThenItIsPosted() throws IOException {
		when(readerMock.readLine())
			.thenReturn("eid,gender,observed_features,non_observed_features,genes,solved")
			.thenReturn("person1,M,HP:0000479;HP:0007754;HP:0000007;HP:0000505,HP:0000593;HP:0000481;HP:0000359;HP:0000587;HP:0000356;HP:0005306;HP:0000589;HP:0000405;HP:0000953;HP:0000316;HP:0001010;HP:0000601;HP:0000568;HP:0004467;HP:0000384;HP:0000407;HP:0011276,DRAM2,solved")
			.thenReturn(null);
		
		parser.processDataForFile(readerMock);

		final Map<String, Object> map = ImmutableMap.<String, Object>builder().
			      put("eid", "person1").
			      put("gender", "M").
			      put("observed_features", Arrays.asList("HP:0000479", "HP:0007754", "HP:0000007", "HP:0000505")).
			      put("non_observed_features", Arrays.asList("HP:0000593", "HP:0000481", "HP:0000359", "HP:0000587", "HP:0000356", "HP:0005306", "HP:0000589", "HP:0000405", "HP:0000953", "HP:0000316", "HP:0001010", "HP:0000601", "HP:0000568", "HP:0004467", "HP:0000384", "HP:0000407", "HP:0011276")).
			      put("genes", "DRAM2").
			      put("solved", "solved").
			      build();
		
		//1 event is sent to the eventBus
		verify(eventBusMock, times(1)).post(map);
	}

}
