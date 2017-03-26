package com.graph.db.file.annotation.subscriber;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.output.GeneToGeneticVariantOutput;
import com.graph.db.file.SetBasedGenericSubscriber;
import com.graph.db.output.OutputFileType;

public class GeneToGeneticVariantSubscriber extends SetBasedGenericSubscriber<GeneticVariant, GeneToGeneticVariantOutput> {
	
	public GeneToGeneticVariantSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.GENE_TO_GENETIC_VARIANT);
	}

	@Override
	public void processRow(GeneticVariant variant) {
		variant.getTranscript_consequences().stream()
			.map(tc -> new GeneToGeneticVariantOutput(tc))
			.forEach(output -> set.add(output));
	}
}
