[![Build Status](https://travis-ci.org/phenopolis/pheno4j.svg?branch=master)](https://travis-ci.org/phenopolis/pheno4j)
[![Coverage Status](https://coveralls.io/repos/github/phenopolis/pheno4j/badge.svg?branch=master)](https://coveralls.io/github/phenopolis/pheno4j?branch=master)
<!-- Sajid fix this :-)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.graph%3Adb)](https://sonarqube.com/dashboard/index/com.graph%3Adb)
-->

# Pheno4j: a graph based HPO to NGS database

# Purpose
Take raw input files in JSON, VCF and CSV format and convert them into CSV files that represent Nodes and Relationships, which can then be used to populate Pheno4J using the neo4j bulk CSV import tool.

# Public datasets

| Description | URL |
| --- | --- |
| Gencode gene-transcript | [ftp://ftp.sanger.ac.uk/pub/gencode/Gencode_human/release_25/GRCh37_mapping/gencode.v25lift37.annotation.gtf.gz](ftp://ftp.sanger.ac.uk/pub/gencode/Gencode_human/release_25/GRCh37_mapping/gencode.v25lift37.annotation.gtf.gz)| 
| HPO ontology | [http://purl.obolibrary.org/obo/hp.obo](http://purl.obolibrary.org/obo/hp.obo)|
| HPO-Gene | [http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_diseases_to_genes_to_phenotypes.txt](http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_diseases_to_genes_to_phenotypes.txt) |

# User specified datasets

Example datasets.

| Description | URL |
| --- | --- |
| VCF file which contains genotypes | https://github.com/phenopolis/pheno4j/blob/master/src/test/resources/genotypes.vcf |
| VEP JSON file | https://github.com/phenopolis/pheno4j/blob/master/src/test/resources/VEP.json |
| Individuals with HPO terms as CSV file | https://github.com/phenopolis/pheno4j/blob/master/src/test/resources/person_phenotypes.csv |

# Pheno4J schema overview

![](https://github.com/sajid-mughal/pheno4j/blob/master/docs/complete_diagram.png?raw=true)

# Installation
## Local Installation ##
### Prerequisites ###
- Java 1.8
- Maven 3

### Build Graph and Start up Neo4j on test data ###
This will build the database and load the test data referenced in [config.properties](https://github.com/phenopolis/pheno4j/blob/master/src/main/resources/config.properties).

First clone the repository:
```
git clone https://github.com/phenopolis/pheno4j.git
```
Then run the following in the checkout directory:
```
mvn clean compile -P build-graph,run-neo4j
```
This should load the test data and start the server on port 7474.
Once the server is running, it can be queried either by going to the web interface on http://localhost:7474/ or using `curl`
to do http requests from the command line (see below).

### Run Example Queries with curl
The curl http queries return data in JSON format and so the response can be parsed using [jq](https://stedolan.github.io/jq/)
Get count of variants shared between person1 and person2:
```
curl -H "Content-Type: application/json" -d '{
"query": "WITH [$p1,$p2] AS persons MATCH (p:Person)<-[]-(v:GeneticVariant) WHERE p.personId IN persons WITH v, count(*) as c, persons WHERE c = size(persons) RETURN count(v.variantId);",
"params":{"p1":"person1","p2":"person2"}
}' http://localhost:7474/db/data/cypher
```
Get ids of persons with variant 22-51171497-G-A:
```
curl -H "Content-Type: application/json" -d '{
"query": "MATCH (gv:GeneticVariant)-[]->(p:Person) WHERE gv.variantId =$var RETURN p.personId;",
"params":{"var":"22-51171497-G-A"}
}' http://localhost:7474/db/data/cypher
```

### Running Pheno4J on your own data

[Documentation here](https://github.com/phenopolis/pheno4j/blob/master/docs/Additional-Documentation.md#loading-manually-created-files).

## Server Installation ##

### Prerequisites ###
- Java 1.8
- Neo4j installation - download from https://neo4j.com/download/community-edition/, extract the archive. The location of the extract will be referred to as **$NEO4J_HOME**

### Deploy code ###
Run the following in the checkout directory, which will generate a zip file, "graph-bundle.zip", in the target folder. Deploy this to your target server and extract it.
```
mvn clean package
```
### Update config file to reference your input data ###
In the conf folder of the extracted zip above, update config.properties to reference your input data.
### Run the GraphDatabaseBuilder ###
This step will take all the input data and build csv files, which are then built into a Neo4j database using their ImportTool. Constraints and Indexes are then created.
In the lib folder of the extracted zip above, run the following:
```
java -cp *:../conf/ com.graph.db.GraphDatabaseBuilder
```
### Link the generated database above to your Neo4j installation #
```
cd $NEO4J_HOME/data/databases
ln -s ${output.folder}/graph-db/data/databases/graph.db graph.db 
```
${output.folder} is defined in config.properties
### Update Neo4j config ###
Ideally you should hold as much of the data in memory as possible ([See here for more information](https://neo4j.com/docs/operations-manual/current/performance/))
Set the value of `dbms.memory.pagecache.size` in ${NEO4J_HOME}/conf/neo4j.conf to the size of the files: `NEO4J_HOME/data/databases/graph.db/*store.db*`
### Start Neo4j ###
```
cd $NEO4J_HOME/bin
./neo4j start
```
### Run 'warmup' query ###
This query will basically hit the entire graph, the result will be all the data stored on the disk will be loaded into memory. ([See here for more information](https://neo4j.com/developer/kb/warm-the-cache-to-improve-performance-from-cold-start/))
This takes up to 10 minutes for our data.
```
MATCH (n)
OPTIONAL MATCH (n)-[r]->()
RETURN count(n.prop) + count(r.prop);
```
### Additional Steps ###
If you would like to connect to your instance from your application tier, you can change the password to the Neo4j instance with the following; the port is the value of "dbms.connector.http.listen_address" in $NEO4J_HOME/conf/neo4j.conf, the password with the following will be set to "1".
```
curl -H "Content-Type: application/json" -X POST -d '{"password":"1"}' -u neo4j:neo4j http://**{HOST}**:**{PORT}**/user/neo4j/password
```

# Example Cypher Queries

Examples can be found [here](https://github.com/phenopolis/pheno4j/blob/master/docs/Cypher-Queries.md).

# Further reading
[Additional Documentation](docs/Additional-Documentation.md)
