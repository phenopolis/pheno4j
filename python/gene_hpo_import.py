#! /bin/env python
from __future__ import print_function
import sys
import py2neo
import requests


py2neo.authenticate("bigtop:57474", "neo4j", "1")
graph = py2neo.Graph('http://bigtop:57474/db/data/',secure=False,bolt=None, bolt_port=57687)

p=requests.get('http://compbio.charite.de/jenkins/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/ALL_SOURCES_ALL_FREQUENCIES_genes_to_phenotype.txt')
p=p.text

#Format: entrez-gene-id<tab>entrez-gene-symbol<tab>HPO-Term-Name<tab>HPO-Term-ID

for l in p.split('\n'):
    if l.startswith('#'): continue
    l=l.strip()
    #Format: entrez-gene-id<tab>entrez-gene-symbol<tab>HPO-Term-Name<tab>HPO-Term-ID
    gene_id, gene_symbol, hpo_term_name, hpo_term_id,=l.split('\t')
    s="""
    MATCH (g:Gene),(t:Term)
    WHERE g.gene_name = {gene_symbol} AND t.termId = {hpo_id}
    CREATE (g)-[r:Causes]->(t)
    RETURN r
    """
    print(gene_symbol,hpo_term_id)
    print(graph.run(s,gene_symbol=gene_symbol,hpo_id=hpo_term_id))


