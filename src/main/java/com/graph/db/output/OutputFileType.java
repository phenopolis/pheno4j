package com.graph.db.output;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.graph.db.domain.output.ConsequenceTermOutput;
import com.graph.db.domain.output.GeneOutput;
import com.graph.db.domain.output.GeneToGeneticVariantOutput;
import com.graph.db.domain.output.GeneToTermOutput;
import com.graph.db.domain.output.GeneticVariantOutput;
import com.graph.db.domain.output.GeneticVariantToTranscriptVariantOutput;
import com.graph.db.domain.output.TranscriptOutput;
import com.graph.db.domain.output.TranscriptToGeneOutput;
import com.graph.db.domain.output.TranscriptToTranscriptVariantOutput;
import com.graph.db.domain.output.TranscriptVariantOutput;
import com.graph.db.domain.output.TranscriptVariantToConsequenceTermOutput;

public enum OutputFileType {

	GENE_TO_TERM("GeneToTerm", GeneToTermOutput.class),
	GENE_TO_GENETIC_VARIANT("GeneToGeneticVariant", GeneToGeneticVariantOutput.class),
	GENETIC_VARIANT("GeneticVariant", GeneticVariantOutput.class),
	TRANSCRIPT_VARIANT("TranscriptVariant", TranscriptVariantOutput.class),
	GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT("GeneticVariantToTranscriptVariant", GeneticVariantToTranscriptVariantOutput.class),
	TRANSCRIPT_TO_TRANSCRIPT_VARIANT("TranscriptToTranscriptVariant", TranscriptToTranscriptVariantOutput.class),
	CONSEQUENCE_TERM("ConsequenceTerm", ConsequenceTermOutput.class),
	TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM("TranscriptVariantToConsequenceTerm", TranscriptVariantToConsequenceTermOutput.class),
	GENE("Gene", GeneOutput.class),
	TRANSCRIPT("Transcript", TranscriptOutput.class),
	TRANSCRIPT_TO_GENE("TranscriptToGene", TranscriptToGeneOutput.class),
	;
	
	private final String fileTag;
	private final Class<?> beanClass;

	private OutputFileType(String fileTag, Class<?> beanClass) {
		this.fileTag = fileTag;
		this.beanClass = beanClass;
	}
	
	public String getFileTag() {
		return fileTag;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public String[] getHeader() {
		List<String> fields = new ArrayList<>();
		for (Field field : getBeanClass().getDeclaredFields()) {
			fields.add(field.getName());
		}
		return fields.toArray(new String[0]);
	}
}
