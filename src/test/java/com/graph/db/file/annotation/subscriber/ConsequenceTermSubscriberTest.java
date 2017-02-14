package com.graph.db.file.annotation.subscriber;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.io.IOException;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Sets;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.ConsequenceTermOutput;
import com.graph.db.file.GenericSubscriber;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GenericSubscriber.class)
public class ConsequenceTermSubscriberTest {
	
	private ConsequenceTermSubscriber subscriber;
	
	@Before
	public void before() {
		suppress(constructor(GenericSubscriber.class));
		subscriber = new ConsequenceTermSubscriber(null, null);
	}
	
	@Test
	public void whenConsequenceTermsIsNullThenNoExceptionIsThrown() throws IOException {
		GeneticVariant variant = new GeneticVariant();
		TranscriptConsequence transcriptConsequence = new TranscriptConsequence.TranscriptConsequenceBuilder()
				.variantId("variant_id")
				.hgvsc("hgvsc")
				.build();
		Set<TranscriptConsequence> transcript_consequences = Sets.newHashSet(transcriptConsequence);
		variant.setTranscript_consequences(transcript_consequences);
		
		subscriber.processRow(variant);
		
		Set<ConsequenceTermOutput> set = subscriber.getSet();
		assertThat(set, is(empty()));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenCorrectDataIsSuppliedThenItIsWrittenOut() throws Exception {
		GeneticVariant variant = new GeneticVariant();
		TranscriptConsequence transcriptConsequence = new TranscriptConsequence.TranscriptConsequenceBuilder()
				.variantId("variant_id")
				.hgvsc("hgvsc")
				.addConsequenceTerm("consequenceTerm1")
				.addConsequenceTerm("consequenceTerm2")
				.build();
		Set<TranscriptConsequence> transcript_consequences = Sets.newHashSet(transcriptConsequence);
		variant.setTranscript_consequences(transcript_consequences);
		
		subscriber.processRow(variant);
		
		Set<ConsequenceTermOutput> set = subscriber.getSet();
		assertThat(set, hasSize(2));
		assertThat(set, hasItems(hasProperty("consequenceTerm", is("consequenceTerm1"))));
		assertThat(set, hasItems(hasProperty("consequenceTerm", is("consequenceTerm2"))));
		
	}
}
