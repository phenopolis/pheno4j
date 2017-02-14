package com.graph.db.file.annotation.subscriber;

import static java.util.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Set;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.output.ConsequenceTermOutput;
import com.graph.db.file.SetBasedGenericSubscriber;
import com.graph.db.output.OutputFileType;

public class ConsequenceTermSubscriber extends SetBasedGenericSubscriber<GeneticVariant, ConsequenceTermOutput> {
	
	public ConsequenceTermSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.CONSEQUENCE_TERM);
	}
	
	@Override
	public void processRow(GeneticVariant variant) {
		Set<String> allConsequenceTerms = variant.getTranscript_consequences().stream()
			.map(x -> x.getConsequence_terms())
			.filter(Objects::nonNull)
			.flatMap(l -> l.stream())
			.collect(toSet());
		
		allConsequenceTerms.stream()
			.forEach(x -> set.add(new ConsequenceTermOutput(x)));
	}
}
