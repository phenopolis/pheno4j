# Pheno4j: a graph based HPO to NGS database

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
| Person  | VariantToPerson |
| Variant |  |
### AnnotationParser
This parses the annotation file produced by the Variant Effect Predictor in the JSON format (VCF format to be supported soon):
```
java -jar graph-db.jar AnnotationParser $inputFolderWithJsonFiles $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| GeneId | GeneIdToVariant |
| GeneSymbol | VariantToAnnotatedVariant |
| AnnotatedVariant | GeneSymbolToGeneId |
### GeneParser
This parses the OMIM-HPO file which links genes to the HPO terms to which they are associate:
```
java -jar graph-db.jar GeneParser $geneFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| GeneSymbol | GeneSymbolToTerm |

### PersonParser
This parses the phenotype file which links individuals to their HPO terms:
```
java -jar graph-db.jar PersonParser $personFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| GeneSymbol | PersonToObservedTerm |
|  | PersonToNonObservedTerm |
|  | PersonToGeneSymbol |
### HPOTermParser
This loads the HPO ontology which links HPO terms to other HPO terms with "is a" relationships.
```
java -jar graph-db.jar TermParser $termFile $outputFolder
```
| Nodes | Relationships |
| --- | --- |
| Term | TermToTerm |
# Running the neo4j Bulk Csv Import Tool
1. generate the database using the csv's
```
bin/neo4j-import   --into /generatedGraphOutputFolder/graph.db --id-type string --bad-tolerance 50000  --skip-bad-relationships true --skip-duplicate-nodes true \
--nodes:AnnotatedVariant /folder/AnnotatedVariant-header.csv,/folder/AnnotatedVariant-AnnotationParser.csv \
--nodes:GeneSymbol /folder/GeneSymbol-header.csv,/folder/GeneSymbol.csv \
--nodes:GeneId /folder/GeneId-header.csv,/folder/GeneId-AnnotationParser.csv \
--nodes:Person /folder/Person-header.csv,/folder/Person-mainset_July2016.csv \
--nodes:Term /folder/Term-header.csv,/folder/Term-TermParser.csv \
--nodes:Variant /folder/Variant-header.csv,/folder/Variant-mainset_July2016.csv \
--relationships:HAS_VARIANT /folder/GeneIdToVariant-header.csv,/folder/GeneIdToVariant-AnnotationParser.csv \
--relationships:HAS_GENE_ID /folder/GeneSymbolToGeneId-header.csv,/folder/GeneSymbolToGeneId-AnnotationParser.csv \
--relationships:INFLUENCES /folder/GeneSymbolToTerm-header.csv,/folder/GeneSymbolToTerm-GeneSymbolParser.csv \
--relationships:HAS_GENE_SYMBOL /folder/PersonToGeneSymbol-header.csv,/folder/PersonToGeneSymbol-PersonParser.csv \
--relationships:HAS_NON_OBSERVED_TERM /folder/PersonToNonObservedTerm-header.csv,/folder/PersonToNonObservedTerm-PersonParser.csv \
--relationships:HAS_OBSERVED_TERM /folder/PersonToObservedTerm-header.csv,/folder/PersonToObservedTerm-PersonParser.csv \
--relationships:IS_A /folder/TermToTerm-header.csv,/folder/TermToTerm-TermParser.csv \
--relationships:HAS_ANNOTATION /folder/VariantToAnnotatedVariant-header.csv,/folder/VariantToAnnotatedVariant-AnnotationParser.csv \
--relationships:PRESENT_IN /folder/VariantToPerson-header.csv,/folder/VariantToPerson-mainset_July2016.csv > /folder/neo4j-log.txt &
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
CREATE CONSTRAINT ON (p:Variant) ASSERT p.variantId IS UNIQUE;
CREATE CONSTRAINT ON (p:Person) ASSERT p.personId IS UNIQUE;
CREATE CONSTRAINT ON (p:GeneSymbol) ASSERT p.geneSymbol IS UNIQUE;
CREATE CONSTRAINT ON (p:AnnotatedVariant) ASSERT p.variantId IS UNIQUE;
CREATE CONSTRAINT ON (p:Term) ASSERT p.termId IS UNIQUE;
CREATE CONSTRAINT ON (p:GeneId) ASSERT p.geneId IS UNIQUE;

