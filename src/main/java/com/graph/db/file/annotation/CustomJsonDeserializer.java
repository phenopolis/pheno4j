package com.graph.db.file.annotation;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.graph.db.domain.input.annotation.Exac;
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
		
		copyCaddFromTranscriptConsequenceToVariant(variant);
		setHasExac(variant);
		defaultNonDoubleExacAf(variant);
		
		return variant;
	}

	private void updateVariantIdOnTranscriptConsequences(Collection<TranscriptConsequence> transcriptConsequences,
			String variantId) {
		for (TranscriptConsequence consequence : transcriptConsequences) {
			consequence.setVariant_id(variantId);
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
	
	private void setHasExac(GeneticVariant variant) {
		variant.setHasExac(variant.getEXAC() != null);
	}
	
	private void defaultNonDoubleExacAf(GeneticVariant variant) {
		Exac exac = variant.getEXAC();
		if (exac != null) {
			if (!NumberUtils.isNumber(exac.getAF())) {
				exac.setAF("1");
			}
		}
	}
}
