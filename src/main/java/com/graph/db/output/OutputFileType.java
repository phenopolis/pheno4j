package com.graph.db.output;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

import com.graph.db.domain.output.ConsequenceTermOutput;
import com.graph.db.domain.output.GeneOutput;
import com.graph.db.domain.output.GeneToGeneticVariantOutput;
import com.graph.db.domain.output.GeneToTermOutput;
import com.graph.db.domain.output.GeneticVariantOutput;
import com.graph.db.domain.output.GeneticVariantToTranscriptVariantOutput;
import com.graph.db.domain.output.TermOutput;
import com.graph.db.domain.output.TermToDescendantTermsOutput;
import com.graph.db.domain.output.TermToParentTermOutput;
import com.graph.db.domain.output.TranscriptOutput;
import com.graph.db.domain.output.TranscriptToGeneOutput;
import com.graph.db.domain.output.TranscriptToTranscriptVariantOutput;
import com.graph.db.domain.output.TranscriptVariantOutput;
import com.graph.db.domain.output.TranscriptVariantToConsequenceTermOutput;

public enum OutputFileType {

	GENE_TO_TERM(GeneToTermOutput.class, Neo4jMapping.GeneToTerm),
	GENE_TO_GENETIC_VARIANT(GeneToGeneticVariantOutput.class, Neo4jMapping.GeneToGeneticVariant),
	GENETIC_VARIANT(GeneticVariantOutput.class, Neo4jMapping.GeneticVariant),
	TRANSCRIPT_VARIANT(TranscriptVariantOutput.class, Neo4jMapping.TranscriptVariant),
	GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT(GeneticVariantToTranscriptVariantOutput.class, Neo4jMapping.GeneticVariantToTranscriptVariant),
	TRANSCRIPT_TO_TRANSCRIPT_VARIANT(TranscriptToTranscriptVariantOutput.class, Neo4jMapping.TranscriptToTranscriptVariant),
	CONSEQUENCE_TERM(ConsequenceTermOutput.class, Neo4jMapping.ConsequenceTerm),
	TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM(TranscriptVariantToConsequenceTermOutput.class, Neo4jMapping.TranscriptVariantToConsequenceTerm),
	GENE(GeneOutput.class, Neo4jMapping.Gene),
	TRANSCRIPT(TranscriptOutput.class, Neo4jMapping.Transcript),
	TRANSCRIPT_TO_GENE(TranscriptToGeneOutput.class, Neo4jMapping.TranscriptToGene),
	TERM(TermOutput.class, Neo4jMapping.Term),
	TERM_TO_PARENT_TERM(TermToParentTermOutput.class, Neo4jMapping.TermToParentTerm),
	TERM_TO_DESCENDANT_TERMS(TermToDescendantTermsOutput.class, Neo4jMapping.TermToDescendantTerms),
	PERSON_TO_OBSERVED_TERM(NotImplementedException.class, Neo4jMapping.PersonToObservedTerm),
	PERSON_TO_NON_OBSERVED_TERM(NotImplementedException.class, Neo4jMapping.PersonToNonObservedTerm),
	PERSON(NotImplementedException.class, Neo4jMapping.Person),
	GENETIC_VARIANT_TO_PERSON(NotImplementedException.class, Neo4jMapping.GeneticVariantToPerson),
	;
	
	private final Class<?> beanClass;
	private final Neo4jMapping neo4jMapping;
	
	private static final Map<Neo4jMapping, OutputFileType> map = new HashMap<>();
	static {
		for (OutputFileType outputFileType : values()) {
			map.put(outputFileType.getNeo4jMapping(), outputFileType);
		}
	}

	private OutputFileType(Class<?> beanClass, Neo4jMapping neo4jMapping) {
		this.beanClass = beanClass;
		this.neo4jMapping = neo4jMapping;
	}
	
	public String getFileTag() {
		return neo4jMapping.name();
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public Neo4jMapping getNeo4jMapping() {
		return neo4jMapping;
	}

	public String[] getHeader() {
		List<String> fields = new ArrayList<>();
		for (Field field : getBeanClass().getDeclaredFields()) {
			fields.add(field.getName());
		}
		return fields.toArray(new String[0]);
	}
	
	public static OutputFileType toOutputFileType(Neo4jMapping neo4jMapping) {
		return map.get(neo4jMapping);
	}
}
