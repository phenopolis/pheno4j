# Examples of Cypher Queries

These can be run in 3 ways:
* using the browser interface
* using curl
* via the neo4j-shell ([See here for more information](http://neo4j.com/docs/operations-manual/current/tools/cypher-shell/))

## All Variants for an individual
```
MATCH (gv:GeneticVariant)-[]->(p:Person)
WHERE p.personId ='person1'
RETURN count(gv);
```
## Individuals who have a particular variant
```
MATCH (gv:GeneticVariant)-[]->(p:Person)
WHERE gv.variantId ='22-51171497-G-A'
RETURN p.personId;
```
## Find Variants shared by a list of Individuals
```
WITH ["person1","person2"] as persons
MATCH (p:Person)<-[]-(v:GeneticVariant) 
WHERE p.personId IN persons
WITH v, count(*) as c, persons
WHERE c = size(persons)
RETURN count(v.variantId);
```
## Rank Shared Variants by occurences in all Individuals
```
WITH ["person1","person2"] as persons
MATCH (p:Person)<-[]-(v:GeneticVariant) 
WHERE p.personId IN persons
and v.allele_freq < 0.01
WITH v, count(*) as c, persons
WHERE c = size(persons)
with v
return v.variantId, size((v)-[]-())  as count
ORDER BY count asc
LIMIT 10;
```
## Find Variants shared by a list of Individuals, that no one else has
```
WITH ["person1","person2"] as individuals
MATCH (p:Person)<-[]-(v:GeneticVariant) 
WHERE p.personId IN individuals
WITH v, count(*) as c, individuals
WHERE c = size(individuals)
with v, individuals
MATCH (v:GeneticVariant)
where size((v)-[]-()) = size(individuals)
RETURN v.variantId;
```
## Individuals who have a Term
```
MATCH (t:Term)<-[tp:PersonToObservedTerm]-(p:Person)
WHERE t.termId = 'HP:0000505'
RETURN p.personId;
```
## Find Genes that have no Term
```
MATCH (s:Gene)
WHERE NOT (s)-[:GeneToTerm]->()
RETURN count(s.gene_id);
```
## Find Variants which have a frequency less than 0.001 and a CADD score greater than 20
```
MATCH (gv:GeneticVariant)
WHERE gv.cadd > 20
AND gv.allele_freq < 0.001
RETURN count(gv);
```
## For a Term, find all the Descendant Terms
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)
where p.termId ='HP:0000505'
return q
```
## Find all Individuals with a specific Term (and any of its descendants)
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:PersonToObservedTerm]-(r:Person)
WHERE p.termId ='HP:0000505'
RETURN count(r);
```
## Find variants which have a frequency less than 0.001 and a CADD score greater than 20 seen in in people with HP:0000556 and belonging to a gene with HP:0000556
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:GeneToTerm]-(gs:Gene)
WHERE p.termId ='HP:0000556'
WITH distinct gs
MATCH (gs)-[:GeneToGeneticVariant]->(gv:GeneticVariant)
WHERE gv.allele_freq < 0.001 
and gv.cadd_phred > 20 
WITH distinct gv, gs
MATCH (r:Person)<-[]-(gv)
WITH distinct gv, gs, r
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:PersonToObservedTerm]-(r)
WHERE p.termId ='HP:0000556'
RETURN distinct r.personId, gv.variantId, gs.gene_name
ORDER BY r.personId asc;
```
## For an Individual, rank their variants by the number of occurrences in other Individuals
```
MATCH (p:Person {personId:"person1"})<-[]-(gv:GeneticVariant)-[:GeneticVariantToTranscriptVariant]->(tv:TranscriptVariant)
WHERE (gv.allele_freq < 0.1 or NOT EXISTS(gv.exac_af))
WITH size(()<-[]-(gv)) as count, gv
WHERE count > 1 
RETURN gv.variantId, size(()<-[]-(gv)) as count
ORDER BY count asc
LIMIT 10;
```
## For a particular individual, show a list of 10 top individuals in decreasing order by the number of variants they share with the given individual, with a frequency less than 0.001 or NA in ExAC, and that appear in less than 5% of individuals
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"WebsterURMD_Sample_GV4344"})<-[]-(gv:GeneticVariant)
WHERE (gv.allele_freq < 0.001 or NOT EXISTS(gv.exac_af))
WITH size(()<-[]-(gv)) as count , gv, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (gv)-[]->(q:Person)
WHERE p <> q
WITH p,q,count(gv) as c
ORDER BY c desc LIMIT 10
RETURN p.personId,q.personId, c;
```
## As above, but show the as a percentage the common variants (i.e. the intersection) over the shared variants (the union)
```
MATCH (k:Person)
WITH count(k) as numberOfPeople
MATCH (p:Person {personId:"person1"})<-[]-(gv:GeneticVariant)
WHERE (gv.allele_freq < 0.001 or NOT EXISTS(gv.exac_af))
WITH size(()<-[]-(gv)) as count , gv, p, numberOfPeople
WHERE count > 1 
AND ((count / toFloat(numberOfPeople))  <= 0.05)
MATCH (gv)-[]->(q:Person)
WHERE p <> q
WITH p,q,count(gv) as intersection, numberOfPeople
ORDER BY intersection DESC limit 1
MATCH (x:Person)<-[]-(v:GeneticVariant)
WHERE (x.personId = p.personId or x.personId = q.personId)
AND (v.allele_freq < 0.001 or NOT EXISTS(v.exac_af))
AND ((size(()<-[]-(v)) / toFloat(numberOfPeople))  <= 0.05)
WITH p, q, v, intersection
RETURN p.personId, q.personId, intersection, size(collect(distinct v)) as unionSum, (round((intersection/toFloat(size(collect(distinct v))))*100.0*10)/10) as PercentShared
ORDER BY PercentShared DESC;
```
## Get rare damaging variants in Gene TTLL5
```
MATCH (gs:Gene {gene_name:"TTLL5"})-[:GeneToGeneticVariant]->(gv:GeneticVariant)
WHERE gv.allele_freq < 0.001 
AND gv.cadd_phred > 20 
WITH distinct gv
MATCH (gv)-[]->(p:Person)-[:PersonToObservedTerm]-(t:Term)
return gv.variantId, p.personId, t.termId, t.name;
```
## Use `LOAD CSV` to load in 'Relatedness' relationships
```
USING PERIODIC COMMIT 500
LOAD CSV WITH HEADERS FROM "file:///absolute_path_to_file/relatedness.txt" AS csvLine FIELDTERMINATOR '\t'
MATCH (person1:Person { personId: csvLine.ID1}),(person2:Person { personId: csvLine.ID2})
CREATE (person1)-[:Relatedness { N_SNP: csvLine.N_SNP, HetHet: csvLine.HetHet, IBS0: csvLine.IBS0, Kinship: csvLine.Kinship }]->(person2)
```
