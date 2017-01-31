package com.graph.db;

import java.io.IOException;

import org.neo4j.tooling.ImportTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.output.ImportCommandGenerator;

public class ImportToolRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportToolRunner.class);
	
	public void execute() {
		//Generate the Import Command
		String[] arguments = new ImportCommandGenerator().execute();
		
		//Generate the graph database
		try {
			ImportTool.main(arguments);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		LOGGER.info("Finished");
	}
	
	public static void main(String[] args) {
		new ImportToolRunner().execute();
	}
}
