package com.graph.db.file.annotation;

import static com.graph.db.util.Constants.HYPHEN;
import static com.graph.db.util.Constants.UNDERSCORE;

import java.lang.reflect.Type;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.domain.TranscriptConsequence;

public class CustomJsonDeserializer implements JsonDeserializer<Annotation> {
	
	@Override
	public Annotation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		Annotation annotation = new Gson().fromJson(json, Annotation.class);
		
		String transformedVariantId = getTransformedVariantId(annotation.getVariant_id());
		
		updateVariantIdOnAnnotation(annotation, transformedVariantId);
		updateVariantIdOnTranscriptConsequences(annotation.getTranscript_consequences(), transformedVariantId);
		
		return annotation;
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

	private void updateVariantIdOnAnnotation(Annotation annotation, String transformedVariantId) {
		annotation.setVariant_id(transformedVariantId);
	}
	
	private void updateVariantIdOnTranscriptConsequences(Collection<TranscriptConsequence> transcriptConsequences,
			String transformedVariantId) {
		for (TranscriptConsequence consequence : transcriptConsequences) {
			consequence.setVariant_id(transformedVariantId);
		}
	}
}
