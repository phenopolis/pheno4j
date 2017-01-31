package com.graph.db.file.term.subscriber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.graph.db.domain.input.term.RawTerm;
import com.graph.db.domain.output.TermToParentTermOutput;
import com.graph.db.file.GenericSubscriber;
import com.graph.db.output.OutputFileType;

public class TermToParentTermSubscriber extends GenericSubscriber<RawTerm> {
	
	private final Set<TermToParentTermOutput> set = ConcurrentHashMap.newKeySet();
	
	public TermToParentTermSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.TERM_TO_PARENT_TERM);
	}
	
	@Override
	public void processRow(RawTerm rawTerm) {
		for (String isA : rawTerm.getIsA()) {
			set.add(new TermToParentTermOutput(rawTerm.getTermId(), isA));
		}
	}
	
	//TODO create subclass of GenericSubscriber to handle sets
	@Override
	public void close() {
		try {
			for (TermToParentTermOutput s : set) {
				beanWriter.write(s);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			super.close();
		}
	}
}
