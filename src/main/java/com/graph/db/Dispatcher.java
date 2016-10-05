package com.graph.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.graph.db.file.annotation.AnnotationParser;
import com.graph.db.file.gene.GeneParser;
import com.graph.db.file.person.PersonParser;
import com.graph.db.file.term.TermParser;
import com.graph.db.file.vcf.VcfParser;

public class Dispatcher {

	private static final Map<String, Class<?>> ENTRY_POINTS = new HashMap<String, Class<?>>();
	static {
		ENTRY_POINTS.put("VcfParser", VcfParser.class);
		ENTRY_POINTS.put("AnnotationParser", AnnotationParser.class);
		ENTRY_POINTS.put("GeneParser", GeneParser.class);
		ENTRY_POINTS.put("PersonParser", PersonParser.class);
		ENTRY_POINTS.put("TermParser", TermParser.class);
	}

	public static void main(final String[] args) throws Exception {
		if (args.length < 1) {
			throw new RuntimeException("Specify one of: " + ENTRY_POINTS.keySet());
		}
		Class<?> entryPoint = ENTRY_POINTS.get(args[0]);
		if (entryPoint == null) {
			throw new RuntimeException("Entry point does not exist; specify one of: " + ENTRY_POINTS.keySet());
		}
		String[] argsCopy = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
		entryPoint.getMethod("main", String[].class).invoke(null, (Object) argsCopy);
	}
}
