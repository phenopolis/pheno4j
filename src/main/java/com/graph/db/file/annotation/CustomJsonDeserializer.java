package com.graph.db.file.annotation;

import java.lang.reflect.Type;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.TranscriptConsequence;

public class CustomJsonDeserializer implements JsonDeserializer<GeneticVariant> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomJsonDeserializer.class);
	
	@Override
	public GeneticVariant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		GeneticVariant variant = null;
		try {
			variant = new Gson().fromJson(json, GeneticVariant.class);
		} catch (Exception e) {
			LOGGER.error("{}", json);
			throw e;
		}
		
		updateVariantIdOnTranscriptConsequences(variant);
		
		return variant;
	}

	private void updateVariantIdOnTranscriptConsequences(GeneticVariant variant) {
		if (variant.getTranscript_consequences() == null) {
			variant.setTranscript_consequences(Collections.emptySet());
		} else {
			for (TranscriptConsequence consequence : variant.getTranscript_consequences()) {
				consequence.setVariant_id(variant.getVariant_id());
			}
		}
	}
}
