package com.graph.db.output;

import org.apache.commons.lang3.tuple.Pair;

import com.graph.db.file.annotation.domain.GeneticVariant;
import com.graph.db.file.annotation.domain.TranscriptConsequence;

public enum OutputFileType {

	GENE_TO_TERM("GeneToTerm", null, new String[] {
			"gene_id",
			"HPO-ID"
			}),
	//TODO get rid of Pair usage
	GENE_TO_GENETIC_VARIANT("GeneToGeneticVariant", Pair.class, new String[] {
			"left",
			"right"
			}),
	GENETIC_VARIANT("GeneticVariant", GeneticVariant.class, new String[] {
			"variant_id",
			"HET_COUNT",
			"WT_COUNT",
			"HOM_COUNT",
			"MISS_COUNT",
			"allele_freq",
			"hasExac",
			"EXAC.AC",
			"EXAC.AC_Adj",
			"EXAC.AC_AFR",
			"EXAC.AC_AMR",
			"EXAC.AC_CONSANGUINEOUS",
			"EXAC.AC_EAS",
			"EXAC.AC_FEMALE",
			"EXAC.AC_FIN",
			"EXAC.AC_Hemi",
			"EXAC.AC_Het",
			"EXAC.AC_Hom",
			"EXAC.AC_MALE",
			"EXAC.AC_NFE",
			"EXAC.AC_OTH",
			"EXAC.AC_POPMAX",
			"EXAC.AC_SAS",
			"EXAC.AF",
			"EXAC.ALT",
			"EXAC.AN",
			"EXAC.AN_Adj",
			"EXAC.AN_AFR",
			"EXAC.AN_AMR",
			"EXAC.AN_CONSANGUINEOUS",
			"EXAC.AN_EAS",
			"EXAC.AN_FEMALE",
			"EXAC.AN_FIN",
			"EXAC.AN_MALE",
			"EXAC.AN_NFE",
			"EXAC.AN_OTH",
			"EXAC.AN_POPMAX",
			"EXAC.AN_SAS",
			"EXAC.BaseQRankSum",
			"EXAC.clinvar_conflicted",
			"EXAC.clinvar_measureset_id",
			"EXAC.clinvar_mut",
			"EXAC.clinvar_pathogenic",
			"EXAC.ClippingRankSum",
			"EXAC.culprit",
			"EXAC.DOUBLETON_DIST",
			"EXAC.DP",
			"EXAC.ESP_AC",
			"EXAC.ESP_AF_GLOBAL",
			"EXAC.ESP_AF_POPMAX",
			"EXAC.FS",
			"EXAC.GQ_MEAN",
			"EXAC.GQ_STDDEV",
			"EXAC.Hemi_AFR",
			"EXAC.Hemi_AMR",
			"EXAC.Hemi_EAS",
			"EXAC.Hemi_FIN",
			"EXAC.Hemi_NFE",
			"EXAC.Hemi_OTH",
			"EXAC.Hemi_SAS",
			"EXAC.Het_AFR",
			"EXAC.Het_AMR",
			"EXAC.Het_EAS",
			"EXAC.Het_FIN",
			"EXAC.Het_NFE",
			"EXAC.Het_OTH",
			"EXAC.Het_SAS",
			"EXAC.hgvs",
			"EXAC.Hom_AFR",
			"EXAC.Hom_AMR",
			"EXAC.Hom_CONSANGUINEOUS",
			"EXAC.Hom_EAS",
			"EXAC.Hom_FIN",
			"EXAC.Hom_NFE",
			"EXAC.Hom_OTH",
			"EXAC.Hom_SAS",
			"EXAC.InbreedingCoeff",
			"EXAC.index",
			"EXAC.K1_RUN",
			"EXAC.K2_RUN",
			"EXAC.K3_RUN",
			"EXAC.KG_AC",
			"EXAC.KG_AF_GLOBAL",
			"EXAC.KG_AF_POPMAX",
			"EXAC.MQ",
			"EXAC.MQ0",
			"EXAC.MQRankSum",
			"EXAC.NCC",
			"EXAC.POPMAX",
			"EXAC.POS",
			"EXAC.QD",
			"EXAC.ReadPosRankSum",
			"EXAC.REF",
			"EXAC.VQSLOD"
			}),
	TRANSCRIPT_VARIANT("TranscriptVariant", TranscriptConsequence.class, new String[] {
			"hgvsc",
			"impact",
			"gene_symbol_source",
			"cdna_end",
			"cdna_start",
			"cadd",
			"variant_allele",
			}),
	GENETIC_VARIANT_TO_TRANSCRIPT_VARIANT("GeneticVariantToTranscriptVariant", TranscriptConsequence.class, new String[] {
			"variant_id",
			"hgvsc"
			}),
	TRANSCRIPT_TO_TRANSCRIPT_VARIANT("TranscriptToTranscriptVariant", TranscriptConsequence.class, new String[] {
			"transcript_id",
			"hgvsc"
			}),
	CONSEQUENCE_TERM("ConsequenceTerm", String.class, new String[] {
			"this",
			}),
	//TODO get rid of Pair usage
	TRANSCRIPT_VARIANT_TO_CONSEQUENCE_TERM("TranscriptVariantToConsequenceTerm", Pair.class, new String[] {
			"left",
			"right"
			}),
	GENE("Gene", null, new String[] {
			"gene_id",
			"gene_name"
			}),
	TRANSCRIPT("Transcript", null, new String[] {
			"transcript_id",
			}),
	TRANSCRIPT_TO_GENE("TranscriptToGene", null, new String[] {
			"transcript_id",
			"gene_id"
			}),
	;
	
	private final String fileTag;
	private final Class<?> beanClass;
	private final String[] header;

	private OutputFileType(String fileTag, Class<?> beanClass, String[] header) {
		this.fileTag = fileTag;
		this.beanClass = beanClass;
		this.header = header;
	}
	
	public String getFileTag() {
		return fileTag;
	}

	public Class<?> getBeanClass() {
		return beanClass;
	}

	public String[] getHeader() {
		return header;
	}
}
