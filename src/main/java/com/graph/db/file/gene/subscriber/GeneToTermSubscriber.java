package com.graph.db.file.gene.subscriber;

import java.io.IOException;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.eventbus.Subscribe;
import com.graph.db.file.AbstractSubscriber;
import com.graph.db.file.annotation.output.OutputFileType;
import com.graph.db.file.gene.domain.Gene;
import com.graph.db.file.gene.domain.GeneToTerm;

public class GeneToTermSubscriber extends AbstractSubscriber<Gene> {

	public GeneToTermSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.GENE_TO_TERM;
	}
	
    @Override
	@Subscribe
    public void processAnnotation(Gene gene) {
    	try {
    		if (CollectionUtils.isNotEmpty(gene.getHpo())) {
    			for (String term : gene.getHpo()) {
    				beanWriter.write(new GeneToTerm(gene.getGene(), term));
    			}
    		}
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    }
	
}
