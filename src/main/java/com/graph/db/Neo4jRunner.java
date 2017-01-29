package com.graph.db;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.neo4j.helpers.collection.Pair;
import org.neo4j.server.CommunityBootstrapper;
import org.neo4j.server.NeoServer;
import org.neo4j.server.ServerBootstrapper;

import com.graph.db.util.PropertiesHolder;

public class Neo4jRunner {
	
    public static void main(String[] args) throws IOException {
		PropertiesConfiguration config = PropertiesHolder.getInstance();
		String outputFolderPath = config.getString("output.folder");
    
        File storeDir = new File(outputFolderPath + File.separator + "graph-db");

        ServerBootstrapper serverBootstrapper = new CommunityBootstrapper();
        serverBootstrapper.start(
            storeDir,
            Optional.empty(), // omit configfile, properties follow
            Pair.of("dbms.connector.http.address","127.0.0.1:7474"),
            Pair.of("dbms.connector.http.enabled", "true"),
            Pair.of("dbms.connector.bolt.enabled", "true"),

            // allow the shell connections via port 1337 (default)
            Pair.of("dbms.shell.enabled", "true"),
            Pair.of("dbms.shell.host", "127.0.0.1"),
            Pair.of("dbms.shell.port", "1337"),
            Pair.of("dbms.security.auth_enabled", "false")
        );
        // ^^ serverBootstrapper.start() also registered shutdown hook!

        NeoServer neoServer = serverBootstrapper.getServer();

        System.out.println("Press ENTER to quit.");
        System.in.read();

        neoServer.stop();
    }
}