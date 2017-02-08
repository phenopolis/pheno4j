package com.graph.db;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.domain.output.annotation.Id;
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
        
		try (Transaction tx = graphDb.beginTx()) {
			Schema schema = graphDb.schema();
			Iterable<ConstraintDefinition> constraints = schema.getConstraints();
			
			List<Id> idFields = getIdFields();
			for (Id id : idFields) {
				if(!doesConstraintExist(constraints, id.mapping().name())) {
					LOGGER.info("Creating constraint for field: {}", id);
					schema.constraintFor(Label.label(id.mapping().name()))
	    	        	.assertPropertyIsUnique(id.name())
	                    .create();
				} else {
					LOGGER.warn("Constraint already exists for field: {}", id);
				}
			}
			
			tx.success();
        }
		
		LOGGER.info("Waiting for creation of indexes");
		try (Transaction tx = graphDb.beginTx()) {
			Schema schema = graphDb.schema();
			schema.awaitIndexesOnline(1, TimeUnit.HOURS);
		}
	}
	
	private boolean doesConstraintExist(Iterable<ConstraintDefinition> constraints, String name) {
		for (ConstraintDefinition cd : constraints) {
			if (cd.getLabel().equals(Label.label(name))) {
				return true;
			}
		}
		return false;
	}

	private List<Id> getIdFields() {
		List<Id> idFields = new ArrayList<>();
		for (OutputFileType type : OutputFileType.values()) {
			if (type.getNeo4jMapping().getParent() == Neo4jMapping.NODE) {
				for (Field field : type.getBeanClass().getDeclaredFields()) {
					if (!field.isSynthetic()) {
						if (field.isAnnotationPresent(Id.class)) {
							Id annotation = field.getAnnotation(Id.class);
							idFields.add(annotation);
						}
					}
				}
			}
		}
		return idFields;
	}
	
    public static void main(String[] args) throws IOException {
    	new DatabaseIndexCreator().execute();
    	LOGGER.info("Finished");
    }
}