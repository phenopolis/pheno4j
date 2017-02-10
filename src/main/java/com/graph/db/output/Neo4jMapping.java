package com.graph.db.output;

import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public enum Neo4jMapping {
	
    NODE(null),
        GeneticVariant(NODE),
        Gene(NODE),
        Person(NODE),
        Term(NODE),
        TranscriptVariant(NODE),
        Transcript(NODE),
        ConsequenceTerm(NODE),
    RELATIONSHIP(null),
    	TermToParentTerm(RELATIONSHIP),
    	TermToDescendantTerms(RELATIONSHIP),
    	PersonToObservedTerm(RELATIONSHIP),
    	PersonToNonObservedTerm(RELATIONSHIP),
    	GeneticVariantToPerson(RELATIONSHIP),
    	GeneToGeneticVariant(RELATIONSHIP),
    	GeneticVariantToTranscriptVariant(RELATIONSHIP),
    	TranscriptToTranscriptVariant(RELATIONSHIP),
    	TranscriptVariantToConsequenceTerm(RELATIONSHIP),
    	GeneToTerm(RELATIONSHIP),
    	TranscriptToGene(RELATIONSHIP),
    ;
	
	private final Neo4jMapping parent;

	private Neo4jMapping(Neo4jMapping parent) {
		this.parent = parent;
	}
	
	public Neo4jMapping getParent() {
		return parent;
	}
	
	private static final Multimap<Neo4jMapping, Neo4jMapping> map = ArrayListMultimap.create();
	static {
		for (Neo4jMapping mapping : values()) {
			if (mapping.parent != null) {
				map.put(mapping.parent, mapping);
			}
		}
	}
	
	public static Collection<Neo4jMapping> getChildren(Neo4jMapping neo4jMapping) {
		return map.get(neo4jMapping);
	}
}
