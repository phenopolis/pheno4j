package com.graph.db.output;

import static com.graph.db.util.Constants.COLON;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.file.annotation.domain.Exac;
import com.graph.db.file.annotation.domain.GeneticVariant;
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
		case GENETIC_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", "variantId:ID(GeneticVariant)");
			break;
		case GENE_ID://TODO unused
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", "geneId:ID(GeneId)");
			break;
		case GENE_TO_GENETIC_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "left", ":START_ID(Gene)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "right", ":END_ID(GeneticVariant)");
			break;
		case GENE_SYMBOL_TO_TERM:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "geneSymbol", ":START_ID(GeneSymbol)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "termId", ":END_ID(Term)");
			break;
		case TRANSCRIPT_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "hgvsc", "hgvsc:ID(TranscriptVariant)");
			break;
		case GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", ":START_ID(GeneticVariant)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "hgvsc", ":END_ID(TranscriptVariant)");
			break;
		case TRANSCRIPT_TO_TRANSCRIPT_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "transcript_id", ":START_ID(Transcript)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "hgvsc", ":END_ID(TranscriptVariant)");
			break;
		case CONSEQUENCE_TERM:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "this", "consequenceTerm:ID(ConsequenceTerm)");
			break;
		case TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "left", ":START_ID(TranscriptVariant)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "right", ":END_ID(ConsequenceTerm)");
			break;
		case GENE:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", "gene_id:ID(GENE)");
			break;
		default:
			throw new IllegalStateException("Unknown outputFileType: " + outputFileType);
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
		for (Class<?> c : Arrays.asList(GeneticVariant.class, Exac.class, TranscriptConsequence.class)) {
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
