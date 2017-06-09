
export JAVA_HOME="/share/apps/java/"

cd /home/rmhanpo/neo4j-community-3.1.1/

sort -u -o import/Transcript.csv import/Transcript.csv
sort -u -o import/Gene.csv import/Gene.csv 
sort -u -o import/TranscriptVariant.csv import/TranscriptVariant.csv 

rm -f data/databases/graph.db/*

./bin/neo4j-admin import --database graph.db \
--id-type string \
--nodes:Transcript import/Transcript-header.csv,import/Transcript.csv \
--nodes:TranscriptVariant import/TranscriptVariant-header.csv,import/TranscriptVariant.csv \
--nodes:Gene import/Gene-header.csv,import/Gene.csv \
--nodes:GeneticVariant import/GeneticVariant-header.csv,import/GeneticVariant.csv \
--nodes:Person import/Person.csv \
--relationships:GeneticVariantToGene import/GeneticVariantToGene.csv \
--relationships:GeneticVariantToTranscriptVariant import/GeneticVariantToTranscriptVariant.csv \
--relationships:TranscriptToGene import/TranscriptToGene.csv \
--relationships:HomVariantToPerson import/HomVariantToPerson.csv \
--relationships:HetVariantToPerson import/HetVariantToPerson.csv 

