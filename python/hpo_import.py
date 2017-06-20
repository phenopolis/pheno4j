#! /bin/env python
from __future__ import print_function
import sys
import requests
import py2neo

py2neo.authenticate("bigtop:57474", "neo4j", "1")
graph = py2neo.Graph('http://bigtop:57474/db/data/',secure=False,bolt=None, bolt_port=57687)

p=requests.get('https://raw.githubusercontent.com/obophenotype/human-phenotype-ontology/master/hp.obo')
p=p.text

# [Term]
# id: HP:0008434
# name: Hypoplastic cervical vertebrae
# alt_id: HP:0008415
# synonym: "Cervical vertebrae hypoplasia" EXACT []
# synonym: "Underdeveloped cervical vertebrae" EXACT [orcid.org/0000-0001-6908-9849]
# xref: UMLS:C1835570
# is_a: HP:0008417 ! Vertebral hypoplasia
# is_a: HP:0011041 ! Aplasia/Hypoplasia of the cervical spine

hpo_id=None
for l in p.split('\n'):
    l=l.strip()
    if l.startswith('[Term]'):
        hpo_id=None
        hpo_name=None
        synonyms=[]
        xref=''
    if l.startswith('id:'):
        hpo_id=l.replace('id:','').strip()
    if l.startswith('name:'):
        #Hypoplastic cervical vertebrae
        hpo_name=l.replace('name:','').strip()
    if l.startswith('alt_id:'):
        #HP:0008415
        alt_id=l.replace('alt_id:','').strip()
    if l.startswith('synonym:'):
        #"Cervical vertebrae hypoplasia" EXACT []
        #"Underdeveloped cervical vertebrae" EXACT [orcid.org/0000-0001-6908-9849]
        l=l.replace('synonym:','').strip()
        syn=l.split('EXACT')[0].replace('"','').strip()
        synonyms.append(syn)
    if l.startswith('xref:'):
        #UMLS:C1835570
        xref=l.replace('xref:','').strip()
    if l.startswith('is_a:'):
        #HP:0008417 ! Vertebral hypoplasia
        l=l.replace('is_a:','')
        s="""
        MERGE (t:Term { termId : {termId} , name: {name}, xref: {xref}, synonyms :{synonyms} })
        RETURN t
        """
        print(s)
        print(graph.run(s,termId=hpo_id, name=hpo_name, xref=xref, synonyms=synonyms))
        is_a_hpo_id=l.split('!')[0].strip()
        is_a_hpo_name=l.split('!')[1].strip()
        s="""
        MERGE (t:Term { termId : {termId} , name: {name} })
        RETURN t
        """
        print(s)
        print(graph.run(s,termId=is_a_hpo_id,name=is_a_hpo_name))
        # create relationship
        #print(hpo_id, 'is_a', is_a_hpo_id)
        s="""
        MATCH (t1:Term),(t2:Term)
        WHERE t1.termId = {term1} AND t2.termId = {term2}
        CREATE (t1)-[r:TermToParentTerm]->(t2)
        RETURN r
        """
        print(s)
        print(graph.run(s,term1=hpo_id,term2=is_a_hpo_id))
        #print(is_a_hpo_name)
    if l=='' and not hpo_id: continue
    #if l=='' and hpo_id:
        # merge/create hpo nodes
        #print(hpo_id)
        #print(hpo_name)
        # create relationships

    



