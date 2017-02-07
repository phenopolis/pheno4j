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
				expectedHeader = "gene_id:ID(Gene),gene_name";
				break;
			case GENE_TO_GENETIC_VARIANT:
				expectedHeader = ":START_ID(Gene),:END_ID(GeneticVariant)";
				break;
			case GENE_TO_TERM:
				expectedHeader = ":START_ID(Gene),:END_ID(Term)";
				break;
			case GENETIC_VARIANT:
				expectedHeader = "variantId:ID(GeneticVariant),HET_COUNT:int,WT_COUNT:int,HOM_COUNT:int,MISS_COUNT:int,allele_freq:double,hasExac:boolean,EXAC_AC,EXAC_AC_Adj,EXAC_AC_AFR,EXAC_AC_AMR,EXAC_AC_CONSANGUINEOUS,EXAC_AC_EAS,EXAC_AC_FEMALE,EXAC_AC_FIN,EXAC_AC_Hemi,EXAC_AC_Het,EXAC_AC_Hom,EXAC_AC_MALE,EXAC_AC_NFE,EXAC_AC_OTH,EXAC_AC_POPMAX,EXAC_AC_SAS,EXAC_AF,EXAC_ALT,EXAC_AN,EXAC_AN_Adj,EXAC_AN_AFR,EXAC_AN_AMR,EXAC_AN_CONSANGUINEOUS,EXAC_AN_EAS,EXAC_AN_FEMALE,EXAC_AN_FIN,EXAC_AN_MALE,EXAC_AN_NFE,EXAC_AN_OTH,EXAC_AN_POPMAX,EXAC_AN_SAS,EXAC_BaseQRankSum,EXAC_clinvar_conflicted,EXAC_clinvar_measureset_id,EXAC_clinvar_mut,EXAC_clinvar_pathogenic,EXAC_ClippingRankSum,EXAC_culprit,EXAC_DOUBLETON_DIST,EXAC_DP,EXAC_ESP_AC,EXAC_ESP_AF_GLOBAL,EXAC_ESP_AF_POPMAX,EXAC_FS,EXAC_GQ_MEAN,EXAC_GQ_STDDEV,EXAC_Hemi_AFR,EXAC_Hemi_AMR,EXAC_Hemi_EAS,EXAC_Hemi_FIN,EXAC_Hemi_NFE,EXAC_Hemi_OTH,EXAC_Hemi_SAS,EXAC_Het_AFR,EXAC_Het_AMR,EXAC_Het_EAS,EXAC_Het_FIN,EXAC_Het_NFE,EXAC_Het_OTH,EXAC_Het_SAS,EXAC_hgvs,EXAC_Hom_AFR,EXAC_Hom_AMR,EXAC_Hom_CONSANGUINEOUS,EXAC_Hom_EAS,EXAC_Hom_FIN,EXAC_Hom_NFE,EXAC_Hom_OTH,EXAC_Hom_SAS,EXAC_InbreedingCoeff,EXAC_index,EXAC_K1_RUN,EXAC_K2_RUN,EXAC_K3_RUN,EXAC_KG_AC,EXAC_KG_AF_GLOBAL,EXAC_KG_AF_POPMAX,EXAC_MQ,EXAC_MQ0,EXAC_MQRankSum,EXAC_NCC,EXAC_POPMAX,EXAC_POS,EXAC_QD,EXAC_ReadPosRankSum,EXAC_REF,EXAC_VQSLOD";
				break;
			case GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT:
				expectedHeader = ":START_ID(GeneticVariant),:END_ID(TranscriptVariant)";
				break;
			case TRANSCRIPT:
				expectedHeader = "transcript_id:ID(Transcript)";
				break;
			case TRANSCRIPT_TO_GENE:
				expectedHeader = ":START_ID(Transcript),:END_ID(Gene)";
				break;
			case TRANSCRIPT_TO_TRANSCRIPT_VARIANT:
				expectedHeader = ":START_ID(Transcript),:END_ID(TranscriptVariant)";
				break;
			case TRANSCRIPT_VARIANT:
				expectedHeader = "hgvsc:ID(TranscriptVariant),impact,gene_symbol_source,cdna_end:int,cdna_start:int,cadd:double,variant_allele";
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
			case GENETIC_VARIANT_TO_PERSON:
				continue;
			default:
				throw new IllegalStateException("outputFileType: " + outputFileType);
			}
			
			String actualHeader = headerGenerator.generateHeadersForOutputFileTypes(EnumSet.of(outputFileType)).values().iterator().next();
			assertThat("For: " + outputFileType.toString(), actualHeader, is(expectedHeader));
		}
	}
}
