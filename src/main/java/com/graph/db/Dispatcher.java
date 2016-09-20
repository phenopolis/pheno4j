package com.graph.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.graph.db.file.annotation.ExacToVariant;
import com.graph.db.file.annotation.GeneToVariant;
import com.graph.db.file.vcf.VcfParser;

public class Dispatcher {

	private static final Map<String, Class<?>> ENTRY_POINTS = new HashMap<String, Class<?>>();
	static {
		ENTRY_POINTS.put("ExacToVariant", ExacToVariant.class);
		ENTRY_POINTS.put("GeneToVariant", GeneToVariant.class);
		ENTRY_POINTS.put("VcfParser", VcfParser.class);
	}

	public static void main(final String[] args) throws Exception {
		if (args.length < 1) {
			throw new RuntimeException("Specify one of: " + ENTRY_POINTS.keySet());
		}
		final Class<?> entryPoint = ENTRY_POINTS.get(args[0]);
		if (entryPoint == null) {
			throw new RuntimeException("Entry point does not exist; specify one of: " + ENTRY_POINTS.keySet());
		}
		final String[] argsCopy = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
		entryPoint.getMethod("main", String[].class).invoke(null, (Object) argsCopy);
	}
}
