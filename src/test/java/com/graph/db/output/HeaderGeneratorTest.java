package com.graph.db.output;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.EnumSet;

import org.junit.Test;

public class HeaderGeneratorTest {
	
	private HeaderGenerator headerGenerator = new HeaderGenerator();

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
				expectedHeader = "variantId:ID(GeneticVariant),HET_COUNT:int,WT_COUNT:int,HOM_COUNT:int,MISS_COUNT:int,allele_freq:double,hasExac:boolean,EXAC.AC,EXAC.AC_Adj,EXAC.AC_AFR,EXAC.AC_AMR,EXAC.AC_CONSANGUINEOUS,EXAC.AC_EAS,EXAC.AC_FEMALE,EXAC.AC_FIN,EXAC.AC_Hemi,EXAC.AC_Het,EXAC.AC_Hom,EXAC.AC_MALE,EXAC.AC_NFE,EXAC.AC_OTH,EXAC.AC_POPMAX,EXAC.AC_SAS,EXAC.AF,EXAC.ALT,EXAC.AN,EXAC.AN_Adj,EXAC.AN_AFR,EXAC.AN_AMR,EXAC.AN_CONSANGUINEOUS,EXAC.AN_EAS,EXAC.AN_FEMALE,EXAC.AN_FIN,EXAC.AN_MALE,EXAC.AN_NFE,EXAC.AN_OTH,EXAC.AN_POPMAX,EXAC.AN_SAS,EXAC.BaseQRankSum,EXAC.clinvar_conflicted,EXAC.clinvar_measureset_id,EXAC.clinvar_mut,EXAC.clinvar_pathogenic,EXAC.ClippingRankSum,EXAC.culprit,EXAC.DOUBLETON_DIST,EXAC.DP,EXAC.ESP_AC,EXAC.ESP_AF_GLOBAL,EXAC.ESP_AF_POPMAX,EXAC.FS,EXAC.GQ_MEAN,EXAC.GQ_STDDEV,EXAC.Hemi_AFR,EXAC.Hemi_AMR,EXAC.Hemi_EAS,EXAC.Hemi_FIN,EXAC.Hemi_NFE,EXAC.Hemi_OTH,EXAC.Hemi_SAS,EXAC.Het_AFR,EXAC.Het_AMR,EXAC.Het_EAS,EXAC.Het_FIN,EXAC.Het_NFE,EXAC.Het_OTH,EXAC.Het_SAS,EXAC.hgvs,EXAC.Hom_AFR,EXAC.Hom_AMR,EXAC.Hom_CONSANGUINEOUS,EXAC.Hom_EAS,EXAC.Hom_FIN,EXAC.Hom_NFE,EXAC.Hom_OTH,EXAC.Hom_SAS,EXAC.InbreedingCoeff,EXAC.index,EXAC.K1_RUN,EXAC.K2_RUN,EXAC.K3_RUN,EXAC.KG_AC,EXAC.KG_AF_GLOBAL,EXAC.KG_AF_POPMAX,EXAC.MQ,EXAC.MQ0,EXAC.MQRankSum,EXAC.NCC,EXAC.POPMAX,EXAC.POS,EXAC.QD,EXAC.ReadPosRankSum,EXAC.REF,EXAC.VQSLOD";
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
				expectedHeader = "hgvsc:ID(TranscriptVariant),impact,gene_symbol_source,cdna_end:int,cdna_start:int,cadd,variant_allele";
				break;
			case TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM:
				expectedHeader = ":START_ID(TranscriptVariant),:END_ID(ConsequenceTerm)";
				break;
			default:
				throw new IllegalStateException();
			}
			
			String actualHeader = headerGenerator.generateHeadersForOutputFileTypes(EnumSet.of(outputFileType)).values().iterator().next();
			assertThat("For: " + outputFileType.toString(), actualHeader, is(expectedHeader));
		}
	}

}
