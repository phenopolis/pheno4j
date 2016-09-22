package com.graph.db.file.annotation.domain;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.gson.Gson;

public class Annotation {
	
	private String variant_id;
	private Exac EXAC;
	private Set<TranscriptConsequence> transcript_consequences;
	
	private Integer HET_COUNT;
	private Integer WT_COUNT;
	private Integer HOM_COUNT;
	private Integer MISS_COUNT;
	
	public String getVariant_id() {
		return variant_id;
	}
	
	public void setVariant_id(String transformedVariantId) {
		variant_id = transformedVariantId;
	}

	public Exac getEXAC() {
		return EXAC;
	}

	public Set<TranscriptConsequence> getTranscript_consequences() {
		return transcript_consequences;
	}

	public Integer getHET_COUNT() {
		return HET_COUNT;
	}

	public Integer getWT_COUNT() {
		return WT_COUNT;
	}

	public Integer getHOM_COUNT() {
		return HOM_COUNT;
	}

	public Integer getMISS_COUNT() {
		return MISS_COUNT;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public static void main(String[] args) {
		try (LineNumberReader reader = new LineNumberReader(new FileReader("src/test/resources/testJson.json"));) {
			String line;
			while (( line = reader.readLine()) != null) {
				Gson gson = new Gson();
				Annotation response = gson.fromJson(line, Annotation.class);
				System.out.println(response);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
