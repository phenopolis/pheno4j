[![Build Status](https://travis-ci.org/phenopolis/pheno4j.svg?branch=master)](https://travis-ci.org/phenopolis/pheno4j)
[![Coverage Status](https://coveralls.io/repos/github/phenopolis/pheno4j/badge.svg?branch=master)](https://coveralls.io/github/phenopolis/pheno4j?branch=master)

[![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.graph%3Adb)](https://sonarqube.com/dashboard/index/com.graph%3Adb)

# Pheno4j: a graph based HPO to NGS database

# Purpose
Take raw input files and convert them into csv files that represent Nodes and Relationships, which can then be used to populate neo4j using their bulk csv import tool.

# Public datasets

| | Description | URL |
| --- | --- | --- |
| 1 | Gencode gene-transcript | [ftp://ftp.sanger.ac.uk/pub/gencode/Gencode_human/release_25/GRCh37_mapping/gencode.v25lift37.annotation.gtf.gz](ftp://ftp.sanger.ac.uk/pub/gencode/Gencode_human/release_25/GRCh37_mapping/gencode.v25lift37.annotation.gtf.gz)| 
| 2 | HPO ontology | [http://purl.obolibrary.org/obo/hp.obo](http://purl.obolibrary.org/obo/hp.obo)|
| 3 | HPO-Gene | [http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_diseases_to_genes_to_phenotypes.txt](http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_diseases_to_genes_to_phenotypes.txt) |

# Domain Model
The following neo4j schema is produced from the imported data:
![](https://github.com/sajid-mughal/pheno4j/blob/master/docs/complete_diagram.png?raw=true)

# Installation
## Local Installation ##
### Prerequisites ###
- Java 1.8
- Maven 3

### Build Graph and Start up Neo4j on test data ###
This will build the database and load the test data referenced in (config.properties)[https://github.com/phenopolis/pheno4j/blob/master/src/main/resources/config.properties].

First clone the repository:
```
git clone
```
Then run the following in the checkout directory:
```
mvn clean compile -P build-graph,run-neo4j
```
This should load the test data and start the server on port 7474.
Once the server is running, it can be queried either by going to the web interface on http://localhost:7474/ or using `curl`
to do http requests from the command line (see below).

### Run Example Queries with curl
The curl http queries return data in json format and so the response can be parsed using (jq)[https://stedolan.github.io/jq/]
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
These can be run in the browser interface or via the neo4j-shell ([See here for more information](http://neo4j.com/docs/operations-manual/current/tools/cypher-shell/))
## All Variants for an individual
```
MATCH (gv:GeneticVariant)-[]->(p:Person)
WHERE p.personId ='person1'
RETURN count(gv);
```
## Individuals who have a particular variant
```
MATCH (gv:GeneticVariant)-[]->(p:Person)
WHERE gv.variantId ='22-51171497-G-A'
RETURN p.personId;
```
## Find Variants shared by a list of Individuals
```
WITH ["person1","person2"] as persons
MATCH (p:Person)<-[]-(v:GeneticVariant) 
WHERE p.personId IN persons
WITH v, count(*) as c, persons
WHERE c = size(persons)
RETURN count(v.variantId);
```
## Rank Shared Variants by occurences in all Individuals
```
WITH ["person1","person2"] as persons
MATCH (p:Person)<-[]-(v:GeneticVariant) 
WHERE p.personId IN persons
and v.allele_freq < 0.01
WITH v, count(*) as c, persons
WHERE c = size(persons)
with v
return v.variantId, size((v)-[]-())  as count
ORDER BY count asc
LIMIT 10;
```
## Find Variants shared by a list of Individuals, that no one else has
```
WITH ["person1","person2"] as individuals
MATCH (p:Person)<-[]-(v:GeneticVariant) 
WHERE p.personId IN individuals
WITH v, count(*) as c, individuals
WHERE c = size(individuals)
with v, individuals
MATCH (v:GeneticVariant)
where size((v)-[]-()) = size(individuals)
RETURN v.variantId;
```
## Individuals who have a Term
```
MATCH (t:Term)<-[tp:PersonToObservedTerm]-(p:Person)
WHERE t.termId = 'HP:0000505'
RETURN p.personId;
```
## Find Genes that have no Term
```
MATCH (s:Gene)
WHERE NOT (s)-[:GeneToTerm]->()
RETURN count(s.gene_id);
```
## Find Variants which have a frequency less than 0.001 and a CADD score greater than 20
```
MATCH (gv:GeneticVariant)
WHERE gv.cadd > 20
AND gv.allele_freq < 0.001
RETURN count(gv);
```
## For a Term, find all the Descendant Terms
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)
where p.termId ='HP:0000505'
return q
```
## Find all Individuals with a specific Term (and any of its descendants)
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:PersonToObservedTerm]-(r:Person)
WHERE p.termId ='HP:0000505'
RETURN count(r);
```
## Find variants which have a frequency less than 0.001 and a CADD score greater than 20 seen in in people with HP:0000556 and belonging to a gene with HP:0000556
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:GeneToTerm]-(gs:Gene)
WHERE p.termId ='HP:0000556'
WITH distinct gs
MATCH (gs)-[:GeneToGeneticVariant]->(gv:GeneticVariant)
WHERE gv.allele_freq < 0.001 
and gv.cadd > 20 
WITH distinct gv, gs
MATCH (r:Person)<-[]-(gv)
WITH distinct gv, gs, r
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:PersonToObservedTerm]-(r)
WHERE p.termId ='HP:0000556'
RETURN distinct r.personId, gv.variantId, gs.gene_name
ORDER BY r.personId asc;
```
### For an Individual, rank their variants by the number of occurrences in other Individuals
```
MATCH (p:Person {personId:"person1"})<-[]-(gv:GeneticVariant)-[:GeneticVariantToTranscriptVariant]->(tv:TranscriptVariant)
WHERE (gv.allele_freq < 0.1 or NOT EXISTS(gv.exac_af))
WITH size(()<-[]-(gv)) as count, gv
WHERE count > 1 
RETURN gv.variantId, size(()<-[]-(gv)) as count
ORDER BY count asc
LIMIT 10;
```
### For a particular individual, show a list of 10 top individuals in decreasing order by the number of variants they share with the given individual, with a frequency less than 0.001 or NA in ExAC, and that appear in less than 5% of individuals
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"WebsterURMD_Sample_GV4344"})<-[]-(gv:GeneticVariant)
WHERE (gv.allele_freq < 0.001 or NOT EXISTS(gv.exac_af))
WITH size(()<-[]-(gv)) as count , gv, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (gv)-[]->(q:Person)
WHERE p <> q
WITH p,q,count(gv) as c
ORDER BY c desc LIMIT 10
RETURN p.personId,q.personId, c;
```
### As above, but show the as a percentage the common variants (i.e. the intersection) over the shared variants (the union)
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"person1"})<-[]-(gv:GeneticVariant)
WHERE (gv.allele_freq < 0.001 or NOT EXISTS(gv.exac_af))
WITH size(()<-[]-(gv)) as count , gv, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (gv)-[]->(q:Person)
WHERE p <> q
WITH p,q,count(gv) as intersection, numberOfPeople
ORDER BY intersection DESC limit 1
MATCH (x:Person)<-[]-(v:GeneticVariant)
WHERE (x.personId = p.personId or x.personId = q.personId)
AND (v.allele_freq < 0.001 or NOT EXISTS(v.exac_af))
AND ((size(()<-[]-(v)) / toFloat(numberOfPeople))  <= 0.05)
WITH p, q, v, intersection
RETURN p.personId, q.personId, intersection, size(collect(distinct v)) as unionSum, (round((intersection/toFloat(size(collect(distinct v))))*100.0*10)/10) as PercentShared
ORDER BY PercentShared DESC;
```
### Get rare damaging variants in Gene TTLL5
```
MATCH (gs:Gene {gene_name:"TTLL5"})-[:GeneToGeneticVariant]->(gv:GeneticVariant)
WHERE gv.allele_freq < 0.001 
AND gv.cadd > 20 
WITH distinct gv
MATCH (gv)-[]->(p:Person)-[:PersonToObservedTerm]-(t:Term)
return gv.variantId, p.personId, t.termId, t.name;
```
# Further reading
[Additional Documentation](docs/Additional-Documentation.md)
