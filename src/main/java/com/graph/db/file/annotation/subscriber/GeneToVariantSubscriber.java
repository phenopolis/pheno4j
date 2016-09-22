package com.graph.db.file.annotation.subscriber;

import com.graph.db.file.annotation.output.OutputFileType;

public class GeneToVariantSubscriber extends AbstractSubscriber {

	public GeneToVariantSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected String getOutputFileName() {
		return "GeneToVariant.csv";
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.GENE_TO_VARIANT;
	}
	
}
