# Application Design #
- Files are read line-by-line using BufferedReader, as opposed to loading all the data into memory
- Use Guava's EventBus to decouple the reading of input data and the writing of output data
- Input beans are mapped to Output beans using reflection, and are written out using super-csv

# Execution #
To generate and run the Graph Database, a number of processes are run. Each process can be run standalone if needed, but the following two maven profiles are provided that call underlying Java wrappers for convenience.
- `build-graph` - Takes the input data and generates csv's that are used to build the graph database using neo4j's `org.neo4j.tooling.ImportTool`
- `run-neo4j` - Starts up the neo4j database, based one the graph built previously
![](https://github.com/sajid-mughal/pheno4j/blob/master/docs/class%20overview.png?raw=true)

# Project Structure
The project consists of 6 parsers, each one takes two parameters, a specific file or a folder (depending on the parser) and an output directory. These are specified in `config.properties`
# Packaging
Run `mvn clean package`; this will generate graph-bundle.zip in the /target folder
# Parsers
Below is the list of the Parsers, and the Nodes and Relationships that each one is responsible for producing. 
The java command is executed from the lib folder of the extracted zipfile. Substitute `${PARSER_NAME}` with the fully qualified name of the Parser e.g. com.graph.db.file.vcf.VcfParser
```
java -classpath '*:../conf/' **${PARSER_NAME}**
```
### VcfParser ###
This parses the genotype VCF file containing the variant to individual relationships.

| Nodes | Relationships |
| --- | --- |
| Person  | GeneticVariantToPerson |

### AnnotationParser ###
This parses the annotation file produced by the Variant Effect Predictor in the JSON format (VCF format to be supported soon).

| Nodes | Relationships |
| --- | --- |
| GeneticVariant | GeneToGeneticVariant |
| TranscriptVariant | GeneticVariantToTranscriptVariant |
| ConsequenceTerm | TranscriptToTranscriptVariant |
| Transcript | TranscriptVariantToConsequenceTerm |
| Gene | |

### GeneParser ###
This parses the OMIM-HPO file which links genes to the HPO terms to which they are associate.

| Nodes | Relationships |
| --- | --- |
| | GeneToTerm |

### PersonParser ###
This parses the phenotype file which links individuals to their HPO terms.

| Nodes | Relationships |
| --- | --- |
| Person | PersonToObservedTerm |
|  | PersonToNonObservedTerm |

### TermParser ###
This loads the HPO ontology which links HPO terms to other HPO terms. The relationships are produced; TermToParentTerm simply links a Term to its Parent Term, and TermToDescendantTerms produces all the descendant Terms for a specific Term, e.g. querying for HP:0000001 will output every single term since it is the root node.
   
| Nodes | Relationships |
| --- | --- |
| Term | TermToParentTerm |
|  | TermToDescendantTerms |

### TranscriptParser ###

| Nodes | Relationships |
| --- | --- |
| Transcript | TranscriptToGene |
| Gene | |
