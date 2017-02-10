package com.graph.db;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.domain.output.annotation.Id;
import com.graph.db.domain.output.annotation.Index;
import com.graph.db.output.Neo4jMapping;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.PropertiesHolder;

public class DatabaseIndexCreator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIndexCreator.class);
	
	public void execute() {
		PropertiesConfiguration config = PropertiesHolder.getInstance();
        String pathname = config.getString("output.folder") + File.separator + "graph-db" + File.separator + "data" + File.separator +"databases" + File.separator + "graph.db";
		File storeDir = new File(pathname);
        
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(storeDir);
        
        LOGGER.info("Creating constraints...");
		try (Transaction tx = graphDb.beginTx()) {
			Schema schema = graphDb.schema();
			Iterable<ConstraintDefinition> constraints = schema.getConstraints();
			
	        for (Neo4jMapping node : Neo4jMapping.getChildren(Neo4jMapping.NODE)) {
	        	OutputFileType outputFileType = OutputFileType.toOutputFileType(node);
        		Set<Field> fieldsForAnnotation = getFieldForAnnotation(outputFileType, Id.class);
        		if (fieldsForAnnotation.size() == 1) {
        			Id annotation = fieldsForAnnotation.iterator().next().getAnnotation(Id.class);
    				if(!doesConstraintExist(constraints, node.name())) {
    					LOGGER.info("Creating constraint for Type: {}", node);
    					schema.constraintFor(Label.label(node.name()))
        					.assertPropertyIsUnique(annotation.name())
        					.create();
    				} else {
    					LOGGER.warn("Constraint already exists for Type: {}", node);
    				}
        		}
	        }
			tx.success();
        }
		
		LOGGER.info("Creating indexes...");
		try (Transaction tx = graphDb.beginTx()) {
			Schema schema = graphDb.schema();
			Iterable<IndexDefinition> indexes = schema.getIndexes();
			
	        for (Neo4jMapping node : Neo4jMapping.getChildren(Neo4jMapping.NODE)) {
	        	OutputFileType outputFileType = OutputFileType.toOutputFileType(node);
        		Set<Field> fieldsForAnnotation = getFieldForAnnotation(outputFileType, Index.class);
        		for (Field field : fieldsForAnnotation) {
    				if(!doesIndexExist(indexes, node.name(), field.getName())) {
    					LOGGER.info("Creating index for Field: {}", field);
    					
    					schema.indexFor(Label.label(node.name()))
        					.on( field.getName() )
        					.create();
    				} else {
    					LOGGER.warn("Index already exists for Field: {}", field);
    				}
        		}
	        }
			tx.success();
		}
		
		LOGGER.info("Waiting for creation of constraints and indexes");
		try (Transaction tx = graphDb.beginTx()) {
			Schema schema = graphDb.schema();
			schema.awaitIndexesOnline(1, TimeUnit.HOURS);
		}
	}
	
	private <T extends Annotation> Set<Field> getFieldForAnnotation(OutputFileType type, Class<T> class1) {
		Set<Field> set = new HashSet<>();
		if (type.getNeo4jMapping().getParent() == Neo4jMapping.NODE) {
			for (Field field : type.getBeanClass().getDeclaredFields()) {
				if (!field.isSynthetic() && field.isAnnotationPresent(class1)) {
					set.add(field);
				}
			}
		}
		return set;
	}
	
	private boolean doesConstraintExist(Iterable<ConstraintDefinition> constraints, String name) {
		for (ConstraintDefinition cd : constraints) {
			if (cd.getLabel().equals(Label.label(name))) {
				return true;
			}
		}
		return false;
	}
	
	private boolean doesIndexExist(Iterable<IndexDefinition> indexes, String name, String on) {
		for (IndexDefinition index : indexes) {
			if (index.getLabel().equals(Label.label(name))
					&& StreamSupport.stream(index.getPropertyKeys().spliterator(), false).anyMatch(on::equals)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
    	new DatabaseIndexCreator().execute();
    	LOGGER.info("Finished");
    }
}