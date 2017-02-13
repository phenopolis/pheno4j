package com.graph.db.file.annotation.subscriber;

import static com.google.common.base.Throwables.throwIfUnchecked;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.output.GeneticVariantToTranscriptVariantOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.OutputFileType;

public class GeneticVariantToTranscriptVariantSubscriber extends GenericSubscriber<GeneticVariant> {
	
	public GeneticVariantToTranscriptVariantSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT);
	}

	@Override
	public void processRow(GeneticVariant variant) {
		variant.getTranscript_consequences().stream()
			.filter(x -> isNotBlank(x.getHgvsc()))
			.map((x) -> new GeneticVariantToTranscriptVariantOutput(x))
			.forEach(x -> {
				try {
					beanWriter.write(x);
				} catch (IOException e) {
					throwIfUnchecked(e);
				}
			});
	}
}
