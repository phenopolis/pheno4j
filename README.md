[![Build Status](https://travis-ci.org/sajid-mughal/pheno4j.svg?branch=master)](https://travis-ci.org/sajid-mughal/pheno4j)
[![Coverage Status](https://coveralls.io/repos/github/sajid-mughal/pheno4j/badge.svg?branch=master)](https://coveralls.io/github/sajid-mughal/pheno4j?branch=master)
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
Once loaded into neo4j, the following schema is produced:
![](https://github.com/sajid-mughal/a/blob/master/complete_diagram.png?raw=true)

# Installation
## Local Installation ##
### Prerequisites ###
- Java 1.8
- Maven 3

### Build Graph and Start up Neo4j ###
Run the following, and then browse to http://localhost:7474/
```
mvn clean compile -P build-graph,run-neo4j
```
## Server Installation ##
### Prerequisites ###
- Java 1.8
- Neo4j installation - download from https://neo4j.com/download/community-edition/, extract the archive

### Deploy code ###
Run the following, which will generate a zip file, "graph-bundle.zip", in the target folder. Deploy this to your target server and extract it.
```
mvn clean package
```
### Update config file to reference your input data ###
In the conf folder of the extracted zip above, update config.properties to reference your input data.
### Run the GraphDatabaseBuilder ###
//TODO
### Link the generated database above to your Neo4j installation #
//TODO validate the path
```
cd $NEO4J_HOME/data/databases
ln -s /generatedGraphOutputFolder/graph.db graph.db 
```
### Update Neo4j config ###
//TODO the properties file to load the data into memory
### Start Neo4j ###
```
cd $NEO4J_HOME/bin
./neo4j start
```
### Run 'warmup' query ###
This query will basically hit the entire graph, the result will be all the data stored on the disk will be loaded into memory.
```
MATCH (a)-[b]-(c)
with count(b) as count
RETURN count;
```
### Create the constraints and indexes ###
```
CREATE CONSTRAINT ON (p:Term) ASSERT p.termId IS UNIQUE;
CREATE CONSTRAINT ON (p:Person) ASSERT p.personId IS UNIQUE;
CREATE CONSTRAINT ON (p:GeneticVariant) ASSERT p.variantId IS UNIQUE;
CREATE CONSTRAINT ON (p:Gene) ASSERT p.gene_id IS UNIQUE;
CREATE CONSTRAINT ON (p:TranscriptVariant) ASSERT p.hgvsc IS UNIQUE;
CREATE CONSTRAINT ON (p:Transcript) ASSERT p.transcript_id IS UNIQUE;
CREATE CONSTRAINT ON (p:ConsequenceTerm) ASSERT p.consequenceTerm IS UNIQUE;

CREATE INDEX ON :GeneticVariant(allele_freq);
CREATE INDEX ON :TranscriptVariant(cadd);
CREATE INDEX ON :GeneticVariant(hasExac);
```

# Further reading
[Additional Documentation](docs/Additional Documentation.md)

# Example Cypher Queries
## All Variants for an individual
```
MATCH (gv:GeneticVariant)-[:GeneticVariantToPerson]->(p:Person)
WHERE p.personId ='person1'
RETURN count(gv.variantId);
```
## Individuals who have a particular variant
```
MATCH (gv:GeneticVariant)-[:GeneticVariantToPerson]->(p:Person)
WHERE gv.variantId ='22-51171497-G-A'
RETURN p.personId;
```
## Find Variants shared by a list of Individuals
```
WITH ["person1","person2"] as persons
MATCH (p:Person)<-[:GeneticVariantToPerson]-(v:GeneticVariant) 
WHERE p.personId IN persons
WITH v, count(*) as c, persons
WHERE c = size(persons)
RETURN count(v.variantId);
```
## Find Variants shared by a list of Individuals, that no one else has
```
WITH ["person1","person2"] as individuals
MATCH (p:Person)<-[:GeneticVariantToPerson]-(v:GeneticVariant) 
WHERE p.personId IN individuals
WITH v, count(*) as c, individuals
WHERE c = size(individuals)
with v, individuals
MATCH (v:GeneticVariant)
where size((v)-[:GeneticVariantToPerson]-()) = size(individuals)
RETURN v.variantId;
```
## Individuals who have a Term
```
MATCH (t:Term)<-[tp:PersonToObservedTerm]-(p:Person)
WHERE t.termId = 	'HP:0000505'
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
MATCH (n:GeneticVariant)-[:GeneticVariantToTranscriptVariant]->(tv:TranscriptVariant)
WHERE n.allele_freq < 0.001 and tv.cadd > 20 
RETURN count(*);
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
//TODO needs validation, also slow (was 17 seconds, now 13 with new relationship)
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:GeneToTerm]-(gs:Gene)-[:GeneToGeneticVariant]->(gv:GeneticVariant)-[:GeneticVariantToTranscriptVariant]->(ts:TranscriptVariant)
WHERE p.termId ='HP:0000556'
AND gv.allele_freq < 0.001 
AND ts.cadd > 20 
RETURN count(distinct gv.variantId);
```
### For an Individual, rank their variants by the number of occurrences in other Individuals
```
MATCH (p:Person {personId:"XXX"})<-[:GeneticVariantToPerson]-(gv:GeneticVariant)-[:GeneticVariantToTranscriptVariant]->(tv:TranscriptVariant)
WHERE (gv.allele_freq < 0.1 or tv.hasExac = false)
WITH size(()<-[:GeneticVariantToPerson]-(gv)) as count, gv
WHERE count > 1 
RETURN gv.variantId, size(()<-[:GeneticVariantToPerson]-(gv)) as count
ORDER BY count asc
LIMIT 10;
```
### For a particular individual, show a list of individuals in decreasing order by the number of variants they share with the given individual, with a frequency less than 0.001 or NA in ExAC, and that appear in less than 5% of individuals
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"person1"})<-[:GeneticVariantToPerson]-(gv:GeneticVariant)
WHERE (gv.allele_freq < 0.001 or gv.hasExac = false)
WITH size(()<-[:GeneticVariantToPerson]-(gv)) as count , gv, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (gv)-[:GeneticVariantToPerson]->(q:Person)
WHERE p <> q
WITH p,q,count(gv) as c
ORDER BY c desc LIMIT 10
RETURN p.personId,q.personId, c
```
### As above, but show the as a percentage the common variants (i.e. the intersection) over the shared variants (the union)
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"person1"})<-[:GeneticVariantToPerson]-(gv:GeneticVariant)
WHERE (gv.allele_freq < 0.001 or gv.hasExac = false)
WITH size(()<-[:GeneticVariantToPerson]-(gv)) as count , gv, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (gv)-[:GeneticVariantToPerson]->(q:Person)
WHERE p <> q
WITH p,q,count(gv) as intersection, numberOfPeople
ORDER BY intersection DESC limit 1
MATCH (x:Person)<-[:GeneticVariantToPerson]-(v:GeneticVariant)
WHERE (x.personId = p.personId or x.personId = q.personId)
AND (v.allele_freq < 0.001 or v.hasExac = false)
AND ((size(()<-[:GeneticVariantToPerson]-(v)) / toFloat(numberOfPeople))  <= 0.05)
WITH p, q, v, intersection
RETURN p.personId, q.personId, intersection, size(collect(distinct v)) as unionSum, (round((intersection/toFloat(size(collect(distinct v))))*100.0*10)/10) as PercentShared
ORDER BY PercentShared DESC;
```
