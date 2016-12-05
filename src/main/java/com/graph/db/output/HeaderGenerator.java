package com.graph.db.output;

import static com.graph.db.util.Constants.COLON;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.domain.input.annotation.Exac;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.TranscriptVariantOutput;
import com.graph.db.util.Constants;
import com.graph.db.util.FileUtil;

public class HeaderGenerator {
	
	public void generateHeaders(String outputFolder, Set<OutputFileType> set) {
		Map<OutputFileType, String> headersForOutputFileTypes = generateHeadersForOutputFileTypes(set);
		for (Entry<OutputFileType, String> entry : headersForOutputFileTypes.entrySet()) {
			FileUtil.writeOutCsvHeader(outputFolder, entry.getKey().getFileTag(), Arrays.asList(entry.getValue()));
		}
	}
	
	protected Map<OutputFileType, String> generateHeadersForOutputFileTypes(Set<OutputFileType> set) {
		Map<OutputFileType, String> result = new HashMap<>();
		for (OutputFileType outputFileType : set) {
			String joinedHeaders = StringUtils.join(outputFileType.getHeader(), Constants.COMMA);
			joinedHeaders = relabelIdColumns(outputFileType, joinedHeaders);
			joinedHeaders = relabelNonStringTypes(joinedHeaders);
			
			result.put(outputFileType, joinedHeaders);
		}
		return result;
	}

	private String relabelIdColumns(OutputFileType outputFileType, String joinedHeaders) {
		switch(outputFileType) {
		case GENETIC_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", "variantId:ID(GeneticVariant)");
			break;
		case GENE_TO_GENETIC_VARIANT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", ":START_ID(Gene)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "variant_id", ":END_ID(GeneticVariant)");
			break;
		case GENE_TO_TERM:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", ":START_ID(Gene)");
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
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "consequenceTerm", "consequenceTerm:ID(ConsequenceTerm)");
			break;
		case TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "hgvsc", ":START_ID(TranscriptVariant)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "consequenceTerm", ":END_ID(ConsequenceTerm)");
			break;
		case GENE:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", "gene_id:ID(Gene)");
			break;
		case TRANSCRIPT:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "transcript_id", "transcript_id:ID(Transcript)");
			break;
		case TRANSCRIPT_TO_GENE:
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "transcript_id", ":START_ID(Transcript)");
			joinedHeaders = StringUtils.replaceOnce(joinedHeaders, "gene_id", ":END_ID(Gene)");
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
		for (Class<?> c : Arrays.asList(GeneticVariant.class, Exac.class, TranscriptConsequence.class, TranscriptVariantOutput.class)) {
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
