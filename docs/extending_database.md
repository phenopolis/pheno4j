# Extending the database
 
A key advantage of graph database over relational databases, is that adding additional nodes, attributes or relationships can be done on the fly without redesigning the schema.
If a user wants to specify new relationship or node types, then the most straightforward appraoch is to use the Cypher LOAD_CSV functionality, as follows. 

## Loading new relationships: kinship example
 
Kinship are person-to-person relationships which give an estimate of how related people are.
The relationship inference software KING (http://people.virginia.edu/~wc9c/KING/) ran on a VCF
file of genotypes produces a tab delimited file showing the kinship relation between the different individuals.


The Cypher query language can then be used to directly load this file as relationships between persons. The “Relatedness” relationships are annotated with the following attributes N_SNP, HetHet, IBS0 and Kinship.
Here is the Cypher command to do the import:
 
``cypher
USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file://<absolute_path_to_file/relatedness.txt>" AS csvLine FIELDTERMINATOR '\t'
MATCH (person1:Person { personId: csvLine.ID1}),(person2:Person { personId: csvLine.ID2})
CREATE (person1)-[:Relatedness { N_SNP: csvLine.N_SNP, HetHet: csvLine.HetHet, IBS0: csvLine.IBS0, Kinship: csvLine.Kinship }]->(person2)
```

## Loading new nodes and relationships: transcript to tissue expression example
 
It also possible to create new node types and link them to existing nodes.
For example tissue expression data can be downloaded from Gene tissue expression database (GTEx) (https://www.gtexportal.org) in order to link a new Tissue type node to existing Transcript nodes on TranscriptReads relationship.

 
The data can be integrated using the following commands.
Load the Tissue Nodes:
 
 ``cypher
USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file://<absolute_path_to_file/Transcript2Tissue.csv>" AS csvLine 
MERGE (tissue:Tissue { name: csvLine.Tissue })
 ```
 
Load the TranscriptToTissue relationships:
 ``cypher
USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file://<absolute_path_to_file/Transcript2Tissue.csv>" AS csvLine 
MATCH (transcript:Transcript { transcript_id: csvLine.Transcript}),(tissue:Tissue { name: csvLine.Tissue})
CREATE (transcript)-[:TranscriptReads { Value: csvLine.Value }]->(tissue)
```
