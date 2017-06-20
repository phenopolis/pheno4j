set +x

cwd=`pwd`

export JAVA_HOME="/share/apps/java/"

cd /home/rmhanpo/neo4j-community-3.1.1/

sort -u -o import/Transcript.csv import/Transcript.csv
sort -u -o import/Gene.csv import/Gene.csv 
sort -u -o import/TranscriptVariant.csv import/TranscriptVariant.csv 


#rm -f data/databases/graph.db/*
rm -rf /SAN/vyplab/NCMD_raw/neo4j2/final/graph.db/*

./bin/neo4j-import --into /SAN/vyplab/NCMD_raw/neo4j2/final/graph.db \
--id-type string \
--nodes:Transcript $cwd/Transcript-header.csv,import/Transcript.csv \
--nodes:TranscriptVariant $cwd/TranscriptVariant-header.csv,import/TranscriptVariant.csv \
--nodes:Gene $cwd/Gene-header.csv,import/Gene.csv \
--nodes:GeneticVariant $cwd/GeneticVariant-header.csv,import/GeneticVariant.csv \
--nodes:Person import/Person.csv \
--relationships:GeneticVariantToGene import/GeneticVariantToGene.csv \
--relationships:GeneticVariantToTranscriptVariant import/GeneticVariantToTranscriptVariant.csv \
--relationships:TranscriptToGene import/TranscriptToGene.csv \
--relationships:HomVariantToPerson import/HomVariantToPerson.csv \
--relationships:HetVariantToPerson import/HetVariantToPerson.csv \
--bad-tolerance 10000000 \
--skip-bad-relationships true

# link the database
ln -s /SAN/vyplab/NCMD_raw/neo4j2/final/graph.db /home/rmhanpo/neo4j-community-3.1.1/data/databases/

