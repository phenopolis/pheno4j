package com.graph.db.file.annotation.subscriber;

import com.graph.db.file.annotation.output.OutputFileType;

public class VariantToAnnotationSubscriber extends AbstractSubscriber {

	public VariantToAnnotationSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.VARIANT_TO_ANNOTATION;
	}
	
}
