package com.graph.db.domain.output;

import com.graph.db.domain.output.annotation.RelationshipEnd;
import com.graph.db.domain.output.annotation.RelationshipStart;
import com.graph.db.output.Neo4jMapping;

public class PersonToTermOutput {

	@RelationshipStart(mapping = Neo4jMapping.Person)
	private final String personId;
	
	@RelationshipEnd(mapping = Neo4jMapping.Term)
	private final String term;

	public PersonToTermOutput(String personId, String term) {
		this.personId = personId;
		this.term = term;
	}

	public String getPersonId() {
		return personId;
	}

	public String getTerm() {
		return term;
	}
}
