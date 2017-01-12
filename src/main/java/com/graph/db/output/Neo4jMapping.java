package com.graph.db.output;

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
}
