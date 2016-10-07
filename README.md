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
| Nodes            | Relationships             |
| ---------------- |:-------------------------:|
| Person           | VariantToPerson           |
| Variant          |                           |
### AnnotationParser
| Nodes            | Relationships             |
| ---------------- |:-------------------------:|
| AnnotatedGene    | AnnotatedGeneToVariant    |
| AnnotatedVariant | VariantToAnnotatedVariant |
|                  | GeneToAnnotatedGene       |
### GeneParser
| Nodes | Relationships |
| --- |:---:|
| Gene | GeneToTerm |
### PersonParser
| Nodes | Relationships |
| --- |:---:|
|  | PersonToObservedTerm |
|  | PersonToNonObservedTerm |
|  | PersonToGene |
### TermParser
| Nodes | Relationships |
| --- |:---:|
| Term | TermToTerm |





Command to load into neo4j
Example cypher queries