CREATE INDEX ON :AnnotatedVariant(allele_freq);
CREATE INDEX ON :AnnotatedVariant(cadd);
CREATE INDEX ON :AnnotatedVariant(hasExac);
```
# Example Cypher Queries
## All Variants for an individual
```
MATCH (root:Variant)-[:PRESENT_IN]->(child:Person)
WHERE child.personId ='XXX'
RETURN count(root.variantId);
```
## Individuals who have a particular variant
```
MATCH (root:Variant)-[:PRESENT_IN]->(child:Person)
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
RETURN p.personId;
```
## Find Gene Symbols that have multiple Gene Ids
```
MATCH (s:GeneSymbol)-[:HAS_GENE_ID]->(i:GeneId)
WITH count(*) AS count, s
WHERE count > 1
RETURN s.geneSymbol, count;
```
## Find Gene Symbols that have no Term
```
MATCH (s:GeneSymbol)
WHERE NOT (s)-[:INFLUENCES]->()
RETURN s.geneSymbol;
```
## Find Variants which have a frequency less than 0.001 and a CADD score greater than 20
```
MATCH (n:AnnotatedVariant) 
WHERE n.allele_freq < 0.001 and n.cadd > 20 
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
where p.termId ='XXX'
with  p + collect( distinct q) as allRows
MATCH (p:Person)-[:HAS_OBSERVED_TERM]->(t:Term) 
where t IN allRows
return count(p);
```
## Find variants which have a frequency less than 0.001 and a CADD score greater than 20 seen in in people with HP:0000556 and belonging to a gene with HP:0000556
```
MATCH (p:Term)<-[:IS_A*]-(q:Term)
WHERE p.termId ='HP:0000556'
WITH  p + collect( distinct q) as allTerms
MATCH (t:Term)<-[:INFLUENCES]-(gs:GeneSymbol) <-[:HAS_GENE_SYMBOL]-(p:Person)<-[:PRESENT_IN]-(v:Variant)-[:HAS_ANNOTATION]-(av:AnnotatedVariant)
WHERE t IN allTerms
AND av.allele_freq < 0.001 
AND av.cadd > 20 
RETURN count(distinct av.variantId);

```
### For an Individual, rank their variants by the number of occurrences in other Individuals
```
MATCH (p:Person {personId:"XXX"})<-[:PRESENT_IN]-(v:Variant)-[:HAS_ANNOTATION]->(av:AnnotatedVariant)
where (av.allele_freq < 0.001 or av.hasExac = false)
with size(()<-[:PRESENT_IN]-(v)) as count , v
where count > 1 
and count < 10
return v.variantId, size(()<-[:PRESENT_IN]-(v)) as count
order by count asc
```
### For a particular individual, show a list of individuals in decreasing order by the number of variants they share with the given individual, with a frequency less than 0.001 or NA in ExAC, and that appear in less than 5% of individuals
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"XXX"})<-[:PRESENT_IN]-(v:Variant)-[:HAS_ANNOTATION]->(av:AnnotatedVariant)
WHERE (av.allele_freq < 0.001 or av.hasExac = false)
WITH size(()<-[:PRESENT_IN]-(v)) as count , v, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
match (v)-[:PRESENT_IN]->(q:Person)
WHERE p <> q
WITH p,q,count(v) as c
ORDER BY c desc LIMIT 10
RETURN p.personId,q.personId, c
```
### As above, but show the as a percentage the common variants (i.e. the intersection) over the shared variants (the union)
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"XXX"})<-[:PRESENT_IN]-(v:Variant)-[:HAS_ANNOTATION]->(av:AnnotatedVariant)
WHERE (av.allele_freq < 0.001 or av.hasExac = false)
WITH size(()<-[:PRESENT_IN]-(v)) as count , v, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (v)-[:PRESENT_IN]->(q:Person)
WHERE p <> q
WITH p,q,count(v) as intersection, numberOfPeople
order by intersection DESC limit 1
MATCH (x:Person)<-[:PRESENT_IN]-(v:Variant)-[:HAS_ANNOTATION]->(av:AnnotatedVariant)
WHERE (x.personId = p.personId or x.personId = q.personId)
AND (av.allele_freq < 0.001 or av.hasExac = false)
AND ((size(()<-[:PRESENT_IN]-(v)) / toFloat(numberOfPeople))  <= 0.05)
WITH p, q, v, intersection
RETURN p.personId, q.personId, intersection, size(collect(distinct v)) as unionSum, (round((intersection/toFloat(size(collect(distinct v))))*100.0*10)/10) as PercentShared
ORDER BY PercentShared DESC
```