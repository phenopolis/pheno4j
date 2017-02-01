package com.graph.db.domain.output.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.graph.db.output.Neo4jMapping;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RelationshipStart {
	
	public Neo4jMapping mapping();

}
