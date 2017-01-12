package com.graph.db;

import java.util.EnumSet;

import com.graph.db.output.OutputFileType;

public interface Parser {

	void execute();
	
	EnumSet<OutputFileType> getNonHeaderOutputFileTypes();
}
