package com.graph.db.file.annotation.output;

import com.graph.db.file.annotation.domain.Annotation;
import com.graph.db.file.annotation.domain.TranscriptConsequence;

public enum OutputFileType implements OutputFile {

	GENE("Gene", TranscriptConsequence.class) {
		@Override
		public String[] getHeader() {
			return new String[] {
					"gene_id",
					"gene_symbol"
					};
		}
	},
	GENE_TO_VARIANT("GeneToVariant", TranscriptConsequence.class) {
		@Override
		public String[] getHeader() {
			return new String[] {
					"gene_id",
					"variant_id"
					};
		}
	},
	VARIANT_TO_ANNOTATION("AnnotationToVariant", Annotation.class) {
		@Override
		public String[] getHeader() {
			return new String[] {
					"variant_id",
					"variant_id",
					"HET_COUNT",
					"WT_COUNT",
					"HOM_COUNT",
					"MISS_COUNT",
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
					};
		}
	};
	
	private final String fileTag;
	private final Class<?> beanClass;

	private OutputFileType(String fileTag, Class<?> beanClass) {
		this.fileTag = fileTag;
		this.beanClass = beanClass;
	}
	
	@Override
	public String getFileTag() {
		return fileTag;
	}

	@Override
	public Class<?> getBeanClass() {
		return beanClass;
	}
}
