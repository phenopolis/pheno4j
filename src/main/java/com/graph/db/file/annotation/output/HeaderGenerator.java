package com.graph.db.file.annotation.output;

import static com.graph.db.util.Constants.COLON;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.domain.Exac;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.util.Constants;

public class HeaderGenerator {
	
	public void generateHeaders(String outputFolder) {
		for (OutputFileType outputFileType : OutputFileType.values()) {
			String joinedHeaders = StringUtils.join(outputFileType.getHeader(), Constants.COMMA);
			joinedHeaders = relabelIdColumns(outputFileType, joinedHeaders);
			joinedHeaders = relabelNonStringTypes(joinedHeaders);
			
			writeOutHeader(outputFolder, outputFileType, joinedHeaders);
		}
	}

	private String relabelIdColumns(OutputFileType outputFileType, String joinedHeaders) {
		switch(outputFileType) {
		case VARIANT_TO_ANNOTATION:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", ":START_ID(Variant)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", ":END_ID(Annotation)");
			break;
		case GENE:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", "geneId:ID(Gene)");
			break;
		case GENE_TO_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", ":START_ID(Gene)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", ":END_ID(Variant)");
			break;
		default:
			throw new IllegalStateException();
		}
		return joinedHeaders;
	}
	
	private String relabelNonStringTypes(String joinedHeaders) {
		Map<String, String> nameToGraphType = createMapFromJavaTypeToGraphType();
		
		for (Entry<String, String> entry : nameToGraphType.entrySet()) {
			joinedHeaders = StringUtils.replace(joinedHeaders, entry.getKey(), entry.getValue());
		}
		return joinedHeaders;
	}
	
	//currently only handles ints everything else will be a string
	private Map<String, String> createMapFromJavaTypeToGraphType() {
		Map<String, String> nameToGraphType = new HashMap<>();
		for (Class<?> c : Arrays.asList(Annotation.class, Exac.class, TranscriptConsequence.class)) {
			for (Field field : c.getDeclaredFields()) {
				String name = field.getName();
				switch(field.getType().getName()) {
				case "java.lang.Integer":
					nameToGraphType.put(name, name + COLON + "int");
					break;
				}
			}
		}
		return nameToGraphType;
	}
	
	private void writeOutHeader(String outputFolder, OutputFileType outputFileType, String joinedHeaders) {
		String pathname = outputFolder + File.separator + outputFileType.getFileTag() + Constants.HYPHEN + "header.csv";
		try {
			FileUtils.writeStringToFile(new File(pathname), joinedHeaders);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
