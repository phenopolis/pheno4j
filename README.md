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
| AnnotatedGene | AnnotatedGeneToVariant |
| AnnotatedVariant | VariantToAnnotatedVariant |
|  | GeneToAnnotatedGene |
### GeneParser
```
java -jar graph-db.jar GeneParser $geneFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| Gene | GeneToTerm |
### PersonParser
```
java -jar graph-db.jar PersonParser $personFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
|  | PersonToObservedTerm |
|  | PersonToNonObservedTerm |
|  | PersonToGene |
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
1. Start neo4j
```
cd $NEO4J_HOME/bin
./neo4j start
```
2. Create the constraints
```
CREATE CONSTRAINT ON (p:Variant) ASSERT p.variantId IS UNIQUE;
CREATE CONSTRAINT ON (p:Person) ASSERT p.personId IS UNIQUE;
CREATE CONSTRAINT ON (p:AnnotatedGene) ASSERT p.geneId IS UNIQUE;
CREATE CONSTRAINT ON (p:AnnotatedVariant) ASSERT p.variantId IS UNIQUE;
CREATE CONSTRAINT ON (p:Term) ASSERT p.termId IS UNIQUE;
CREATE CONSTRAINT ON (p:geneSymbol) ASSERT p.termId IS UNIQUE;
```


# Example Cypher Queries

