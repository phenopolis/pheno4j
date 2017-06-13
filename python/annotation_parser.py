#! /bin/env python
from __future__ import print_function
import sys
import json
import argparse
import gzip
from pyfaidx import Fasta
import os
import json
from subprocess import call


def eprint(*args, **kwargs): print(*args, file=sys.stderr, **kwargs)

# Takes the output of VEP and reformats

parser=argparse.ArgumentParser(description='Arguments to annotation_importer.py')
parser.add_argument('--file', required=True)
parser.add_argument('--basename', required=False)
parser.add_argument('--importdir', required=True)


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

# node files

#Gene_filename='-'.join([basename,'Gene.csv'])
Gene_filename='Gene.csv'
Gene_file=open(Gene_filename, 'w+')
# {gene_id:"ENSG00000242822",gene_name:"RN7SL575P"}
#print('gene_id:ID(Gene)','gene_name',sep=',',file=Gene_file)
call(["ln", "-s","-f",os.path.abspath(Gene_filename),args.importdir])

#GeneticVariant_filename='-'.join([basename,'GeneticVariant.csv'])
GeneticVariant_filename='GeneticVariant.csv'
GeneticVariant_file=open(GeneticVariant_filename, 'w+')
# {variantId:"1-13302-C-T",allele_string:"C/T",start:13302,end:13302,seq_region_name:"1",most_severe_consequence:"non_coding_transcript_exon_variant",strand:1,AC:1488,allele_freq:0.269,AN:5528,ExcessHet:0.0105,FS:9.303,InbreedingCoeff:0.0382,MLEAC:1480,MLEAF:0.268,MQ:17.8,MQRankSum:-3.05,ReadPosRankSum:-1.076,VQSLOD:-0.6461,culprit:"QD",gnomad_genomes_AC_AFR:1824,gnomad_genomes_AC_AMR:222,gnomad_genomes_AC_ASJ:62,gnomad_genomes_AC_raw:12176,gnomad_genomes_AF_NFE:0.372049,gnomad_genomes_AF_OTH:0.392361,gnomad_genomes_AF_raw:0.39856,gnomad_genomes_AN_AFR:5410,gnomad_genomes_AC_EAS:89,gnomad_genomes_AC_Female:3140,gnomad_genomes_AC_OTH:226,gnomad_genomes_AC_NFE:3152,gnomad_genomes_AC_Male:3685,gnomad_genomes_AC_FIN:1250,gnomad_genomes_AF_AFR:0.337153,gnomad_genomes_AF_AMR:0.4625,gnomad_genomes_AF_ASJ:0.348315,gnomad_genomes_AF_EAS:0.0911885,gnomad_genomes_AF_FIN:0.544425,gnomad_genomes_AF_Female:0.38274,gnomad_genomes_AF_Male:0.361842,gnomad_genomes_AN_AMR:480,gnomad_genomes_AN_ASJ:178,gnomad_genomes_AN_EAS:976,gnomad_genomes_AN_FIN:2296,gnomad_genomes_AN_Female:8204,gnomad_genomes_AN_Male:10184,gnomad_genomes_AN_NFE:8472,gnomad_genomes_AN_OTH:576,gnomad_genomes_AN_raw:30550,gnomad_genomes_Hom_AFR:78,gnomad_genomes_Hom_AMR:29,gnomad_genomes_Hom_ASJ:8,gnomad_genomes_Hom_EAS:0,gnomad_genomes_Hom_FIN:212,gnomad_genomes_Hom_Female:227,gnomad_genomes_Hom_Male:296,gnomad_genomes_Hom_NFE:174,gnomad_genomes_Hom_OTH:22,gnomad_genomes_Hom_raw:1684,gnomad_genomes_Hom:523,kaviar_AN:26378,kaviar_AC:643,kaviar_AF:0.0243764,cadd_phred:6,cadd_raw:0.349094}
gnomad_genomes_headers=['gnomad_genomes_AC_AFR', 'gnomad_genomes_AC_AMR', 'gnomad_genomes_AC_ASJ', 'gnomad_genomes_AC_raw', 'gnomad_genomes_AF_NFE', 'gnomad_genomes_AF_OTH', 'gnomad_genomes_AF_raw', 'gnomad_genomes_AN_AFR', 'gnomad_genomes_AC_EAS', 'gnomad_genomes_AC_Female', 'gnomad_genomes_AC_OTH', 'gnomad_genomes_AC_NFE', 'gnomad_genomes_AC_Male', 'gnomad_genomes_AC_FIN', 'gnomad_genomes_AF_AFR', 'gnomad_genomes_AF_AMR', 'gnomad_genomes_AF_ASJ', 'gnomad_genomes_AF_EAS', 'gnomad_genomes_AF_FIN', 'gnomad_genomes_AF_Female', 'gnomad_genomes_AF_Male', 'gnomad_genomes_AN_AMR', 'gnomad_genomes_AN_ASJ', 'gnomad_genomes_AN_EAS', 'gnomad_genomes_AN_FIN', 'gnomad_genomes_AN_Female', 'gnomad_genomes_AN_Male', 'gnomad_genomes_AN_NFE', 'gnomad_genomes_AN_OTH', 'gnomad_genomes_AN_raw', 'gnomad_genomes_Hom_AFR', 'gnomad_genomes_Hom_AMR', 'gnomad_genomes_Hom_ASJ', 'gnomad_genomes_Hom_EAS', 'gnomad_genomes_Hom_FIN', 'gnomad_genomes_Hom_Female', 'gnomad_genomes_Hom_Male', 'gnomad_genomes_Hom_NFE', 'gnomad_genomes_Hom_OTH', 'gnomad_genomes_Hom_raw', 'gnomad_genomes_Hom']
gnomad_exomes_headers=['gnomad_exomes_AC_AFR', 'gnomad_exomes_AC_AMR', 'gnomad_exomes_AC_ASJ', 'gnomad_exomes_AC_raw', 'gnomad_exomes_AF_NFE', 'gnomad_exomes_AF_OTH', 'gnomad_exomes_AF_raw', 'gnomad_exomes_AN_AFR', 'gnomad_exomes_AC_EAS', 'gnomad_exomes_AC_Female', 'gnomad_exomes_AC_OTH', 'gnomad_exomes_AC_NFE', 'gnomad_exomes_AC_Male', 'gnomad_exomes_AC_FIN', 'gnomad_exomes_AF_AFR', 'gnomad_exomes_AF_AMR', 'gnomad_exomes_AF_ASJ', 'gnomad_exomes_AF_EAS', 'gnomad_exomes_AF_FIN', 'gnomad_exomes_AF_Female', 'gnomad_exomes_AF_Male', 'gnomad_exomes_AN_AMR', 'gnomad_exomes_AN_ASJ', 'gnomad_exomes_AN_EAS', 'gnomad_exomes_AN_FIN', 'gnomad_exomes_AN_Female', 'gnomad_exomes_AN_Male', 'gnomad_exomes_AN_NFE', 'gnomad_exomes_AN_OTH', 'gnomad_exomes_AN_raw', 'gnomad_exomes_Hom_AFR', 'gnomad_exomes_Hom_AMR', 'gnomad_exomes_Hom_ASJ', 'gnomad_exomes_Hom_EAS', 'gnomad_exomes_Hom_FIN', 'gnomad_exomes_Hom_Female', 'gnomad_exomes_Hom_Male', 'gnomad_exomes_Hom_NFE', 'gnomad_exomes_Hom_OTH', 'gnomad_exomes_Hom_raw', 'gnomad_exomes_Hom']
GeneticVariant_headers=[ 'variantId', 'allele_string', 'start', 'end', 'seq_region_name', 'most_severe_consequence', 'strand', 'AC', 'allele_freq', 'AN', 'ExcessHet', 'FS', 'InbreedingCoeff', 'MLEAC', 'MLEAF', 'MQ', 'MQRankSum', 'ReadPosRankSum', 'VQSLOD', 'culprit', 'kaviar_AN', 'kaviar_AC', 'kaviar_AF', 'cadd_phred', 'cadd_raw']+gnomad_genomes_headers+gnomad_exomes_headers
#print( ',',join([ 'variantId:ID(GeneticVariant)', 'allele_string', 'start', 'end', 'seq_region_name', 'most_severe_consequence', 'strand', 'AC', 'allele_freq', 'AN', 'ExcessHet', 'FS', 'InbreedingCoeff', 'MLEAC', 'MLEAF', 'MQ', 'MQRankSum', 'ReadPosRankSum', 'VQSLOD', 'culprit', 'kaviar_AN', 'kaviar_AC', 'kaviar_AF', 'cadd_phred', 'cadd_raw']+gnomad_genomes_headers+gnomad_exomes_headers),file=file())
#print(','.join(GeneticVariant_headers),file=GeneticVariant_file)
call(["ln", "-s","-f",os.path.abspath(GeneticVariant_filename),args.importdir])

