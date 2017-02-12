package com.graph.db.file.annotation;

import static com.graph.db.util.FileUtil.getAllJsonFiles;
import static com.graph.db.util.FileUtil.logLineNumber;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.file.AbstractParser;
import com.graph.db.file.AbstractSubscriber;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.annotation.subscriber.ConsequenceTermSubscriber;
import com.graph.db.file.annotation.subscriber.GeneToGeneticVariantSubscriber;
import com.graph.db.file.annotation.subscriber.GeneticVariantToTranscriptVariantSubscriber;
import com.graph.db.file.annotation.subscriber.TranscriptSubscriber;
import com.graph.db.file.annotation.subscriber.TranscriptToTranscriptVariantSubscriber;
import com.graph.db.file.annotation.subscriber.TranscriptVariantSubscriber;
import com.graph.db.file.annotation.subscriber.TranscriptVariantToConsequenceTermSubscriber;
import com.graph.db.file.transcript.subscriber.GeneSubscriber;
import com.graph.db.output.OutputFileType;

/**
 * Nodes
 * - GeneticVariant
 * - TranscriptVariant
 * - ConsequenceTerm
 * - Transcript
 * - Gene
 * 
 * Relationships
 * - GeneToGeneticVariant
 * - GeneticVariantToTranscriptVariant
 * - TranscriptToTranscriptVariant
 * - TranscriptVariantToConsequenceTerm
 */
public class AnnotationParser extends AbstractParser {
	
	private final String inputFolder;
	private final Gson gson;

	public AnnotationParser() {
		this(config.getString("annotationParser.input.folder"));
	}
	
	public AnnotationParser(String inputFolder) {
		this.inputFolder = inputFolder;
		if (StringUtils.isBlank(inputFolder)) {
			throw new RuntimeException("inputFolder cannot be empty");
		}
		
		gson = createGson();
	}
	
	@Override
	public Collection<File> getInputFiles() {
		return getAllJsonFiles(inputFolder);
	}

	@Override
	protected List<AbstractSubscriber> createSubscribers() {
        GeneToGeneticVariantSubscriber geneToGeneticVariantSubscriber = new GeneToGeneticVariantSubscriber(outputFolder, getParserClass());
        GenericSubscriber<Object> geneticVariantSubscriber = new GenericSubscriber<Object>(outputFolder, getParserClass(), OutputFileType.GENETIC_VARIANT);
        TranscriptVariantSubscriber transcriptVariantSubscriber = new TranscriptVariantSubscriber(outputFolder, getParserClass());
        GeneticVariantToTranscriptVariantSubscriber geneticVariantToTranscriptVariantSubscriber = new GeneticVariantToTranscriptVariantSubscriber(outputFolder, getParserClass());
        TranscriptToTranscriptVariantSubscriber transcriptToTranscriptVariantSubscriber = new TranscriptToTranscriptVariantSubscriber(outputFolder, getParserClass());
        ConsequenceTermSubscriber consequenceTermSubscriber = new ConsequenceTermSubscriber(outputFolder, getParserClass());
        TranscriptVariantToConsequenceTermSubscriber transcriptVariantToConsequenceTermSubscriber = new TranscriptVariantToConsequenceTermSubscriber(outputFolder, getParserClass());
        TranscriptSubscriber transcriptSubscriber = new TranscriptSubscriber(outputFolder, getParserClass());
        GeneSubscriber geneSubscriber = new GeneSubscriber(outputFolder, getParserClass());
        
		return Arrays.asList(geneToGeneticVariantSubscriber, geneticVariantSubscriber, transcriptVariantSubscriber,
				geneticVariantToTranscriptVariantSubscriber, transcriptToTranscriptVariantSubscriber,
				consequenceTermSubscriber, transcriptVariantToConsequenceTermSubscriber, transcriptSubscriber, geneSubscriber);
	}

	@Override
	public void processDataForFile(LineNumberReader reader) throws IOException {
		String line;
		
		while (( line = reader.readLine()) != null) {
			logLineNumber(reader, 1000);
			
			GeneticVariant geneticVariant = gson.fromJson(line, GeneticVariant.class);
			eventBus.post(geneticVariant);
		}
	}
	
	private Gson createGson() {
		GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(GeneticVariant.class, new CustomJsonDeserializer());
        return b.create();
	}

	@Override
	public Class<?> getParserClass() {
		return AnnotationParser.class;
	}

	public static void main(String[] args) {
		new AnnotationParser().execute();
	}
}
