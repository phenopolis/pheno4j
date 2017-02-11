package com.graph.db.file;

import java.io.File;
import java.util.Collection;

public interface Parser {

	void execute();
	
	Collection<File> getInputFiles();
	
}
