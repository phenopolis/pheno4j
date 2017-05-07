package com.graph.db.output;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.Constants.EQUALS;
import static com.graph.db.util.FileUtil.createFileName;
import static com.graph.db.util.FileUtil.createHeaderFileName;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.graph.db.file.Parser;
import com.graph.db.file.annotation.AnnotationParser;
import com.graph.db.file.gene.GeneParser;
import com.graph.db.file.person.PersonParser;
import com.graph.db.file.term.TermParser;
import com.graph.db.file.transcript.TranscriptParser;
import com.graph.db.file.vcf.VcfParser;
import com.graph.db.util.FileUtil;
import com.graph.db.util.PropertiesHolder;

public class ImportCommandGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportCommandGenerator.class);
	
	private final String inputFolderPath;
	private final String outputFolderPath;

	private final Multimap<String, String> neoTypeToManualFiles;
	
	public ImportCommandGenerator() {
		PropertiesConfiguration config = PropertiesHolder.getInstance();
		
		this.inputFolderPath = config.getString("output.folder");
		this.outputFolderPath = config.getString("output.folder") + File.separator + "graph-db" + File.separator + "data" + File.separator +"databases" + File.separator + "graph.db";
		
		this.neoTypeToManualFiles = getNeoTypeToManualFiles(config);
	}

	private Multimap<String, String> getNeoTypeToManualFiles(PropertiesConfiguration config) {
		Multimap<String,String> neoTypeToManualFiles = HashMultimap.create();
		Iterator<String> keys = config.getKeys("manualUpload");
		while (keys.hasNext()) {
			String key = keys.next();
			String propertyValue = config.getString(key);
			if (StringUtils.isNotEmpty(propertyValue)) {
				String[] split = propertyValue.split(COMMA);
				for (String s : split) {
					neoTypeToManualFiles.put(StringUtils.remove(key, "manualUpload."), s);
				}
			}
		}
		return neoTypeToManualFiles;
	}
	
	public String[] execute() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("--stacktrace", "true");
		map.put("--into", outputFolderPath);
		map.put("--id-type", "string");
		map.put("--bad-tolerance", "1000000");
		map.put("--skip-bad-relationships", "true");
		map.put("--skip-duplicate-nodes", "true");
		
		Set<Class<? extends Parser>> parserImplementations = getSubclassesOfParser();
		Multimap<OutputFileType, String> outputFileTypeToFileNames = createOutputFileTypeToFileNamesMap(parserImplementations);
		for (Neo4jMapping neo4jMapping : Neo4jMapping.values()) {
			if (neo4jMapping.getParent() != null) {
				OutputFileType outputFileType = OutputFileType.toOutputFileType(neo4jMapping);
				
				StringBuilder key = new StringBuilder();
				appendNeo4jType(key, neo4jMapping);
				appendNeo4jMapping(key, neo4jMapping);
				
				StringBuilder value = new StringBuilder();
				appendHeaderFileName(value, outputFileType);
				appendContentFiles(value, outputFileTypeToFileNames, outputFileType);
				appendManualFiles(value, neo4jMapping);
				
				map.put(key.toString(), value.toString());
			}
		}
		
		logImportCommandToConsole(map);
		return convertMapToStringArray(map);
	}
	
	private void logImportCommandToConsole(Map<String, String> map) {
		LOGGER.info("Following command will be passed into neo4j-import");
		map.entrySet().stream()
			.map(entry -> StringUtils.join(entry.getKey(), EQUALS, DOUBLE_QUOTE, entry.getValue(), DOUBLE_QUOTE))
			.forEach(System.out::println);
	}

	private String[] convertMapToStringArray(Map<String, String> map) {
		return map.entrySet().stream()
			.map(entry -> StringUtils.join(entry.getKey(), EQUALS, entry.getValue()))
			.toArray(size -> new String[ size ]);
	}

	private Set<Class<? extends Parser>> getSubclassesOfParser() {
		return Sets.newHashSet(Arrays.asList(AnnotationParser.class, GeneParser.class, TermParser.class,
				TranscriptParser.class, PersonParser.class, VcfParser.class));
	}

	private Multimap<OutputFileType, String> createOutputFileTypeToFileNamesMap(Set<Class<? extends Parser>> parserImplementations) {
		Multimap<OutputFileType, String> outputFileTypeToFileNames = ArrayListMultimap.create();
		for (OutputFileType outputFileType : OutputFileType.values()) {
			for (Class<? extends Parser> parser : outputFileType.getCreatedBy()) {
				String fileName = createFileName(inputFolderPath, parser, outputFileType);
				outputFileTypeToFileNames.put(outputFileType, fileName);
			}
		}
		return outputFileTypeToFileNames;
	}

	private void appendNeo4jType(StringBuilder builder, Neo4jMapping neo4jMapping) {
		switch(neo4jMapping.getParent()) {
		case NODE:
			builder.append("--nodes:");
			break;
		case RELATIONSHIP:
			builder.append("--relationships:");
			break;
		default:
			throw new IllegalStateException("Unknown parent: " + neo4jMapping.getParent());
		}
	}
	
	private StringBuilder appendNeo4jMapping(StringBuilder builder, Neo4jMapping neo4jMapping) {
		return builder.append(neo4jMapping);
	}
	
	private void appendHeaderFileName(StringBuilder builder, OutputFileType outputFileType) {
		String headerFileName = createHeaderFileName(inputFolderPath, outputFileType.getFileTag());
		builder.append(headerFileName).append(COMMA);
	}
	
	private void appendContentFiles(StringBuilder builder, Multimap<OutputFileType, String> outputFileTypeToFileNames,OutputFileType outputFileType) {
		if (relevantForFileUnion(outputFileType)) {
			builder.append(FileUtil.createUnionedFileName(inputFolderPath, outputFileType.getFileTag()));
		} else {
			Collection<String> collection = outputFileTypeToFileNames.get(outputFileType);
			if (collection.isEmpty()) {
				throw new IllegalStateException("No file names for: " + outputFileType);
			}
			builder.append(StringUtils.join(collection, COMMA));
		}
	}
	
	private void appendManualFiles(StringBuilder value, Neo4jMapping neo4jMapping) {
		if (neoTypeToManualFiles.containsKey(neo4jMapping.toString())) {
			Collection<String> collection = neoTypeToManualFiles.get(neo4jMapping.toString());
			StringJoiner stringJoiner = new StringJoiner(COMMA, COMMA, EMPTY);
			collection.stream().forEach(file -> stringJoiner.add(file));
			value.append(stringJoiner.toString());
		}
	}
	
	private boolean relevantForFileUnion(OutputFileType outputFileType) {
		return FileUnion.SOURCE_OUTPUTFILETYPES.contains(outputFileType);
	}
	
	public static void main(String[] args) {
		new ImportCommandGenerator().execute();
		LOGGER.info("Finished");
	}
}
