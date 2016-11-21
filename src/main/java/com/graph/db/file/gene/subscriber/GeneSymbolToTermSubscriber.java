package com.graph.db.file.gene.subscriber;

import java.io.IOException;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.eventbus.Subscribe;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.file.gene.domain.Gene;
import com.graph.db.file.gene.domain.GeneSymbolToTerm;
import com.graph.db.output.OutputFileType;

public class GeneSymbolToTermSubscriber extends GenericSubscriber<Gene> {

    public GeneSymbolToTermSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.GENE_SYMBOL_TO_TERM);
	}

	@Override
	@Subscribe
    public void processAnnotation(Gene gene) {
    	try {
    		if (CollectionUtils.isNotEmpty(gene.getHpo())) {
    			for (String term : gene.getHpo()) {
    				beanWriter.write(new GeneSymbolToTerm(gene.getGene(), term));
    			}
    		}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    }
}