#TranscriptVariant_filename='-'.join([basename,'TranscriptVariant.csv'])
TranscriptVariant_filename='TranscriptVariant.csv'
TranscriptVariant_file=open(TranscriptVariant_filename, 'w+')
# {hgvsc:"ENST00000456328.2:n.550C>T",impact:"MODIFIER",exon:"3/3"}
TranscriptVariant_headers=[ 'hgvsc','hgvsp','impact','exon','consequence_terms','fathmm_mkl_nc']
#print(','.join(TranscriptVariant_headers),file=TranscriptVariant_file)
call(["ln", "-s","-f",os.path.abspath(TranscriptVariant_filename),args.importdir])

#Transcript_filename='-'.join([basename,'Transcript.csv'])
Transcript_filename='Transcript.csv'
Transcript_file=open(Transcript_filename, 'w+')
# {transcript_id:"ENST00000541371"}
#print('transcript_id:ID(Transcript)',sep=',',file=Transcript_file)
call(["ln", "-s","-f",os.path.abspath(Transcript_filename),args.importdir])

# relationship files

#GeneticVariantToGene_filename='-'.join([basename,'GeneticVariantToGene.csv'])
GeneticVariantToGene_filename='GeneticVariantToGene.csv'
GeneticVariantToGene_file=open(GeneticVariantToGene_filename, 'w+')
print('variantId:START_ID(GeneticVariant)','gene_id:END_ID(Gene)',sep=',',file=GeneticVariantToGene_file)
call(["ln", "-s","-f",os.path.abspath(GeneticVariantToGene_filename),args.importdir])

