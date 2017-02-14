package com.graph.db.file.term.subscriber;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.supercsv.io.dozer.CsvDozerBeanWriter;

import com.graph.db.domain.input.term.RawTerm;
import com.graph.db.domain.output.TermToDescendantTermsOutput;
import com.graph.db.file.GenericSubscriber;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GenericSubscriber.class)
public class TermToDescendantTermsSubscriberTest {
	
	private TermToDescendantTermsSubscriber subscriber;
	private CsvDozerBeanWriter beanWriterMock;
	
	@Before
	public void before() {
		suppress(constructor(GenericSubscriber.class));
		subscriber = new TermToDescendantTermsSubscriber(null, null);
		
		beanWriterMock = mock(CsvDozerBeanWriter.class);
		subscriber.setBeanWriter(beanWriterMock);
	}
	
	@Test
	public void whenProcessRowIsCalledThenDataIsAddedToTheSet() {
		RawTerm rawTerm = new RawTerm("A", "name", Arrays.asList("B", "C"));
		
		subscriber.processRow(rawTerm);
		
		Set<RawTerm> set = subscriber.getSet();
		assertThat(set, hasSize(1));
	}
	
	@Test
	public void whenProcessRowIsCalledWithDuplicateThenDuplicateDataIsNotAddedToTheSet() {
		RawTerm rawTerm1 = new RawTerm("A", "name", Arrays.asList("B", "C"));
		subscriber.processRow(rawTerm1);
		Set<RawTerm> set = subscriber.getSet();
		assertThat(set, hasSize(1));
		
		RawTerm rawTerm2 = new RawTerm("A", "name", Arrays.asList("B", "C"));
		subscriber.processRow(rawTerm2);
		set = subscriber.getSet();
		assertThat(set, hasSize(1));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void whenPreCloseIsCalledThenDescendantsAreCalculatedAndWrittenOut() throws IOException {
		subscriber.processRow(new RawTerm("A", "name", Arrays.asList("B", "C")));
		subscriber.processRow(new RawTerm("B", "name", Arrays.asList("D", "E")));
		subscriber.processRow(new RawTerm("C", "name", Arrays.asList("F", "G")));
		subscriber.processRow(new RawTerm("D", "name", null));
		subscriber.processRow(new RawTerm("E", "name", null));
		subscriber.processRow(new RawTerm("F", "name", null));
		subscriber.processRow(new RawTerm("G", "name", null));
		
		assertThat(subscriber.getSet(), hasSize(7));
		
		subscriber.preClose();
		
		ArgumentCaptor<TermToDescendantTermsOutput> argument = ArgumentCaptor.forClass(TermToDescendantTermsOutput.class);
		verify(beanWriterMock, times(17)).write(argument.capture());
		List<TermToDescendantTermsOutput> allValues = argument.getAllValues();
		
		assertThat(allValues, hasSize(17));
		
		assertThat(allValues, hasItems(hasProperty("parent", is("A")), hasProperty("child", is("A"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("A")), hasProperty("child", is("B"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("A")), hasProperty("child", is("C"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("A")), hasProperty("child", is("D"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("A")), hasProperty("child", is("E"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("A")), hasProperty("child", is("F"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("A")), hasProperty("child", is("G"))));
		
		assertThat(allValues, hasItems(hasProperty("parent", is("B")), hasProperty("child", is("B"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("B")), hasProperty("child", is("D"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("B")), hasProperty("child", is("E"))));
		
		assertThat(allValues, hasItems(hasProperty("parent", is("C")), hasProperty("child", is("C"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("C")), hasProperty("child", is("F"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("C")), hasProperty("child", is("G"))));
		
		assertThat(allValues, hasItems(hasProperty("parent", is("D")), hasProperty("child", is("D"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("E")), hasProperty("child", is("E"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("F")), hasProperty("child", is("F"))));
		assertThat(allValues, hasItems(hasProperty("parent", is("G")), hasProperty("child", is("G"))));
	}
}
