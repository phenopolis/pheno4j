package com.graph.db.file.person.subscriber;
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
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.supercsv.io.dozer.CsvDozerBeanWriter;

import com.google.common.collect.ImmutableMap;
import com.graph.db.domain.output.PersonToTermOutput;
import com.graph.db.file.GenericSubscriber;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GenericSubscriber.class)
public class PersonToTermSubscriberTest {
	
	private PersonToTermSubscriber subscriber;
	private CsvDozerBeanWriter beanWriterMock;
	
	@Before
	public void before() {
		suppress(constructor(GenericSubscriber.class));

		subscriber = new PersonToTermSubscriber(null, null, null, "observed_features");
		
		beanWriterMock = mock(CsvDozerBeanWriter.class);
		subscriber.setBeanWriter(beanWriterMock);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void whenCorrectDataIsSuppliedThenItIsWrittenOut() throws IOException {
		final Map<String, Object> map = ImmutableMap.<String, Object>builder().
			      put("eid", "person1").
			      put("gender", "M").
			      put("observed_features", Arrays.asList("HP:0000479", "HP:0007754", "HP:0000007", "HP:0000505")).
			      put("non_observed_features", Arrays.asList("HP:0000593", "HP:0000481", "HP:0000359", "HP:0000587", "HP:0000356", "HP:0005306", "HP:0000589", "HP:0000405", "HP:0000953", "HP:0000316", "HP:0001010", "HP:0000601", "HP:0000568", "HP:0004467", "HP:0000384", "HP:0000407", "HP:0011276")).
			      put("genes", "DRAM2").
			      put("solved", "solved").
			      build();
		
		subscriber.processRow(map);
		
		ArgumentCaptor<PersonToTermOutput> argument = ArgumentCaptor.forClass(PersonToTermOutput.class);
		verify(beanWriterMock, times(4)).write(argument.capture());
		List<PersonToTermOutput> allValues = argument.getAllValues();
		
		assertThat(allValues, hasSize(4));
		
		assertThat(allValues, hasItems(hasProperty("personId", is("person1")), hasProperty("term", is("HP:0000505"))));
		assertThat(allValues, hasItems(hasProperty("personId", is("person1")), hasProperty("term", is("HP:0000007"))));
		assertThat(allValues, hasItems(hasProperty("personId", is("person1")), hasProperty("term", is("HP:0000479"))));
		assertThat(allValues, hasItems(hasProperty("personId", is("person1")), hasProperty("term", is("HP:0007754"))));
	}
}
