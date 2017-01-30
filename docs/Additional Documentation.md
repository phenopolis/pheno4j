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