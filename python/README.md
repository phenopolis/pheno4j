Python implementation of parsers to generate csv files from VCF and JSON for bulk import and LOAD_CSV.

```
python ~/pheno4j/python/annotation_parser.py --file VEP.json --importdir ~/neo4j-community-3.1.1/import/
```

```
python ~/pheno4j/python/vcf_parser.py --file all.vcf.gz --importdir ~/neo4j-community-3.1.1/import/  
```

```
bash bulk_import.sh
```
