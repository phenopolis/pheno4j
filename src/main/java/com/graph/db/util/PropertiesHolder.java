package com.graph.db.util;

import java.io.File;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class PropertiesHolder {

    private static final PropertiesConfiguration INSTANCE;
    static {
    	Configurations configs = new Configurations();
    	try {
    		INSTANCE = configs.properties(new File("config.properties"));
    	} catch (ConfigurationException e) {
    		throw new RuntimeException(e);
    	}
    }
    
    private PropertiesHolder() {
    }

    public static PropertiesConfiguration getInstance() {
        return INSTANCE;
    }
}