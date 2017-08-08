#! /bin/env python

### From VEP extracts and outputs separate annotation genotype,

from __future__ import print_function
import sys
import re
import argparse
import gzip
from pyfaidx import Fasta
import os
import json
from subprocess import call


parser=argparse.ArgumentParser(description='Arguments to vcf_parser.py')
parser.add_argument('--file', required=False)
#parser.add_argument('--basename', required=False)
parser.add_argument('--importdir', required=True)
#parser.add_argument('--pheno4j_conn', required=True)
#parser.add_argument('--pheno4j_user', required=True)
#parser.add_argument('--pheno4j_password', required=True)


args=parser.parse_args()
if not args.file:
    infile=sys.stdin
    basename=args.basename
else:
    filename=args.file
    basename=filename.split('.')[0]
    if filename.endswith('.gz'):
        infile=gzip.open(filename,'r')
    else:
        infile=open(filename,'r')


#ANNOTATION_HEADER=['VARIANT_ID']+['ID']+CSQ+CADD+GO+MAF+EXAC+CUSTOM_ANNOTATION+CUSTOM_ALLELE_FREQ+[x.replace('1KG','ONEKG') for x in ONEKG]+ESP
#ANNOTATION_HEADER=['VARIANT_ID']+['ID']+CSQ+CADD+GO+MAF+EXAC+CUSTOM_ALLELE_FREQ+[x.replace('1KG','ONEKG') for x in ONEKG]+ESP
#if args.genotypes: ANNOTATION_HEADER+=['AF','WT','HET','HOM','MISS']
#if args.sequence: ANNOTATION_HEADER+=['old_sequence','new_sequence']
person_filename='Person.csv'
#hom_variant_filename='-'.join([basename,'hom_variants.csv'])
hom_variant_filename='HomVariantToPerson.csv'
#het_variant_filename='-'.join([basename,'het_variants.csv'])
het_variant_filename='HetVariantToPerson.csv'

person_file=open(person_filename, 'w+')
hom_variant_file=open(hom_variant_filename, 'w+')
het_variant_file=open(het_variant_filename, 'w+')
#if args.genotypes: genotype_file=open('-'.join([basename,'genotypes.csv']), 'w+')
#if args.depth: depth_file=open('-'.join([basename,'genotypes_depth.csv']), 'w+')

call(["ln", "-s","-f",os.path.abspath(person_filename),args.importdir])
call(["ln", "-s","-f",os.path.abspath(hom_variant_filename),args.importdir])
call(["ln", "-s","-f",os.path.abspath(het_variant_filename),args.importdir])

print('personId:ID(Person)',sep=',',file=person_file)
print('variantId:START_ID(GeneticVariant)','personId:END_ID(Person)',sep=',',file=hom_variant_file)
print('variantId:START_ID(GeneticVariant)','personId:END_ID(Person)',sep=',',file=het_variant_file)

N=0

for l in infile:
    #if N > 1000: break
    N+=1
    #get the format of the VEP consequence (CSQ) field
    #the names of the VEP CSQ fields are "|" delimited
    if l.startswith('##INFO=<ID=CSQ,'):
        CSQ_HEADER=re.compile('Format: (.*)">').search(l).group(1).split('|')
        continue
    ##reference=file:///lustre/home/production/Applications/bcbio/0.9.7/genomes/Hsapiens/hg38/seq/hg38.fa
    if l.startswith('##reference'):
        ##reference=file:///lustre/home/production/Applications/bcbio/0.9.7/genomes/Hsapiens/hg38/seq/hg38.fa
        reference_file=l.split('=')[1]
        if not os.path.isfile(reference_file):
            if '38' in os.path.basename(reference_file):
                reference_file='/cluster/scratch3/vyp-scratch2/reference_datasets/human_reference_sequence/GCA_000001405.15_GRCh38_no_alt_analysis_set.fna'
            else:
                reference_file='/cluster/scratch3/vyp-scratch2/reference_datasets/human_reference_sequence/human_g1k_v37.fasta'
        REFERENCE=Fasta(reference_file)
    #ignore all other '##' lines
    if l.startswith('##'): continue
    #line which starts with a single '#' is the header
    #When we get to the header line, print it out.
    if l.startswith('#'):
        l=l.strip('#')
        HEADER=l.strip().split('\t')
        SAMPLES=HEADER[9:]
        for s in SAMPLES:
            print(s,file=person_file)
        GENOTYPE_HEADER=['VARIANT_ID']+SAMPLES
        #print(*(GENOTYPE_HEADER),sep=',')
        continue
    s=l.strip().split('\t')
    # s will contain all fields for this line (including CSQ)
    s=dict(zip(HEADER, s))
    #for k in SAMPLES: s[k]=s[k].split(':')[0]
    for k in SAMPLES: s[k]=s[k]
    VARIANT_ID='-'.join([s['CHROM'],s['POS'],s['REF'],s['ALT']]).replace(',','/')
    #print output, anything which was not found in the line gets a '.'
    #','.join([csq['Feature']  for csq in cons if csq['Feature_type']=='Transcript']), [s[k] for k in s if 'EXAC' in k]
    #print '\t'.join( [VARIANT_ID] + [s.get(h,'.')  for h in OUTPUT] )
    # GENOTYPE
    # output of this goes to genotype.csv file
    # number of times we have the alternative allele
    def genotype(g):
        d=dict(zip(s['FORMAT'].split(':'),g.split(':')))
        geno=d['GT']
        geno=geno.replace('|','/')
        if geno=='0/0' or geno=='./0' or geno=='0/.': 
            return 0
        elif geno=='1/0' or geno=='0/1' or geno=='./1' or geno=='1/.': 
            return 1
        elif geno=='1/1': 
            return 2
        elif geno=='./.':
            return 'NA'
        else:
            #print( VARIANT_ID, geno, sep=',', file=sys.stderr)
            #print( l, file=sys.stderr)
            return 'NA'
    GENOTYPES=[genotype(s.get(h,'NA'))for h in SAMPLES]
    # if all genotypes are NA then skip this variant
    if GENOTYPES.count('NA')==len(SAMPLES): continue
    if '/' in VARIANT_ID: continue
    for i, g in enumerate(GENOTYPES):
        if g==1:
            print(VARIANT_ID,SAMPLES[i],sep=',',file=het_variant_file)
        elif g==2:
            print(VARIANT_ID,SAMPLES[i],sep=',',file=hom_variant_file)


