# Pheno4j: a graph based HPO to NGS database

# Public datasets

| | Description | URL |
| --- | --- | --- |
| 1 | Gencode gene-transcript | [ftp://ftp.sanger.ac.uk/pub/gencode/Gencode_human/release_25/GRCh37_mapping/gencode.v25lift37.annotation.gtf.gz](ftp://ftp.sanger.ac.uk/pub/gencode/Gencode_human/release_25/GRCh37_mapping/gencode.v25lift37.annotation.gtf.gz)| 
| 2 | HPO ontology | [http://purl.obolibrary.org/obo/hp.obo](http://purl.obolibrary.org/obo/hp.obo)|
| 3 | HPO-Gene | [http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_diseases_to_genes_to_phenotypes.txt](http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_diseases_to_genes_to_phenotypes.txt) |
| 4 | Tissue-Transcript | [http://www.gtexportal.org/home/](http://www.gtexportal.org/home/) |

# How to run the program?

# Purpose
Take raw input files and convert them into csv files that represent Nodes and Relationships, which can then be used to populate neo4j using their bulk csv import tool.
# Domain Model
Once loaded into neo4j, the following schema is produced:
![](https://github.com/sajid-mughal/a/blob/master/schema%20diagram.png?raw=true)
# Project Structure
The project consists of 5 parsers, each one takes two parameters, a specific file or a folder (depending on the parser) and an output directory. 
The class Dispatcher is the entry point for execution, it takes as a parameter the name of the parser, and the two parameters mentioned above. 
# Packaging
Run `mvn clean package`; this will generate graph-db.jar in the /target folder
# Parsers
Below is the list of the Parsers, and the Nodes and Relationships that each one is responsible for producing. 
### VcfParser
This parses the genotype VCF file containing the variant to individual relationships:
```
java -jar graph-db.jar VcfParser $vcfFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| Person  | GeneticVariantToPerson |
### AnnotationParser
This parses the annotation file produced by the Variant Effect Predictor in the JSON format (VCF format to be supported soon):
```
java -jar graph-db.jar AnnotationParser $inputFolderWithJsonFiles $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| GeneticVariant | GeneToGeneticVariant |
| TranscriptVariant | GeneticVariantToTranscriptVariant |
| ConsequenceTerm | TranscriptToTranscriptVariant |
| Transcript | TranscriptVariantToConsequenceTerm |
| Gene | |
### GeneParser
This parses the OMIM-HPO file which links genes to the HPO terms to which they are associate:
```
java -jar graph-db.jar GeneParser $geneFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| | GeneToTerm |

### PersonParser
This parses the phenotype file which links individuals to their HPO terms:
```
java -jar graph-db.jar PersonParser $personFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| GeneSymbol | PersonToObservedTerm |
|  | PersonToNonObservedTerm |
### HPOTermParser
This loads the HPO ontology which links HPO terms to other HPO terms with "is a" relationships.
```
java -jar graph-db.jar TermParser $termFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| Term | TermToTerm |
### TranscriptParser
```
java -jar graph-db.jar TranscriptParser $inputFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| Transcript | TranscriptToGene |
| Gene | |
# Running the neo4j Bulk Csv Import Tool
1. generate the database using the csv's
```
bin/neo4j-import   --into /generatedGraphOutputFolder/graph.db --id-type string --bad-tolerance 1000000  --skip-bad-relationships true --skip-duplicate-nodes true  \
--nodes:GeneticVariant /folder/GeneticVariant-header.csv,/folder/GeneticVariant-AnnotationParser.csv \
--nodes:Gene /folder/Gene-header.csv,/folder/Gene-TranscriptParser.csv \
--nodes:Person /folder/Person-header.csv,/folder/Person-mainset_July2016-VcfParser.csv \
--nodes:Term /folder/Term-header.csv,/folder/Term-TermParser.csv \
--nodes:TranscriptVariant /folder/TranscriptVariant-header.csv,/folder/TranscriptVariant-AnnotationParser.csv \
--nodes:Transcript /folder/Transcript-header.csv,/folder/Transcript-TranscriptParser.csv \
--nodes:ConsequenceTerm /folder/ConsequenceTerm-header.csv,/folder/ConsequenceTerm-AnnotationParser.csv \
--relationships:HAS_VARIANT /folder/GeneToGeneticVariant-header.csv,/folder/GeneToGeneticVariant-AnnotationParser.csv \
--relationships:INFLUENCES /folder/GeneToTerm-header.csv,/folder/GeneToTerm-GeneParser.csv \
--relationships:IS_A /folder/TermToTerm-header.csv,/folder/TermToTerm-TermParser.csv \
--relationships:PRESENT_IN /folder/GeneticVariantToPerson-header.csv,/folder/GeneticVariantToPerson-mainset_July2016.csv \
--relationships:HAS /folder/GeneticVariantToTranscriptVariant-header.csv,/folder/GeneticVariantToTranscriptVariant-AnnotationParser.csv \
--relationships:HAS_NON_OBSERVED_TERM /folder/PersonToNonObservedTerm-header.csv,/folder/PersonToNonObservedTerm-PersonParser.csv \
--relationships:HAS_OBSERVED_TERM /folder/PersonToObservedTerm-header.csv,/folder/PersonToObservedTerm-PersonParser.csv \
--relationships:PRESENT_IN /folder/TranscriptToGene-header.csv,/folder/TranscriptToGene-TranscriptParser.csv \
--relationships:HAS_CONSEQUENCE /folder/TranscriptVariantToConsequenceTerm-header.csv,/folder/TranscriptVariantToConsequenceTerm-AnnotationParser.csv \
--relationships:HAS_VARIANT /folder/TranscriptToTranscriptVariant-header.csv,/folder/TranscriptToTranscriptVariant-AnnotationParser.csv  > /folder/neo4j-log.txt &

```
2. Create the symlink to the generated graph
```
cd $NEO4J_HOME/data/databases
ln -s /generatedGraphOutputFolder/graph.db graph.db 

```
3. Start neo4j
```
cd $NEO4J_HOME/bin
./neo4j start
```
1. Run 'warmup' query
This query will basically hit the entire graph, the result will be all the data stored on the disk will be loaded into memory.
```
MATCH (a)-[b]-(c)
with count(b) as count
RETURN count;
```
4. Create the constraints and indexes
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
# Example Cypher Queries
## All Variants for an individual
```
MATCH (gv:GeneticVariant)-[:PRESENT_IN]->(p:Person)
WHERE p.personId ='XXX'
RETURN count(gv.variantId);
```
## Individuals who have a particular variant
```
MATCH (gv:GeneticVariant)-[:PRESENT_IN]->(p:Person)
WHERE gv.variantId ='xxx'
RETURN p.personId;
```
## Find Variants shared by a list of Individuals
```
WITH ["XXX","yyy"] as persons
MATCH (p:Person)<-[:PRESENT_IN]-(v:GeneticVariant) 
WHERE p.personId IN persons
WITH v, count(*) as c, persons
WHERE c = size(persons)
RETURN count( v.variantId);
```
## Find Variants shared by a list of Individuals, that no one else has
```
WITH ["XXX","YYY"] as individuals
MATCH (p:Person)<-[:PRESENT_IN]-(v:GeneticVariant) 
WHERE p.personId IN individuals
WITH v, count(*) as c, individuals
WHERE c = size(individuals)
with v, individuals
MATCH (v:GeneticVariant)
where size((v)-[:PRESENT_IN]-()) = size(individuals)
RETURN v.variantId;
```
## Individuals who have a Term
```
MATCH (t:Term)<-[tp:HAS_OBSERVED_TERM]-(p:Person)
WHERE t.termId = 'HP:XXX'
RETURN p.personId;
```
## Find Genes that have no Term
```
MATCH (s:Gene)
WHERE NOT (s)-[:INFLUENCES]->()
RETURN s.gene_id;
```
## Find Variants which have a frequency less than 0.001 and a CADD score greater than 20
```
MATCH (n:GeneticVariant)-[:HAS]->(tv:TranscriptVariant)
WHERE n.allele_freq < 0.001 and tv.cadd > 20 
RETURN count(*);
```
## For a Term, find all the Descendant Terms
```
MATCH (p:Term)<-[:IS_A*]-(q:Term)
where p.termId ='XXX'
with  p + collect( distinct q) as allRows
unwind allRows as rows
return rows
```
## Find all Individuals with a specific Term (and any of its descendants)
```
MATCH (p:Term)<-[:IS_A*]-(q:Term)
WHERE p.termId ='XXX'
with  p + collect( distinct q) as allRows
MATCH (p:Person)-[:HAS_OBSERVED_TERM]->(t:Term) 
WHERE t IN allRows
RETURN count(p);
```
## Find variants which have a frequency less than 0.001 and a CADD score greater than 20 seen in in people with HP:0000556 and belonging to a gene with HP:0000556
```
//TODO needs validation, also slow (16 seconds)
MATCH (p:Term)<-[:IS_A*]-(q:Term)<-[:INFLUENCES]-(gs:Gene)-[:HAS_VARIANT]->(gv:GeneticVariant)-[:HAS]->(ts:TranscriptVariant)
WHERE (q.termId ='HP:0000556' or p.termId ='HP:0000556')
AND gv.allele_freq < 0.001 
AND ts.cadd > 20 
RETURN count(distinct gv.variantId);
```
### For an Individual, rank their variants by the number of occurrences in other Individuals
```
MATCH (p:Person {personId:"XXX"})<-[:PRESENT_IN]-(gv:GeneticVariant)-[:HAS]->(tv:TranscriptVariant)
WHERE (gv.allele_freq < 0.1 or tv.hasExac = false)
WITH size(()<-[:PRESENT_IN]-(gv)) as count , gv
WHERE count > 1 
RETURN gv.variantId, size(()<-[:PRESENT_IN]-(gv)) as count
ORDER BY count asc
LIMIT 10;
```
### For a particular individual, show a list of individuals in decreasing order by the number of variants they share with the given individual, with a frequency less than 0.001 or NA in ExAC, and that appear in less than 5% of individuals
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"XXX"})<-[:PRESENT_IN]-(gv:GeneticVariant)
WHERE (gv.allele_freq < 0.001 or gv.hasExac = false)
WITH size(()<-[:PRESENT_IN]-(gv)) as count , gv, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (gv)-[:PRESENT_IN]->(q:Person)
WHERE p <> q
WITH p,q,count(gv) as c
ORDER BY c desc LIMIT 10
RETURN p.personId,q.personId, c
```
### As above, but show the as a percentage the common variants (i.e. the intersection) over the shared variants (the union)
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"XXX"})<-[:PRESENT_IN]-(gv:GeneticVariant)
WHERE (gv.allele_freq < 0.001 or gv.hasExac = false)
WITH size(()<-[:PRESENT_IN]-(gv)) as count , gv, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (gv)-[:PRESENT_IN]->(q:Person)
WHERE p <> q
WITH p,q,count(gv) as intersection, numberOfPeople
ORDER BY intersection DESC limit 1
MATCH (x:Person)<-[:PRESENT_IN]-(v:GeneticVariant)
WHERE (x.personId = p.personId or x.personId = q.personId)
AND (v.allele_freq < 0.001 or v.hasExac = false)
AND ((size(()<-[:PRESENT_IN]-(v)) / toFloat(numberOfPeople))  <= 0.05)
WITH p, q, v, intersection
RETURN p.personId, q.personId, intersection, size(collect(distinct v)) as unionSum, (round((intersection/toFloat(size(collect(distinct v))))*100.0*10)/10) as PercentShared
ORDER BY PercentShared DESC;
```
