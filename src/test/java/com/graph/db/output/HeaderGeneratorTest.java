package com.graph.db.output;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.EnumSet;

import org.junit.Test;

public class HeaderGeneratorTest {
	
	private final HeaderGenerator headerGenerator = new HeaderGenerator();

	@Test
	public void testHeaderGenerator() {
		for (OutputFileType outputFileType : OutputFileType.values()) {
			final String expectedHeader;
			switch (outputFileType) {
			case CONSEQUENCE_TERM:
				expectedHeader = "consequenceTerm:ID(ConsequenceTerm)";
				break;
			case GENE:
				expectedHeader = "gene_id:ID(Gene),gene_name,hgnc_id:int";
				break;
			case GENE_TO_GENETIC_VARIANT:
				expectedHeader = ":START_ID(Gene),:END_ID(GeneticVariant)";
				break;
			case GENE_TO_TERM:
				expectedHeader = ":START_ID(Gene),:END_ID(Term)";
				break;
			case GENETIC_VARIANT:
				expectedHeader = "variantId:ID(GeneticVariant),allele_string,start:int,end:int,seq_region_name,most_severe_consequence,strand:int,AC:int,allele_freq:double,AN:int,ExcessHet:double,FS:double,InbreedingCoeff:double,MLEAC:int,MLEAF:double,MQ:double,MQRankSum:double,ReadPosRankSum:double,VQSLOD:double,Culprit,gnomad_exomes_AC_AFR:int,gnomad_exomes_AC_AMR:int,gnomad_exomes_AC_ASJ:int,gnomad_exomes_AC_raw:int,gnomad_exomes_AF_NFE:double,gnomad_exomes_AF_OTH:double,gnomad_exomes_AF_raw:double,gnomad_exomes_AN_AFR:int,gnomad_exomes_AC_EAS:int,gnomad_exomes_AC_Female:int,gnomad_exomes_AC_OTH:int,gnomad_exomes_AC_NFE:int,gnomad_exomes_AC_Male:int,gnomad_exomes_AC_FIN:int,gnomad_exomes_AF_AFR:double,gnomad_exomes_AF_AMR:double,gnomad_exomes_AF_ASJ:double,gnomad_exomes_AF_EAS:double,gnomad_exomes_AF_FIN:double,gnomad_exomes_AF_Female:double,gnomad_exomes_AF_Male:double,gnomad_exomes_AN_AMR:int,gnomad_exomes_AN_ASJ:int,gnomad_exomes_AN_EAS:int,gnomad_exomes_AN_FIN:int,gnomad_exomes_AN_Female:int,gnomad_exomes_AN_Male:int,gnomad_exomes_AN_NFE:int,gnomad_exomes_AN_OTH:int,gnomad_exomes_AN_raw:int,gnomad_exomes_Hom_AFR:int,gnomad_exomes_Hom_AMR:int,gnomad_exomes_Hom_ASJ:int,gnomad_exomes_Hom_EAS:int,gnomad_exomes_Hom_FIN:int,gnomad_exomes_Hom_Female:int,gnomad_exomes_Hom_Male:int,gnomad_exomes_Hom_NFE:int,gnomad_exomes_Hom_OTH:int,gnomad_exomes_Hom_raw:int,gnomad_exomes_Hom:int,gnomad_genomes_AC_AFR:int,gnomad_genomes_AC_AMR:int,gnomad_genomes_AC_ASJ:int,gnomad_genomes_AC_raw:int,gnomad_genomes_AF_NFE:double,gnomad_genomes_AF_OTH:double,gnomad_genomes_AF_raw:double,gnomad_genomes_AN_AFR:int,gnomad_genomes_AC_EAS:int,gnomad_genomes_AC_Female:int,gnomad_genomes_AC_OTH:int,gnomad_genomes_AC_NFE:int,gnomad_genomes_AC_Male:int,gnomad_genomes_AC_FIN:int,gnomad_genomes_AF_AFR:double,gnomad_genomes_AF_AMR:double,gnomad_genomes_AF_ASJ:double,gnomad_genomes_AF_EAS:double,gnomad_genomes_AF_FIN:double,gnomad_genomes_AF_Female:double,gnomad_genomes_AF_Male:double,gnomad_genomes_AN_AMR:int,gnomad_genomes_AN_ASJ:int,gnomad_genomes_AN_EAS:int,gnomad_genomes_AN_FIN:int,gnomad_genomes_AN_Female:int,gnomad_genomes_AN_Male:int,gnomad_genomes_AN_NFE:int,gnomad_genomes_AN_OTH:int,gnomad_genomes_AN_raw:int,gnomad_genomes_Hom_AFR:int,gnomad_genomes_Hom_AMR:int,gnomad_genomes_Hom_ASJ:int,gnomad_genomes_Hom_EAS:int,gnomad_genomes_Hom_FIN:int,gnomad_genomes_Hom_Female:int,gnomad_genomes_Hom_Male:int,gnomad_genomes_Hom_NFE:int,gnomad_genomes_Hom_OTH:int,gnomad_genomes_Hom_raw:int,gnomad_genomes_Hom:int,kaviar_AN:int,kaviar_AC:int,kaviar_AF:double,exac_AF:double,exac_AF_ADJ:double,exac_AF_AFR:int,exac_AF_AMR:double,exac_AF_CONSANGUINEOUS:int,exac_AF_EAS:int,exac_AF_FEMALE:int,exac_AF_FIN:int,exac_AF_MALE:double,exac_AF_NFE:int,exac_AF_OTH:int,exac_AF_POPMAX:double,exac_AF_SAS:double,cadd_phred:int,cadd_raw:double";
				break;
			case GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT:
				expectedHeader = ":START_ID(GeneticVariant),:END_ID(TranscriptVariant)";
				break;
			case TRANSCRIPT:
				expectedHeader = "transcript_id:ID(Transcript),canonical:int,strand:int,ensembl_protein_id,swissprot_protein_id";
				break;
			case TRANSCRIPT_TO_GENE:
				expectedHeader = ":START_ID(Transcript),:END_ID(Gene)";
				break;
			case TRANSCRIPT_TO_TRANSCRIPT_VARIANT:
				expectedHeader = ":START_ID(Transcript),:END_ID(TranscriptVariant)";
				break;
			case TRANSCRIPT_VARIANT:
				expectedHeader = "hgvsc:ID(TranscriptVariant),hgvsp,impact,intron,exon";
				break;
			case TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM:
				expectedHeader = ":START_ID(TranscriptVariant),:END_ID(ConsequenceTerm)";
				break;
			case TERM:
				expectedHeader = "termId:ID(Term),name";
				break;
			case TERM_TO_DESCENDANT_TERMS:
				expectedHeader = ":START_ID(Term),:END_ID(Term)";
				break;
			case TERM_TO_PARENT_TERM:
				expectedHeader = ":START_ID(Term),:END_ID(Term)";
				break;
			case PERSON:
				expectedHeader = "personId:ID(Person)";
				break;
			case PERSON_TO_OBSERVED_TERM:
				expectedHeader = ":START_ID(Person),:END_ID(Term)";
				break;
			case PERSON_TO_NON_OBSERVED_TERM:
				expectedHeader = ":START_ID(Person),:END_ID(Term)";
				break;
			case HET_VARIANT_TO_PERSON:
			case HOM_VARIANT_TO_PERSON:
				continue;
			default:
				throw new IllegalStateException("outputFileType: " + outputFileType);
			}
			
			String actualHeader = headerGenerator.generateHeadersForOutputFileTypes(EnumSet.of(outputFileType)).values().iterator().next();
			assertThat("For: " + outputFileType.toString(), actualHeader, is(expectedHeader));
		}
	}
}
