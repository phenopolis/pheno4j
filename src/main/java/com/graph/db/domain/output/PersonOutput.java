package com.graph.db.domain.output;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.graph.db.domain.output.annotation.Id;
import com.graph.db.file.person.PersonParser;

public class PersonOutput {
	
	@Id(name = "personId")
	private final String personId;
	
	public PersonOutput(Map<String, Object> map) {
		personId = (String) map.get(PersonParser.PERSON_KEY);
	}
	
	public String getPersonId() {
		return personId;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.personId)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof PersonOutput) == false) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final PersonOutput otherObject = (PersonOutput) obj;
		return new EqualsBuilder()
				.append(this.personId, otherObject.personId)
				.isEquals();
	}
}
