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
import com.graph.db.file.Parser;
import com.graph.db.file.annotation.AnnotationParser;
import com.graph.db.file.gene.GeneParser;
import com.graph.db.file.person.PersonParser;
import com.graph.db.file.term.TermParser;
import com.graph.db.file.transcript.TranscriptParser;
import com.graph.db.file.vcf.VcfParser;

@SuppressWarnings("unchecked")
public enum OutputFileType {

	GENE_TO_TERM(GeneToTermOutput.class, Neo4jMapping.GeneToTerm, GeneParser.class),
	GENE_TO_GENETIC_VARIANT(GeneToGeneticVariantOutput.class, Neo4jMapping.GeneToGeneticVariant, AnnotationParser.class),
	GENETIC_VARIANT(GeneticVariantOutput.class, Neo4jMapping.GeneticVariant, AnnotationParser.class),
	TRANSCRIPT_VARIANT(TranscriptVariantOutput.class, Neo4jMapping.TranscriptVariant, AnnotationParser.class),
	GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT(GeneticVariantToTranscriptVariantOutput.class, Neo4jMapping.GeneticVariantToTranscriptVariant, AnnotationParser.class),
	TRANSCRIPT_TO_TRANSCRIPT_VARIANT(TranscriptToTranscriptVariantOutput.class, Neo4jMapping.TranscriptToTranscriptVariant, AnnotationParser.class),
	CONSEQUENCE_TERM(ConsequenceTermOutput.class, Neo4jMapping.ConsequenceTerm, AnnotationParser.class),
	TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM(TranscriptVariantToConsequenceTermOutput.class, Neo4jMapping.TranscriptVariantToConsequenceTerm, AnnotationParser.class),
	GENE(GeneOutput.class, Neo4jMapping.Gene, AnnotationParser.class, TranscriptParser.class),
	TRANSCRIPT(TranscriptOutput.class, Neo4jMapping.Transcript, AnnotationParser.class, TranscriptParser.class),
	TRANSCRIPT_TO_GENE(TranscriptToGeneOutput.class, Neo4jMapping.TranscriptToGene, TranscriptParser.class),
	TERM(TermOutput.class, Neo4jMapping.Term, TermParser.class),
	TERM_TO_PARENT_TERM(TermToParentTermOutput.class, Neo4jMapping.TermToParentTerm, TermParser.class),
	TERM_TO_DESCENDANT_TERMS(TermToDescendantTermsOutput.class, Neo4jMapping.TermToDescendantTerms, TermParser.class),
	PERSON_TO_OBSERVED_TERM(NotImplementedException.class, Neo4jMapping.PersonToObservedTerm, PersonParser.class),
	PERSON_TO_NON_OBSERVED_TERM(NotImplementedException.class, Neo4jMapping.PersonToNonObservedTerm, PersonParser.class),
	PERSON(NotImplementedException.class, Neo4jMapping.Person, VcfParser.class, PersonParser.class),
	GENETIC_VARIANT_TO_PERSON(NotImplementedException.class, Neo4jMapping.GeneticVariantToPerson, VcfParser.class),
	;
	
	private final Class<?> beanClass;
	private final Neo4jMapping neo4jMapping;
	private final Class<? extends Parser>[] createdBy;
	
	private static final Map<Neo4jMapping, OutputFileType> map = new HashMap<>();
	static {
		for (OutputFileType outputFileType : values()) {
			map.put(outputFileType.getNeo4jMapping(), outputFileType);
		}
	}

	private OutputFileType(Class<?> beanClass, Neo4jMapping neo4jMapping, Class<? extends Parser> ... createdBy) {
		this.beanClass = beanClass;
		this.neo4jMapping = neo4jMapping;
		this.createdBy = createdBy;
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
			if (!field.isSynthetic()) {
				fields.add(field.getName());
			}
		}
		return fields.toArray(new String[0]);
	}
	
	public static OutputFileType toOutputFileType(Neo4jMapping neo4jMapping) {
		return map.get(neo4jMapping);
	}

	public Class<? extends Parser>[] getCreatedBy() {
		return createdBy;
	}
}
