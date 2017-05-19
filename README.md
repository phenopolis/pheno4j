[![Build Status](https://travis-ci.org/phenopolis/pheno4j.svg?branch=master)](https://travis-ci.org/phenopolis/pheno4j)
[![Coverage Status](https://coveralls.io/repos/github/phenopolis/pheno4j/badge.svg?branch=master)](https://coveralls.io/github/phenopolis/pheno4j?branch=master)
<!-- Sajid fix this :-)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.graph%3Adb)](https://sonarqube.com/dashboard/index/com.graph%3Adb)
-->

# Pheno4j: a graph based HPO to NGS database

## Purpose
Genetic and phenotype data in JSON, VCF and CSV format and convert them into CSV files that represent Nodes and Relationships that can then be used to populate Pheno4J using [the neo4j bulk CSV import tool](https://neo4j.com/docs/operations-manual/current/tutorial/import-tool/).

## Public datasets
Only two publicly available datasets required:
* [Human Phenotype Ontology](http://purl.obolibrary.org/obo/hp.obo)
* [OMIM HPO-Gene mapping](http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_diseases_to_genes_to_phenotypes.txt)

## User specified datasets

Example datasets specified in [config.properties](https://github.com/phenopolis/pheno4j/blob/master/src/main/resources/config.properties):
* VCF file which contains genotypes ([example](https://github.com/phenopolis/pheno4j/blob/master/src/test/resources/genotypes.vcf))
* VEP JSON file ([example](https://github.com/phenopolis/pheno4j/blob/master/src/test/resources/VEP.json))
* Individuals with HPO terms as CSV file ([example](https://github.com/phenopolis/pheno4j/blob/master/src/test/resources/person_phenotypes.csv))

## Pheno4J schema overview

![](https://github.com/sajid-mughal/pheno4j/blob/master/docs/Figure_1.png?raw=true)

## Installation
### Local Installation with Exemplar Data

The local version will not be able to handle efficiently a very large dataset since it does not have access to the configuration for the page cache and jvm size.
Hence it should be used for testing.

#### Prerequisites 
- Java 1.8
- Maven 3

#### Build Graph and Start up Neo4j on test data ###
Download the code, build the database, load the test data referenced in [config.properties](https://github.com/phenopolis/pheno4j/blob/master/src/main/resources/config.properties) and start the server on port 7474:

```
git clone https://github.com/phenopolis/pheno4j.git
cd pheno4j
mvn clean compile -P build-graph,run-neo4j
```

Once the server is running, it can be queried either by going to the web interface on http://localhost:7474/ or using [curl](https://curl.haxx.se/)
to do http requests from the command line (see next section).

#### Run Example Queries with curl
The curl http queries return data in JSON format and so the response can be parsed using [jq](https://stedolan.github.io/jq/).

For example, get count of variants shared between person1 and person2:
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

More cypher queries are available [here](https://github.com/phenopolis/pheno4j/blob/master/docs/Cypher-Queries.md).

#### Running Pheno4J on your own data

[Documentation here](https://github.com/phenopolis/pheno4j/blob/master/docs/Additional-Documentation.md#loading-manually-created-files).

### Server Installation

The server installation can scale to very large datasets as it allows configuration of the JVM size and page cache.

#### Prerequisites
- Java 1.8
- Neo4j installation - download from https://neo4j.com/download/community-edition/, extract the archive. The location of the extract will be referred to as **$NEO4J_HOME**

#### Deploy code
Run the following in the checkout directory, which will generate a zip file, "graph-bundle.zip", in the target folder:
```
mvn clean package
```
Copy `graph-bundle.zip` to your target server and unzip it.

#### Update config file to reference your input data ###
In the `conf` folder of the extracted zip above, update [config.properties](https://github.com/phenopolis/pheno4j/blob/master/src/main/resources/config.properties) to reference your input data.

#### Run the GraphDatabaseBuilder ###
This step will take all the input data and build csv files, which are then built into a Neo4j database using their ImportTool.
Constraints and Indexes are then created.
In the lib folder of the extracted zip above, run the following:
```
java -cp *:../conf/ com.graph.db.GraphDatabaseBuilder
```
#### Link the generated database above to your Neo4j installation
```
cd $NEO4J_HOME/data/databases
ln -s ${output.folder}/graph-db/data/databases/graph.db graph.db 
```
${output.folder} is defined in [config.properties](https://github.com/phenopolis/pheno4j/blob/master/src/main/resources/config.properties)

### Update Neo4j config
Ideally you should hold as much of the data in memory as possible ([See here for more information](https://neo4j.com/docs/operations-manual/current/performance/))
Set the value of `dbms.memory.pagecache.size` in ${NEO4J_HOME}/conf/neo4j.conf to the size of the files: `NEO4J_HOME/data/databases/graph.db/*store.db*`

#### Start Neo4j
```
cd $NEO4J_HOME/bin
./neo4j start
```
#### Run 'warmup' query
This query will basically hit the entire graph, the result will be all the data stored on the disk will be loaded into memory. ([See here for more information](https://neo4j.com/developer/kb/warm-the-cache-to-improve-performance-from-cold-start/))
This takes up to 10 minutes for our data.
```
MATCH (n)
OPTIONAL MATCH (n)-[r]->()
RETURN count(n.prop) + count(r.prop);
```
#### Additional Steps
If you would like to connect to your instance from your application tier to handle incoming database requests, you can change the password to the Neo4j instance with the following; the port is the value of `dbms.connector.http.listen_address` in `$NEO4J_HOME/conf/neo4j.conf`.
The following command will the password to `1`:
```
curl -H "Content-Type: application/json" -X POST -d '{"password":"1"}' -u neo4j:neo4j http://**{HOST}**:**{PORT}**/user/neo4j/password
```

## Example Cypher Queries
Examples can be found [here](https://github.com/phenopolis/pheno4j/blob/master/docs/Cypher-Queries.md).

## Further reading
[Additional Documentation](docs/Additional-Documentation.md)
