package com.graph.db.output;

public interface OutputFile {

	String[] getHeader();
	
	String getFileTag();
	
	Class<?> getBeanClass();
}
