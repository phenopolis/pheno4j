

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
match (p:Person)-[:PersonToObservedTerm]->(t:Term) where p.personId='IRDC_batch12_4057_LDS2248' return t;
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
Descendent terms of "Retinal dystrophy":
```
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term) WHERE p.name ='Retinal dystrophy' RETURN q;
```
Damaging variants in dominant retinal dystrophy genes for people without retinal dystrophy:
```
// non_retinal_dystrophy_persons_list: individuals without retinal dystrophy phenotypes
MATCH (p:Term)-[:TermToDescendantTerms]->(q:Term)<-[:PersonToObservedTerm]-(r:Person)
WHERE p.name ='Retinal dystrophy'
WITH collect(distinct r) as persons
MATCH (p:Person)
WHERE NOT p IN persons
WITH collect(p) as non_retinal_dystrophy_persons_list
// dominant_retinal_dystrophy_genes_list: list of dominant retinal dystrophy genes
MATCH (t:Term)<--(g:Gene)-->(t2:Term)
WHERE t.name='Retinal dystrophy' AND t2.name='Autosomal dominant inheritance'
WITH collect (distinct g) as dominant_retinal_dystrophy_genes_list, non_retinal_dystrophy_persons_list
// gv: list of rare damaging variants 
UNWIND dominant_retinal_dystrophy_genes_list as dominant_retinal_dystrophy_genes
MATCH (dominant_retinal_dystrophy_genes)-->(gv:GeneticVariant)
WHERE gv.allele_freq < 0.001 AND gv.cadd_phred > 25 AND gv.kaviar_AF < 0.0001
WITH gv, non_retinal_dystrophy_persons_list
// link genes, variants and persons
UNWIND non_retinal_dystrophy_persons_list as non_retinal_dystrophy_persons
MATCH (dominant_retinal_dystrophy_genes)-->(gv)-->(non_retinal_dystrophy_persons)
RETURN distinct gv.variantId, gv.most_severe_consequence, gv.cadd_phred, gv.kaviar_AF, dominant_retinal_dystrophy_genes.gene_name, non_retinal_dystrophy_persons.personId ORDER BY gv.cadd_phred DESC;
```





