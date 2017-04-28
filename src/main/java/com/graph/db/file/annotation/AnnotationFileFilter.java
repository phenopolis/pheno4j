package com.graph.db.file.annotation;

import static com.graph.db.util.Constants.COMMA;
import static com.graph.db.util.Constants.DOUBLE_QUOTE;
import static com.graph.db.util.FileUtil.logLineNumber;
import static java.util.stream.Collectors.toSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.file.AbstractParser;
import com.graph.db.util.FileUtil;
import com.graph.db.util.ManagedEventBus.PoisonPill;

public class AnnotationFileFilter extends AbstractParser {
	
	private final String annotationFileName;
	
	public AnnotationFileFilter() {
		this(config.getString("annotationParser.input.fileName"));
	}
	
	public AnnotationFileFilter(String annotationFile) {
		this.annotationFileName = annotationFile;
		
		if (StringUtils.isBlank(annotationFile)) {
			throw new RuntimeException("annotationFileName cannot be empty");
		}
	}
	
	@Override
	public Collection<File> getInputFiles() {
		return Collections.singleton(new File(annotationFileName));
	}

	@Override
	protected void processDataForFile(LineNumberReader reader) throws IOException {
		String line;
		
		while (( line = reader.readLine()) != null) {
			logLineNumber(reader, 1000);
			eventBus.post(line);
		}
	}

	@Override
	protected List<? extends AutoCloseable> createSubscribers() {
		return Arrays.asList(new FilteredAnnotationSubscriber());
	}

	@Override
	protected Class<?> getParserClass() {
		return AnnotationFileFilter.class;
	}
	
	private class FilteredAnnotationSubscriber implements AutoCloseable {
		private final Set<String> variantIds;
		private final Gson gson;
		private final BufferedWriter writer;
		
		public FilteredAnnotationSubscriber() {
			String vcfOutputHetFile = config.getString("annotationFileFilter.input.vcfOutputHetFile");
			String vcfOutputHomFile = config.getString("annotationFileFilter.input.vcfOutputHomFile");
					
			variantIds = new HashSet<>();
			variantIds.addAll(extractVariantsFromFile(vcfOutputHetFile));
			variantIds.addAll(extractVariantsFromFile(vcfOutputHomFile));
			LOGGER.info("Searching for {} variant ids", variantIds.size());
			
			gson = createGson();
			
			File fout = new File(getOutputFolder() + File.separator + "test.json");
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(fout);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			writer = new BufferedWriter(new OutputStreamWriter(fos));
		}

		private Set<String> extractVariantsFromFile(String file) {
			List<String> lines = FileUtil.getLines(file);
			return lines.stream()
				.map(line -> StringUtils.split(line, COMMA))
				.map(fields -> fields[0])
				.map(variantId -> StringUtils.replace(variantId, DOUBLE_QUOTE, ""))
				.collect(toSet());
		}
		
		private Gson createGson() {
			GsonBuilder b = new GsonBuilder();
	        b.registerTypeAdapter(GeneticVariant.class, new CustomJsonDeserializer());
	        b.setLenient();
	        return b.create();
		}
		
		@Subscribe
	    public void processRow(Object line) {
	    	if (line instanceof PoisonPill) {
	    		return;
	    	}
	    	
	    	String json = (String) line;
			GeneticVariant geneticVariant = gson.fromJson(json, GeneticVariant.class);
	    	String variant_id = geneticVariant.getVariant_id();
	    	
	    	if (variantIds.contains(variant_id)) {
	    		try {
	    			LOGGER.info("found: {}", variant_id);
	    			writer.write(json);
	    			writer.newLine();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
	    	}
	    }

		@Override
		public void close() throws Exception {
			writer.close();
		}
	}
	
	public static void main(String[] args) {
		new AnnotationFileFilter().execute();
	}
}
