package com.graph.db.file.term.subscriber;

import com.graph.db.domain.input.term.RawTerm;
import com.graph.db.domain.output.TermToParentTermOutput;
import com.graph.db.file.SetBasedGenericSubscriber;
import com.graph.db.output.OutputFileType;

public class TermToParentTermSubscriber extends SetBasedGenericSubscriber<RawTerm, TermToParentTermOutput> {
	
	public TermToParentTermSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.TERM_TO_PARENT_TERM);
	}
	
	@Override
	public void processRow(RawTerm rawTerm) {
		for (String isA : rawTerm.getIsA()) {
			set.add(new TermToParentTermOutput(rawTerm.getTermId(), isA));
		}
	}
}
