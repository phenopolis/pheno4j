package com.graph.db.output;

import static com.graph.db.util.Constants.COLON;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.file.annotation.domain.AnnotatedVariant;
import com.graph.db.file.annotation.domain.Exac;
import com.graph.db.file.annotation.domain.TranscriptConsequence;
import com.graph.db.util.Constants;
import com.graph.db.util.FileUtil;

public class HeaderGenerator {
	
	public void generateHeaders(String outputFolder, Set<OutputFileType> set) {
		for (OutputFileType outputFileType : set) {
			String joinedHeaders = StringUtils.join(outputFileType.getHeader(), Constants.COMMA);
			joinedHeaders = relabelIdColumns(outputFileType, joinedHeaders);
			joinedHeaders = relabelNonStringTypes(joinedHeaders);
			
			FileUtil.writeOutCsvHeader(outputFolder, outputFileType.getFileTag(), Arrays.asList(joinedHeaders));
		}
	}

	private String relabelIdColumns(OutputFileType outputFileType, String joinedHeaders) {
		switch(outputFileType) {
		case ANNOTATED_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", "variantId:ID(AnnotatedVariant)");
			break;
		case VARIANT_TO_ANNOTATED_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", ":START_ID(Variant)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", ":END_ID(AnnotatedVariant)");
			break;
		case GENE_ID:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", "geneId:ID(GeneId)");
			break;
		case GENE_ID_TO_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", ":START_ID(GeneId)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", ":END_ID(Variant)");
			break;
		case GENE_SYMBOL_TO_GENE_ID:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "geneSymbol", ":START_ID(GeneSymbol)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "geneId", ":END_ID(GeneId)");
			break;
		case GENE_SYMBOL:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene", "geneSymbol:ID(GeneSymbol)");
			break;
		case GENE_SYMBOL_TO_TERM:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "geneSymbol", ":START_ID(GeneSymbol)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "termId", ":END_ID(Term)");
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
	
	private Map<String, String> createMapFromJavaTypeToGraphType() {
		Map<String, String> nameToGraphType = new HashMap<>();
		for (Class<?> c : Arrays.asList(AnnotatedVariant.class, Exac.class, TranscriptConsequence.class)) {
			for (Field field : c.getDeclaredFields()) {
				String name = field.getName();
				switch (field.getType().getName()) {
				case "java.lang.Integer":
					nameToGraphType.put(name, name + COLON + "int");
					break;
				case "java.lang.Double":
					nameToGraphType.put(name, name + COLON + "double");
					break;
				case "java.lang.Boolean":
					nameToGraphType.put(name, name + COLON + "boolean");
					break;
				}
			}
		}
		return nameToGraphType;
	}

}
