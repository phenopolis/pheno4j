package com.graph.db.file;

import static com.graph.db.util.FileUtil.createFolderIfNotPresent;

import org.apache.commons.configuration2.Configuration;

import com.graph.db.util.PropertiesHolder;

public abstract class LegacyParser implements Parser {
	
	protected final Configuration config;
	protected String outputFolder;
	
	public LegacyParser() {
		config = PropertiesHolder.getInstance();
		
		this.outputFolder = getOutputFolder();
		createFolderIfNotPresent(outputFolder);
	}

	protected String getOutputFolder() {
		return config.getString("output.folder");
	}
}