#GeneticVariantToTranscriptVariant_filename='-'.join([basename,'GeneticVariantToTranscriptVariant.csv'])
GeneticVariantToTranscriptVariant_filename='GeneticVariantToTranscriptVariant.csv'
GeneticVariantToTranscriptVariant_file=open(GeneticVariantToTranscriptVariant_filename, 'w+')
print('variantId:START_ID(GeneticVariant)','hgvsc:END_ID(TranscriptVariant)',sep=',',file=GeneticVariantToTranscriptVariant_file)
call(["ln", "-s","-f",os.path.abspath(GeneticVariantToTranscriptVariant_filename),args.importdir])

#TranscriptToGene_filename='-'.join([basename,'TranscriptToGene.csv'])
TranscriptToGene_filename='TranscriptToGene.csv'
TranscriptToGene_file=open(TranscriptToGene_filename, 'w+')
print('transcript_id:START_ID(Transcript)','gene_id:END_ID(Gene)',sep=',',file=TranscriptToGene_file)
call(["ln", "-s","-f",os.path.abspath(TranscriptToGene_filename),args.importdir])


def clean(d,field):
    for cons in d['transcript_consequences']:
        if field not in cons: continue
        for x in cons[field].split(','):
            if len(x.split(':'))!=2:
                sys.stderr.write(x)
                sys.stderr.write('\n')
                continue
            id,num,=x.split(':') 
            if len(id.split('>'))!=2:
                sys.stderr.write(id)
                sys.stderr.write('\n')
                continue
            ref,alt,=id.split('>')
            if alt!=d['ALT']: continue
            try:
                cons[field]=float(num)
            except:
                #eprint(num)
                print('ERROR:', 'not a number', num, id)

def freq_cleanup(d):
    for cons in d['transcript_consequences']:
        for k in [k for k in cons.keys() if k.startswith('exac') or k=='kaviar']:
            d[k]=cons[k]
    for cons in d['transcript_consequences']:
        for k in [k for k in cons.keys() if k.startswith('exac') or k=='kaviar']:
            del cons[k]

def go_cleanup(d):
    for cons in d['transcript_consequences']:
        if 'go' not in cons: continue
        cons['go']=cons['go'].split(',')

def canonical(d):
    for cons in d['transcript_consequences']:
        if 'canonical' not in cons: continue
        #d['canonical_cadd']=d.get('canonical_cadd',[])+[cons.get('cadd','')]
        d['canonical_hgvsc']=d.get('canonical_hgvsc',[])+[cons.get('hgvsc','')]
        d['canonical_hgvsp']=d.get('canonical_hgvsp',[])+[cons.get('hgvsp','')]
        #grab the transript
        d['canonical_transcript']=d.get('canonical_transcript',[])+[cons['transcript_id']]
        #d['canonical_gene_name_upper']=d.get('gene_name_upper',[])+[cons['gene_symbol'].upper()]
        #d['canonical_gene_name_upper']=d['canonical_gene_name_upper'][0]

