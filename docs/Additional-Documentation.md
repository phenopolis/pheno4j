# Application Design #
- Files are read line-by-line using BufferedReader, as opposed to loading all the data into memory
- Use Guava's EventBus to decouple the reading of input data and the writing of output data
- Input beans are mapped to Output beans using reflection, and are written out using super-csv

# Data Model #
![](https://github.com/phenopolis/pheno4j/blob/master/docs/schema%20diagram.png)

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
| Person  | HetVariantToPerson |
| | HomVariantToPerson |

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

# Constraints and Indexes #
To ensure optimum performance for the queries the following Constraints and Indexes are created by `com.graph.db.DatabaseIndexCreator`
## Constraints
- Term - termId
- Person - personId
- GeneticVariant - variantId
- Gene - gene_id
- TranscriptVariant - hgvsc
- Transcript - transcript_id
- ConsequenceTerm - consequenceTerm

## Indexes
- GeneticVariant - allele_freq
- GeneticVariant - cadd
- GeneticVariant - exac_AF
- Gene - gene_name

# Loading Manually Created Files #
In `config.properties` you can specify any additional files you would like loaded into neo4j as part of the bulk upload. 

For the relevant Node or Relationship (e.g. GeneticVariant, GeneToTerm), specify the full path to the file. If there are multiple files, separate them by commas. The files should not have a header, and the order of the columns must match the headers produced by `HeaderGenerator`.

# Sizing Test #

| Number of Individuals | Number of Variants | Total Number of Nodes | Total Number of Relationships | Total Number of Properties | Database Size (MB) |
| --- | --- | --- | --- | --- | --- |
| 1,000 | 1,876,797 | 4,223,968 | 103,155,817 | 153,133,671 | 5,901 |
| 2,000 | 2,673,978 | 5,911,219 | 199,069,603 | 205,735,931 | 9,897 |
| 3,000 | 3,218,287 | 7,062,972 | 293,919,001 | 243,309,397 | 13,613 |
| 4,000 | 3,653,139 | 7,982,961 | 389,593,314 | 271,785,796 | 17,205 |
| 5,000 | 4,008,807 | 8,736,155 | 484,182,286 | 294,170,309 | 20,653 |
