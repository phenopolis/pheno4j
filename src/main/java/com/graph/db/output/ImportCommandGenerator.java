package com.graph.db.output;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.SPACE;
import static com.graph.db.util.FileUtil.createFileName;
import static com.graph.db.util.FileUtil.createHeaderFileName;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.graph.db.Parser;
import com.graph.db.util.FileUtil;

import sun.reflect.ReflectionFactory;

@SuppressWarnings("restriction")
public class ImportCommandGenerator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportCommandGenerator.class);
	
	private final String inputFolderPath;
	private final String outputFolderPath;
	
	public ImportCommandGenerator(String inputFolderPath, String outputFolderPath) {
		this.inputFolderPath = inputFolderPath;
		this.outputFolderPath = outputFolderPath;
	}
	
	public void execute() {
		List<String> commandLines = new ArrayList<>();
		commandLines.add("bin/neo4j-import  --into " + outputFolderPath + File.separator + "graph.db --id-type string --bad-tolerance 1000000 --skip-bad-relationships true --skip-duplicate-nodes true \\");
		
		Set<Class<? extends Parser>> parserImplementations = getSubclassesOfParser();
		Multimap<OutputFileType, String> outputFileTypeToFileNames = createOutputFileTypeToFileNamesMap(parserImplementations);
		for (Neo4jMapping neo4jMapping : Neo4jMapping.values()) {
			if (neo4jMapping.getParent() != null) {
				OutputFileType outputFileType = OutputFileType.toOutputFileType(neo4jMapping);
				
				StringBuilder builder = new StringBuilder();
				appendNeo4jType(builder, neo4jMapping);
				appendNeo4jMapping(builder, neo4jMapping);
				appendHeaderFileName(builder, outputFileType);
				appendContentFiles(builder, outputFileTypeToFileNames, outputFileType);
				builder.append(" \\");
				
				commandLines.add(builder.toString());
			}
		}
		
		commandLines.add("> " + inputFolderPath + File.separator + "neo4j-log.txt &");
		
		for (String line : commandLines) {
			System.out.println(line);
		}
	}

	private Set<Class<? extends Parser>> getSubclassesOfParser() {
		Reflections reflections = new Reflections("com.graph.db");
		return reflections.getSubTypesOf(Parser.class);
	}

	private Multimap<OutputFileType, String> createOutputFileTypeToFileNamesMap(Set<Class<? extends Parser>> parserImplementations) {
		Multimap<OutputFileType, String> outputFileTypeToFileNames = ArrayListMultimap.create();
		for (Class<? extends Parser> clazz : parserImplementations) {
			Parser parser = createClassWithoutUsingConstructor(clazz);
			for (OutputFileType outputFileType : parser.getNonHeaderOutputFileTypes()) {
				String fileName = createFileName(inputFolderPath, clazz, outputFileType);
				outputFileTypeToFileNames.put(outputFileType, fileName);
			}
		}
		return outputFileTypeToFileNames;
	}
	
	private <T> T createClassWithoutUsingConstructor(Class<T> clazz) {
		try {
			ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
			Constructor<Object> objDef;
			objDef = Object.class.getDeclaredConstructor();
			Constructor<?> intConstr = rf.newConstructorForSerialization(clazz, objDef);
			return clazz.cast(intConstr.newInstance());
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
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
		return builder.append(neo4jMapping).append(SPACE);
	}
	
	private void appendHeaderFileName(StringBuilder builder, OutputFileType outputFileType) {
		String headerFileName = createHeaderFileName(inputFolderPath, outputFileType.getFileTag());
		builder.append(headerFileName).append(COMMA);
	}
	
	private void appendContentFiles(StringBuilder builder, Multimap<OutputFileType, String> outputFileTypeToFileNames,OutputFileType outputFileType) {
		if (relevantForFileUnion(outputFileType)) {
			builder.append(FileUtil.createUnionedFileName(outputFolderPath, outputFileType.getFileTag()));
		} else {
			Collection<String> collection = outputFileTypeToFileNames.get(outputFileType);
			if (collection.isEmpty()) {
				throw new IllegalStateException("No file names for: " + outputFileType);
			}
			builder.append(StringUtils.join(collection, COMMA));
		}
	}
	
	private boolean relevantForFileUnion(OutputFileType outputFileType) {
		return FileUnion.SOURCE_OUTPUTFILETYPES.contains(outputFileType);
	}
	
	public static void main(String[] args) {
		if ((args != null) && (args.length != 2)) {
			throw new RuntimeException("Incorrect args: $1=inputFolder, $2=outputFolder");
		}
		new ImportCommandGenerator(args[0], args[1]).execute();
		LOGGER.info("Finished");
	}

}
