

```
MATCH (gs:Gene {gene_name:"TTLL5"})-[:GeneToGeneticVariant]->(gv:GeneticVariant) WHERE gv.allele_freq < 0.001 AND gv.cadd_phred > 20 WITH distinct gv MATCH (gv)-[]->(p:Person)-[:PersonToObservedTerm]-(t:Term) return gv.variantId, p.personId, t.termId, t.name;
```

```
match (t:Term)<-[]-(g:Gene)-[]->(t2:Term) where t.name='Retinal dystrophy' and t2.name='Autosomal dominant inheritance'
return distinct g;
```

```
match (p:Person)-[:PersonToObservedTerm]->(t:Term) where NOT (:Term {name:'Retinal dystrophy'})-[:PersonToObserverdTerm]->(p:Person) return distinct p;
```
```
match (p:Person)-[:PersonToObservedTerm]->(t:Term) where p.personId='IoO_Gavin_FEVRS0195' return t;
```
```
match (p:Person)-[:PersonToObservedTerm]->(t:Term) where p.personId='IoO_FFS_batch4_FH10388' return t;
```
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:PersonToObservedTerm]-(r:Person) WHERE p.name='Retinal dystrophy'
WITH distinct r as p1
MATCH (p2:Person)
WHERE p2.personID<>p1.personID
return distinct p2;
```
```
MATCH (retinal_dystrophy:Term{name:'Retinal dystrophy'})-[:TermToDescendantTerms]->(q:Term) 
WITH q
MATCH (p:Person)
WHERE NOT (p)-->(q)
RETURN count(distinct p) ;
```
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:PersonToObservedTerm]-(r:Person)
WHERE p.name ='Retinal dystrophy'
WITH collect(distinct r) as persons
MATCH (p:Person)
WHERE NOT p IN persons
RETURN distinct p;
```

```
MATCH (p:Person),(retinal_dystrophy:Term { name: 'Retinal dystrophy' })
WHERE NOT (p)-[:PersonToObservedTerm]->(retinal_dystrophy)
RETURN distinct p;
```
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:PersonToObservedTerm]-(r:Person)
WHERE p.termId ='HP:0000556'
WITH collect(distinct r) as persons
MATCH (p:Person)
WHERE NOT p IN persons
RETURN count(p);
```

