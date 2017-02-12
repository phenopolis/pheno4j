package com.graph.db.file.transcript;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import com.google.common.collect.ImmutableMap;
import com.graph.db.util.ManagedEventBus;

public class TranscriptParserTest {
	
	private TranscriptParser parser;
	private ManagedEventBus eventBusMock;
	private LineNumberReader readerMock;

	@Before
	public void before() {
		parser = new TranscriptParser("fileName");
		
		eventBusMock = PowerMockito.mock(ManagedEventBus.class);
		parser.setEventBus(eventBusMock);
		
		readerMock = PowerMockito.mock(LineNumberReader.class);
	}

	@Test
	public void whenCommentRowIsReadThenItIsSkipped() throws IOException {
		when(readerMock.readLine())
			.thenReturn("##blah")
			.thenReturn(null);

		parser.processDataForFile(readerMock);
		
		//no data is sent to the eventBus
		verify(eventBusMock, times(0)).post(any());
	}
	
	@Test
	public void whenTranscriptRowIsReadThenItIsPosted() throws IOException {
		when(readerMock.readLine())
			.thenReturn("chr1	HAVANA	transcript	11869	14409	.	+	.	gene_id \"ENSG00000223972.5_1\"; transcript_id \"ENST00000456328.2_1\"; gene_type \"transcribed_unprocessed_pseudogene\"; gene_status \"KNOWN\"; gene_name \"DDX11L1\"; transcript_type \"processed_transcript\"; transcript_status \"KNOWN\"; transcript_name \"DDX11L1-002\"; level 2; transcript_support_level 1; tag \"basic\"; havana_gene \"OTTHUMG00000000961.2_1\"; havana_transcript \"OTTHUMT00000362751.1_1\"; remap_num_mappings 1; remap_status \"full_contig\"; remap_target_status \"overlap\";")
			.thenReturn(null);
	
		parser.processDataForFile(readerMock);
		
		final Map<String, String> map = ImmutableMap.<String, String>builder().
			      put("gene_name", "DDX11L1").
			      put("remap_target_status", "overlap").
			      put("transcript_status", "KNOWN").
			      put("remap_status", "full_contig").
			      put("gene_status", "KNOWN").
			      put("level", "2").
			      put("havana_gene", "OTTHUMG00000000961.2_1").
			      put("transcript_name", "DDX11L1-002").
			      put("remap_num_mappings", "1").
			      put("havana_transcript", "OTTHUMT00000362751.1_1").
			      put("transcript_id", "ENST00000456328").
			      put("gene_type", "transcribed_unprocessed_pseudogene").
			      put("tag", "basic").
			      put("transcript_type", "processed_transcript").
			      put("gene_id", "ENSG00000223972").
			      put("transcript_support_level", "1").
			      build();
		
		//1 event is sent to the eventBus
		verify(eventBusMock, times(1)).post(map);
	}
}