def gnomad_genomes(d):
    if 'custom_annotations' not in d: return
    custom_annotations=d['custom_annotations']
    if 'gnomad_genomes' not in custom_annotations: return
    gnomad_genomes=custom_annotations['gnomad_genomes'][0]
    for k in gnomad_genomes['fields']:
        d['gnomad_genomes_'+k]=gnomad_genomes['fields'][k]

def gnomad_exomes(d):
    if 'custom_annotations' not in d: return
    custom_annotations=d['custom_annotations']
    if 'gnomad_exomes' not in custom_annotations: return
    gnomad_genomes=custom_annotations['gnomad_exomes'][0]
    for k in gnomad_genomes['fields']:
        d['gnomad_exomes_'+k]=gnomad_genomes['fields'][k]

def kaviar(d):
    if 'custom_annotations' not in d: return
    custom_annotations=d['custom_annotations']
    if 'kaviar' not in custom_annotations: return
    kav=custom_annotations['kaviar'][0]
    for k in kav['fields']:
        d['kaviar_'+k]=kav['fields'][k]

def allele_freq(d):
    d['allele_freq']=float(d['AC'])/float(d['AN'])

def cadd(d):
    for cons in d['transcript_consequences']:
        if 'cadd_phred' not in cons: continue
        d['cadd_phred']=cons['cadd_phred']
        d['cadd_raw']=cons['cadd_raw']

headers=['CHROM','POS','ID','REF','ALT','QUAL','FILTER','INFO']
for l in infile:
    d=json.loads(l.strip())
    d.update(dict(zip(headers,d['input'].split('\t'))))
    d.update(dict([tuple(x.split('=')) for x in d['INFO'].split(';') if len(x.split('='))==2]))
    del d['INFO']
    del d['input']
    d['variantId']='-'.join([d['CHROM'],d['POS'],d['REF'],d['ALT']])
    if ',' in d['ALT']:
        #eprint(d['variant_id']+' MULTIALLELIC')
        print('ERROR:', d['variantId']+' MULTIALLELIC')
        continue
    if 'transcript_consequences' not in d:
        #eprint(d['variant_id']+' NOT CODING')
        d['transcript_consequences']=[]
        print(d['variantId']+' NOT CODING')
        #continue
    clean(d,'cadd')
    clean(d,'kaviar')
    clean(d,'exac_nfe')
    clean(d,'exac_sas')
    clean(d,'exac_fin')
    clean(d,'exac_eas')
    clean(d,'exac_amr')
    clean(d,'exac_afr')
    clean(d,'exac_oth')
    clean(d,'exac_adj')
    clean(d,'1kg_eur')
    clean(d,'1kg_asn')
    clean(d,'1kg_amr')
    clean(d,'1kg_afr')
    d['genes']=list(set([cons['gene_id'] for cons in d['transcript_consequences']]))
    freq_cleanup(d)
    go_cleanup(d)
    canonical(d)
    gnomad_genomes(d)
    gnomad_exomes(d)
    kaviar(d)
    allele_freq(d)
    cadd(d)
    # try convert str which have a decimal point to number
    for k in d:
        try:
            d[k] = float(d[k])
        except:
            continue
    print(','.join([str(d.get(h,'')) for h in GeneticVariant_headers]),file=GeneticVariant_file)
    for cons in d['transcript_consequences']:
        if 'hgvsc' not in cons: continue
        if 'consequence_terms' in cons: cons['consequence_terms']=cons['consequence_terms'][0]
        print(','.join([str(cons.get(h,'')) for h in TranscriptVariant_headers]),file=TranscriptVariant_file)
        print(cons['transcript_id'],file=Transcript_file)
        print(cons['gene_id'],cons['gene_symbol'],sep=',',file=Gene_file)
        print(cons['transcript_id'],cons['gene_id'],sep=',',file=TranscriptToGene_file)
        print(d['variantId'],cons['gene_id'],sep=',',file=GeneticVariantToGene_file)
        print(d['variantId'],cons['hgvsc'],sep=',',file=GeneticVariantToTranscriptVariant_file)




