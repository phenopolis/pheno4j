package com.graph.db.file.annotation.subscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.constructorsDeclaredIn;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.supercsv.io.dozer.CsvDozerBeanWriter;

import com.google.common.collect.Sets;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.GeneticVariantToTranscriptVariantOutput;

@RunWith(PowerMockRunner.class)
//@PrepareForTest({GeneticVariantToTranscriptVariantSubscriber.class})
public class GeneticVariantToTranscriptVariantSubscriberTest {
	
	private GeneticVariantToTranscriptVariantSubscriber subscriber;
	private CsvDozerBeanWriter beanWriterMock;
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() {
		suppress(constructorsDeclaredIn(GeneticVariantToTranscriptVariantSubscriber.class));
		subscriber = new GeneticVariantToTranscriptVariantSubscriber(null, null);
		
		beanWriterMock = mock(CsvDozerBeanWriter.class);
		subscriber.setBeanWriter(beanWriterMock);
	}

	@Test
	public void whenHgvscIsBlankThenRowIsSkipped() throws IOException {
		GeneticVariant variant = new GeneticVariant();
		TranscriptConsequence transcriptConsequence = new TranscriptConsequence();
		Set<TranscriptConsequence> transcript_consequences = Sets.newHashSet(transcriptConsequence);
		variant.setTranscript_consequences(transcript_consequences);
		
		subscriber.processRow(variant);
		
		//no data is sent to the eventBus
		verify(beanWriterMock, times(0)).write(any());
	}
	
	@Test
	public void whenCorrectDataIsSuppliedThenItIsWrittenOut() throws Exception {
		GeneticVariant variant = new GeneticVariant();
		TranscriptConsequence transcriptConsequence = new TranscriptConsequence("variant_id", "hgvsc");
		Set<TranscriptConsequence> transcript_consequences = Sets.newHashSet(transcriptConsequence);
		variant.setTranscript_consequences(transcript_consequences);
		
		subscriber.processRow(variant);
		
		ArgumentCaptor<GeneticVariantToTranscriptVariantOutput> argument = ArgumentCaptor.forClass(GeneticVariantToTranscriptVariantOutput.class);
		verify(beanWriterMock).write(argument.capture());
		assertEquals("variant_id", argument.getValue().getVariant_id());
		assertEquals("hgvsc", argument.getValue().getHgvsc());
	}
}
