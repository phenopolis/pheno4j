package com.graph.db.file;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.graph.db.output.OutputFileType;

public class SetBasedGenericSubscriber<InputType, SetType> extends GenericSubscriber<InputType> {
	
	protected final Set<SetType> set = ConcurrentHashMap.newKeySet();

	public SetBasedGenericSubscriber(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		super(outputFolder, parserClass, outputFileType);
	}
	
	@Override
	public void preClose() {
		for (SetType s : set) {
			write(s);
		}
	}
}
