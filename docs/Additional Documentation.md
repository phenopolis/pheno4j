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
bin/neo4j-import  --into /generatedGraphOutputFolder/graph.db --id-type string --bad-tolerance 1000000 --skip-bad-relationships true --skip-duplicate-nodes true \
--nodes:GeneticVariant /folder/GeneticVariant-header.csv,/folder/GeneticVariant-AnnotationParser.csv \
--nodes:Gene /folder/Gene-header.csv,/folder/Gene.csv \
--nodes:Person /folder/Person-header.csv,/folder/Person.csv \
--nodes:Term /folder/Term-header.csv,/folder/Term-TermParser.csv \
--nodes:TranscriptVariant /folder/TranscriptVariant-header.csv,/folder/TranscriptVariant-AnnotationParser.csv \
--nodes:Transcript /folder/Transcript-header.csv,/folder/Transcript.csv \
--nodes:ConsequenceTerm /folder/ConsequenceTerm-header.csv,/folder/ConsequenceTerm-AnnotationParser.csv \
--relationships:TermToParentTerm /folder/TermToParentTerm-header.csv,/folder/TermToParentTerm-TermParser.csv \
--relationships:TermToDescendantTerms /folder/TermToDescendantTerms-header.csv,/folder/TermToDescendantTerms-TermParser.csv \
--relationships:PersonToObservedTerm /folder/PersonToObservedTerm-header.csv,/folder/PersonToObservedTerm-PersonParser.csv \
--relationships:PersonToNonObservedTerm /folder/PersonToNonObservedTerm-header.csv,/folder/PersonToNonObservedTerm-PersonParser.csv \
--relationships:GeneticVariantToPerson /folder/GeneticVariantToPerson-header.csv,/folder/GeneticVariantToPerson-VcfParser.csv \
--relationships:GeneToGeneticVariant /folder/GeneToGeneticVariant-header.csv,/folder/GeneToGeneticVariant-AnnotationParser.csv \
--relationships:GeneticVariantToTranscriptVariant /folder/GeneticVariantToTranscriptVariant-header.csv,/folder/GeneticVariantToTranscriptVariant-AnnotationParser.csv \
--relationships:TranscriptToTranscriptVariant /folder/TranscriptToTranscriptVariant-header.csv,/folder/TranscriptToTranscriptVariant-AnnotationParser.csv \
--relationships:TranscriptVariantToConsequenceTerm /folder/TranscriptVariantToConsequenceTerm-header.csv,/folder/TranscriptVariantToConsequenceTerm-AnnotationParser.csv \
--relationships:GeneToTerm /folder/GeneToTerm-header.csv,/folder/GeneToTerm-GeneParser.csv \
--relationships:TranscriptToGene /folder/TranscriptToGene-header.csv,/folder/TranscriptToGene-TranscriptParser.csv \
> /folder/neo4j-log.txt &
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