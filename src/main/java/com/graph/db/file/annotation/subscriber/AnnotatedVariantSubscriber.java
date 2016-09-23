package com.graph.db.file.annotation.subscriber;

import com.graph.db.file.AbstractSubscriber;
import com.graph.db.file.annotation.output.OutputFileType;

public class AnnotatedVariantSubscriber extends AbstractSubscriber<Object> {

	public AnnotatedVariantSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.ANNOTATED_VARIANT;
	}
	
}
