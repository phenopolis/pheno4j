package com.graph.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.file.annotation.AnnotationParser;
import com.graph.db.file.gene.GeneParser;
import com.graph.db.file.person.PersonParser;
import com.graph.db.file.term.TermParser;
import com.graph.db.file.transcript.TranscriptParser;
import com.graph.db.file.vcf.VcfParser;
import com.graph.db.output.FileUnion;
import com.graph.db.output.HeaderGenerator;
import com.graph.db.output.ImportCommandGenerator;

public class AllRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AllRunner.class);
	
	public void execute() {
		//Run all the Parsers
		new VcfParser().execute();
		new PersonParser().execute();
		new TranscriptParser().execute();
		new TermParser().execute();
		new GeneParser().execute();
		new AnnotationParser().execute();
		
		//Run the File Unioner
		new FileUnion().execute();
		
		//Generate the headers
		new HeaderGenerator().execute();
		
		//Generate the Import Command
		new ImportCommandGenerator().execute();
		
		LOGGER.info("Finished");
	}
	
	public static void main(String[] args) {
		new AllRunner().execute();
	}
}
