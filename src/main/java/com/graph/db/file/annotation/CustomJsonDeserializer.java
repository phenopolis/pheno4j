package com.graph.db.file.annotation;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;

public class CustomJsonDeserializer implements JsonDeserializer<GeneticVariant> {
	
	@Override
	public GeneticVariant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		GeneticVariant variant = new Gson().fromJson(json, GeneticVariant.class);
		if (variant.getTranscript_consequences() == null) {
			variant.setTranscript_consequences(Collections.emptySet());
		}
		
		updateVariantIdOnTranscriptConsequences(variant.getTranscript_consequences(), variant.getVariant_id());
		
		clearNonDoubleCadds(variant.getTranscript_consequences());
		setHasExac(variant);
		
		return variant;
	}

	private void updateVariantIdOnTranscriptConsequences(Collection<TranscriptConsequence> transcriptConsequences,
			String variantId) {
		for (TranscriptConsequence consequence : transcriptConsequences) {
			consequence.setVariant_id(variantId);
		}
	}
	
	private void clearNonDoubleCadds(Set<TranscriptConsequence> set) {
		for (TranscriptConsequence transcriptConsequence : set) {
			if (!NumberUtils.isNumber(transcriptConsequence.getCadd())) {
				transcriptConsequence.setCadd(null);
			}
		}
	}
	
	private void setHasExac(GeneticVariant annotatedVariant) {
		annotatedVariant.setHasExac(annotatedVariant.getEXAC() != null);
	}
}
