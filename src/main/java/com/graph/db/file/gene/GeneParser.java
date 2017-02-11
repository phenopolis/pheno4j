package com.graph.db.file.gene;

import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.graph.db.domain.output.GeneToTermOutput;
import com.graph.db.file.AbstractParser;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.Constants;

/**
 * Relationships
 * - GeneToTerm
 */
public class GeneParser extends AbstractParser {
	
	private final String fileName;
	
	public GeneParser() {
		this.fileName = config.getString("geneParser.input.fileName");
	}
	
	public GeneParser(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public Collection<File> getInputFiles() {
		return Collections.singleton(new File(fileName));
	}
	
	@Override
	protected List<? extends AutoCloseable> createSubscribers() {
		GenericSubscriber<GeneToTermOutput> geneToTermSubscriber = new GenericSubscriber<>(outputFolder, getParserClass(), OutputFileType.GENE_TO_TERM);
		return Arrays.asList(geneToTermSubscriber);
	}

	@Override
	public void processDataForFile(LineNumberReader reader) throws IOException {
		String[] header = null;
		String line;
		
		while (( line = reader.readLine()) != null) {
			logLineNumber(reader, 1000);
			
			if (header == null) {
				header = StringUtils.split(line, Constants.TAB);
				continue;
			}
			
			String[] columns = StringUtils.split(line, Constants.TAB);
			
			if (header.length == columns.length) {
				Map<String, String> map = splitColumnsIntoKeyValuePairs(header, columns);
				GeneToTermOutput output = new GeneToTermOutput(map);
				eventBus.post(output);
			}
		}
	}

	private Map<String, String> splitColumnsIntoKeyValuePairs(String[] header, String[] columns) {
		Map<String, String> result = new HashMap<>();
		for (int i = 0; i < header.length; i++) {
				result.put(header[i], columns[i]);
		}
		return result;
	}

	@Override
	public Class<?> getParserClass() {
		return GeneParser.class;
	}

	public static void main(String[] args) {
		new GeneParser().execute();
	}
}
