package com.graph.db.file;

import org.apache.commons.configuration2.Configuration;

import com.graph.db.util.PropertiesHolder;

public abstract class LegacyParser implements Parser {
	
	protected final Configuration config;
	protected String outputFolder;
	
	public LegacyParser() {
		config = PropertiesHolder.getInstance();
		
		this.outputFolder = getOutputFolder();
	}

	protected String getOutputFolder() {
		return config.getString("output.folder");
	}
}
