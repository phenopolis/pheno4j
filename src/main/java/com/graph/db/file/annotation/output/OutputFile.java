package com.graph.db.file.annotation.output;

public interface OutputFile {

	String[] getHeader();
	
	String getFileTag();
	
	Class<?> getBeanClass();
}
