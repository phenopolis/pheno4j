package com.graph.db.file.annotation.subscriber;

import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.output.OutputFileType;

public class AnnotationToVariantSubscriber extends AbstractSubscriber {

	public AnnotationToVariantSubscriber(String outputFolder) {
		super(outputFolder);
	}

	@Override
	protected String getOutputFileName() {
		return "AnnotationToVariant.csv";
	}

	@Override
	protected Class<?> getBeanClass() {
		return Annotation.class;
	}
	
	@Override
	protected OutputFileType getOutputFileType() {
		return OutputFileType.ANNOTATION_TO_VARIANT;
	}
	
}
