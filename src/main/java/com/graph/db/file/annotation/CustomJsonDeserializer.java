package com.graph.db.file.annotation;

import static com.graph.db.util.Constants.HYPHEN;
import static com.graph.db.util.Constants.UNDERSCORE;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;

public class CustomJsonDeserializer implements JsonDeserializer<GeneticVariant> {
	
	@Override
	public GeneticVariant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		GeneticVariant variant = new Gson().fromJson(json, GeneticVariant.class);
		
		String transformedVariantId = getTransformedVariantId(variant.getVariant_id());
		
		updateVariantIdOnAnnotatedVariant(variant, transformedVariantId);
		updateVariantIdOnTranscriptConsequences(variant.getTranscript_consequences(), transformedVariantId);
		
		copyCaddFromTranscriptConsequenceToVariant(variant);
		setHasExac(variant);
		
		return variant;
	}

	/**
	 * Convert hyphen delimited Variant Id to underscore delimited
	 */
	private String getTransformedVariantId(String variantIdWithHyphens) {
		int matches = StringUtils.countMatches(variantIdWithHyphens, HYPHEN);
		if (matches != 3) {
			throw new RuntimeException(variantIdWithHyphens + " does not have 3 hyphens");
		}
		return StringUtils.replace(variantIdWithHyphens, HYPHEN, UNDERSCORE);
	}

	private void updateVariantIdOnAnnotatedVariant(GeneticVariant variant, String transformedVariantId) {
		variant.setVariant_id(transformedVariantId);
	}
	
	private void updateVariantIdOnTranscriptConsequences(Collection<TranscriptConsequence> transcriptConsequences,
			String transformedVariantId) {
		for (TranscriptConsequence consequence : transcriptConsequences) {
			consequence.setVariant_id(transformedVariantId);
		}
	}
	
	private void copyCaddFromTranscriptConsequenceToVariant(GeneticVariant variant) {
		Set<String> cadds = new HashSet<>();
		for (TranscriptConsequence transcriptConsequence : variant.getTranscript_consequences()) {
			if (NumberUtils.isNumber(transcriptConsequence.getCadd())) {
				cadds.add(transcriptConsequence.getCadd());
			}
		}
		
		if (!cadds.isEmpty()) {
			if (cadds.size() > 1) {
				throw new RuntimeException(variant.getVariant_id() + " has more than 1 cadd: " + cadds);
			} else {
				String cadd = cadds.iterator().next();
				variant.setCadd(Double.valueOf(cadd));
			}
		}
	}
	
	private void setHasExac(GeneticVariant annotatedVariant) {
		annotatedVariant.setHasExac(annotatedVariant.getEXAC() != null);
	}
}
