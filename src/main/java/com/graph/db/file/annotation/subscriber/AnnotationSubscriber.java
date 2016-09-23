package com.graph.db.file.annotation.subscriber;

import com.graph.db.file.annotation.output.OutputFileType;

public class AnnotationSubscriber extends AbstractSubscriber {

	public AnnotationSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.ANNOTATION;
	}
	
}
