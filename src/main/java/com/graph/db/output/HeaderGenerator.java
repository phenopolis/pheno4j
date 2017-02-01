package com.graph.db.output;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

import com.graph.db.util.Constants;
import com.graph.db.util.FileUtil;
import com.graph.db.util.PropertiesHolder;

public class HeaderGenerator {
	
	private final String outputFolder;
	
	private static final EnumSet<OutputFileType> EXCLUSION_SET = EnumSet.of(OutputFileType.PERSON,
			OutputFileType.PERSON_TO_OBSERVED_TERM, OutputFileType.PERSON_TO_NON_OBSERVED_TERM,
			OutputFileType.GENETIC_VARIANT_TO_PERSON);

	public HeaderGenerator() {
		PropertiesConfiguration config = PropertiesHolder.getInstance();
		this.outputFolder = config.getString("output.folder");
	}
	
	public HeaderGenerator(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
	public void execute() {
		EnumSet<OutputFileType> headersToGenerate = EnumSet.complementOf(EXCLUSION_SET);
		
		Map<OutputFileType, String> headersForOutputFileTypes = generateHeadersForOutputFileTypes(headersToGenerate);
		for (Entry<OutputFileType, String> entry : headersForOutputFileTypes.entrySet()) {
			FileUtil.writeOutCsvHeader(outputFolder, entry.getKey().getFileTag(), Arrays.asList(entry.getValue()));
		}
	}
	
	protected Map<OutputFileType, String> generateHeadersForOutputFileTypes(Set<OutputFileType> set) {
		Map<OutputFileType, String> result = new HashMap<>();
		for (OutputFileType outputFileType : set) {
			String joinedHeaders = StringUtils.join(outputFileType.getHeaderForCsvFile(), Constants.COMMA);
			result.put(outputFileType, joinedHeaders);
		}
		return result;
	}

	public static void main(String[] args) {
		HeaderGenerator headerGenerator = new HeaderGenerator();
		headerGenerator.execute();
	}
}
