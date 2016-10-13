# a

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
```
java -jar graph-db.jar VcfParser $vcfFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| Person  | VariantToPerson |
| Variant |  |
### AnnotationParser
```
java -jar graph-db.jar AnnotationParser $inputFolderWithJsonFiles $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| GeneId | GeneIdToVariant |
| GeneSymbol | VariantToAnnotatedVariant |
| AnnotatedVariant | GeneSymbolToGeneId |
### GeneParser
```
java -jar graph-db.jar GeneParser $geneFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| GeneSymbol | GeneSymbolToTerm |
### PersonParser
```
java -jar graph-db.jar PersonParser $personFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| GeneSymbol | PersonToObservedTerm |
|  | PersonToNonObservedTerm |
|  | PersonToGeneSymbol |
### TermParser
```
java -jar graph-db.jar TermParser $termFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| Term | TermToTerm |
# Running the neo4j Bulk Csv Import Tool
1. generate the database using the csv's
```
bin/neo4j-import   --into /generatedGraphOutputFolder/graph.db --id-type string --bad-tolerance 50000  --skip-bad-relationships true \
--nodes:Person /folder/Person-header.csv,/folder/Person-mainset_July2016.csv \
--nodes:Variant /folder/Variant-header.csv,/folder/Variant-mainset_July2016.csv \
--nodes:AnnotatedGene /folder/AnnotatedGene-header.csv,/folder/AnnotatedGene.csv \
--nodes:AnnotatedVariant /folder/AnnotatedVariant-header.csv,/folder/AnnotatedVariant.csv \
--nodes:Term /folder/Term-header.csv,/folder/Term.csv \
--nodes:Gene /folder/Gene-header.csv,/folder/Gene.csv \
--relationships:PRESENT_IN /folder/VariantToPerson-header.csv,/folder/VariantToPerson-mainset_July2016.csv \
--relationships:HAS_ANNOTATION /folder/GeneToAnnotatedGene-header.csv,/folder/GeneToAnnotatedGene.csv \
--relationships:HAS_ANNOTATION /folder/VariantToAnnotatedVariant-header.csv,/folder/VariantToAnnotatedVariant.csv \
--relationships:HAS_VARIANT /folder/AnnotatedGeneToVariant-header.csv,/folder/AnnotatedGeneToVariant.csv \
--relationships:INFLUENCES /folder/GeneToTerm-header.csv,/folder/GeneToTerm.csv \
--relationships:IS_A /folder/TermToTerm-header.csv,/folder/TermToTerm.csv \
--relationships:HAS_GENE /folder/PersonToGene-header.csv,/folder/PersonToGene.csv \
--relationships:HAS_NON_OBSERVED_TERM /folder/PersonToNonObservedTerm-header.csv,/folder/PersonToNonObservedTerm.csv \
--relationships:HAS_OBSERVED_TERM /folder/PersonToObservedTerm-header.csv,/folder/PersonToObservedTerm.csv > /folder/neo4j-log.txt &
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
//TODO
```
4. Create the constraints
```
CREATE CONSTRAINT ON (p:Variant) ASSERT p.variantId IS UNIQUE;
CREATE CONSTRAINT ON (p:Person) ASSERT p.personId IS UNIQUE;
CREATE CONSTRAINT ON (p:AnnotatedGene) ASSERT p.geneId IS UNIQUE;
CREATE CONSTRAINT ON (p:AnnotatedVariant) ASSERT p.variantId IS UNIQUE;
CREATE CONSTRAINT ON (p:Term) ASSERT p.termId IS UNIQUE;
CREATE CONSTRAINT ON (p:geneSymbol) ASSERT p.termId IS UNIQUE;
```
# Example Cypher Queries
## All Variants for an individual
```
MATCH (root:Variant)-[rels:PRESENT_IN]->(child:Person)
WHERE child.personId ='XXX'
RETURN count(root.variantId);
```
## Individuals who have a particular variant
```
MATCH (root:Variant)-[rels*]->(child:Person)
WHERE root.variantId ='xxx'
RETURN child.personId;
```
## Find Variants shared by a list of Individuals
```
WITH ["XXX","yyy"] as persons
MATCH (p:Person)<-[:PRESENT_IN]-(v:Variant) 
WHERE p.personId IN persons
WITH v, count(*) as c, persons
WHERE c = size(persons)
RETURN count( v.variantId);
```
## Find Variants shared by a list of Individuals, that no one else has
```
WITH ["XXX","YYY"] as individuals
MATCH (p:Person)<-[:PRESENT_IN]-(v:Variant) 
WHERE p.personId IN individuals
WITH v, count(*) as c, individuals
WHERE c = size(individuals)
with v, individuals
MATCH (v:Variant)
where size((v)-[:PRESENT_IN]-()) = size(individuals)
RETURN v.variantId;
```
## Individuals who have a Term
```
MATCH (t:Term)<-[tp:HAS_OBSERVED_TERM]-(p:Person)
WHERE t.termId = 'HP:XXX'
return p.personId;
``` 