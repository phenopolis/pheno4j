package com.graph.db.file.annotation.subscriber;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.mock;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.supercsv.io.dozer.CsvDozerBeanWriter;

import com.google.common.collect.Sets;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.GeneToGeneticVariantOutput;
import com.graph.db.file.GenericSubscriber;

@PrepareForTest(GenericSubscriber.class)
@RunWith(PowerMockRunner.class)
public class GeneToGeneticVariantSubscriberTest {
	
	private GeneToGeneticVariantSubscriber subscriber;
	private CsvDozerBeanWriter beanWriterMock;
	
	@Before
	public void before() {
		suppress(constructor(GenericSubscriber.class));
		subscriber = new GeneToGeneticVariantSubscriber(null, null);
		
		beanWriterMock = mock(CsvDozerBeanWriter.class);
		subscriber.setBeanWriter(beanWriterMock);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenCorrectDataIsSuppliedThenItIsWrittenOut() throws IOException {
		GeneticVariant variant = new GeneticVariant();
		TranscriptConsequence transcriptConsequence = new TranscriptConsequence("variant_id", "gene_id", "hgvsc", null);
		Set<TranscriptConsequence> transcript_consequences = Sets.newHashSet(transcriptConsequence);
		variant.setTranscript_consequences(transcript_consequences);
		
		subscriber.processRow(variant);
		
		Set<GeneToGeneticVariantOutput> set = subscriber.getSet();
		assertThat(set, hasSize(1));
		assertThat(set, hasItems(hasProperty("gene_id", is("gene_id")), hasProperty("variant_id", is("variant_id"))));
	}
}
