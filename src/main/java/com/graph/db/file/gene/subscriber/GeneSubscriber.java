package com.graph.db.file.gene.subscriber;

import com.graph.db.file.AbstractSubscriber;
import com.graph.db.file.annotation.output.OutputFileType;
import com.graph.db.file.gene.domain.Gene;

public class GeneSubscriber extends AbstractSubscriber<Gene> {

	public GeneSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.GENE;
	}
	
}
