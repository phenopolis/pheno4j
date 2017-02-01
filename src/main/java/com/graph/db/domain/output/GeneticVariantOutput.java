package com.graph.db.domain.output;

import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.output.annotation.Id;
import com.graph.db.output.Neo4jMapping;

public class GeneticVariantOutput {
	
	@Id(mapping = Neo4jMapping.GeneticVariant, name = "variantId")
	private final String variant_id;
	
	private final Integer HET_COUNT;
	private final Integer WT_COUNT;
	private final Integer HOM_COUNT;
	private final Integer MISS_COUNT;
	private final Double allele_freq;
	private final Boolean hasExac;
	
	private String EXAC_AC;
	private String EXAC_AC_Adj;
	private String EXAC_AC_AFR;
	private String EXAC_AC_AMR;
	private String EXAC_AC_CONSANGUINEOUS;
	private String EXAC_AC_EAS;
	private String EXAC_AC_FEMALE;
	private String EXAC_AC_FIN;
	private String EXAC_AC_Hemi;
	private String EXAC_AC_Het;
	private String EXAC_AC_Hom;
	private String EXAC_AC_MALE;
	private String EXAC_AC_NFE;
	private String EXAC_AC_OTH;
	private String EXAC_AC_POPMAX;
	private String EXAC_AC_SAS;
	private String EXAC_AF;
	private String EXAC_ALT;
	private String EXAC_AN;
	private String EXAC_AN_Adj;
	private String EXAC_AN_AFR;
	private String EXAC_AN_AMR;
	private String EXAC_AN_CONSANGUINEOUS;
	private String EXAC_AN_EAS;
	private String EXAC_AN_FEMALE;
	private String EXAC_AN_FIN;
	private String EXAC_AN_MALE;
	private String EXAC_AN_NFE;
	private String EXAC_AN_OTH;
	private String EXAC_AN_POPMAX;
	private String EXAC_AN_SAS;
	private String EXAC_BaseQRankSum;
	private String EXAC_clinvar_conflicted;
	private String EXAC_clinvar_measureset_id;
	private String EXAC_clinvar_mut;
	private String EXAC_clinvar_pathogenic;
	private String EXAC_ClippingRankSum;
	private String EXAC_culprit;
	private String EXAC_DOUBLETON_DIST;
	private String EXAC_DP;
	private String EXAC_ESP_AC;
	private String EXAC_ESP_AF_GLOBAL;
	private String EXAC_ESP_AF_POPMAX;
	private String EXAC_FS;
	private String EXAC_GQ_MEAN;
	private String EXAC_GQ_STDDEV;
	private String EXAC_Hemi_AFR;
	private String EXAC_Hemi_AMR;
	private String EXAC_Hemi_EAS;
	private String EXAC_Hemi_FIN;
	private String EXAC_Hemi_NFE;
	private String EXAC_Hemi_OTH;
	private String EXAC_Hemi_SAS;
	private String EXAC_Het_AFR;
	private String EXAC_Het_AMR;
	private String EXAC_Het_EAS;
	private String EXAC_Het_FIN;
	private String EXAC_Het_NFE;
	private String EXAC_Het_OTH;
	private String EXAC_Het_SAS;
	private String EXAC_hgvs;
	private String EXAC_Hom_AFR;
	private String EXAC_Hom_AMR;
	private String EXAC_Hom_CONSANGUINEOUS;
	private String EXAC_Hom_EAS;
	private String EXAC_Hom_FIN;
	private String EXAC_Hom_NFE;
	private String EXAC_Hom_OTH;
	private String EXAC_Hom_SAS;
	private String EXAC_InbreedingCoeff;
	private String EXAC_index;
	private String EXAC_K1_RUN;
	private String EXAC_K2_RUN;
	private String EXAC_K3_RUN;
	private String EXAC_KG_AC;
	private String EXAC_KG_AF_GLOBAL;
	private String EXAC_KG_AF_POPMAX;
	private String EXAC_MQ;
	private String EXAC_MQ0;
	private String EXAC_MQRankSum;
	private String EXAC_NCC;
	private String EXAC_POPMAX;
	private String EXAC_POS;
	private String EXAC_QD;
	private String EXAC_ReadPosRankSum;
	private String EXAC_REF;
	private String EXAC_VQSLOD;
	
	public GeneticVariantOutput(GeneticVariant geneticVariant) {
		this.variant_id = geneticVariant.getVariant_id();

		this.HET_COUNT = geneticVariant.getHET_COUNT();
		this.WT_COUNT = geneticVariant.getWT_COUNT();
		this.HOM_COUNT = geneticVariant.getHOM_COUNT();
		this.MISS_COUNT = geneticVariant.getMISS_COUNT();
		this.allele_freq = geneticVariant.getAllele_freq();
		this.hasExac = geneticVariant.isHasExac();

		if (geneticVariant.getEXAC() != null) {
			this.EXAC_AC = geneticVariant.getEXAC().getAC();
			this.EXAC_AC_Adj = geneticVariant.getEXAC().getAC_Adj();
			this.EXAC_AC_AFR = geneticVariant.getEXAC().getAC_AFR();
			this.EXAC_AC_AMR = geneticVariant.getEXAC().getAC_AMR();
			this.EXAC_AC_CONSANGUINEOUS = geneticVariant.getEXAC().getAC_CONSANGUINEOUS();
			this.EXAC_AC_EAS = geneticVariant.getEXAC().getAC_EAS();
			this.EXAC_AC_FEMALE = geneticVariant.getEXAC().getAC_FEMALE();
			this.EXAC_AC_FIN = geneticVariant.getEXAC().getAC_FIN();
			this.EXAC_AC_Hemi = geneticVariant.getEXAC().getAC_Hemi();
			this.EXAC_AC_Het = geneticVariant.getEXAC().getAC_Het();
			this.EXAC_AC_Hom = geneticVariant.getEXAC().getAC_Hom();
			this.EXAC_AC_MALE = geneticVariant.getEXAC().getAC_MALE();
			this.EXAC_AC_NFE = geneticVariant.getEXAC().getAC_NFE();
			this.EXAC_AC_OTH = geneticVariant.getEXAC().getAC_OTH();
			this.EXAC_AC_POPMAX = geneticVariant.getEXAC().getAC_POPMAX();
			this.EXAC_AC_SAS = geneticVariant.getEXAC().getAC_SAS();
			this.EXAC_AF = geneticVariant.getEXAC().getAF();
			this.EXAC_ALT = geneticVariant.getEXAC().getALT();
			this.EXAC_AN = geneticVariant.getEXAC().getAN();
			this.EXAC_AN_Adj = geneticVariant.getEXAC().getAN_Adj();
			this.EXAC_AN_AFR = geneticVariant.getEXAC().getAN_AFR();
			this.EXAC_AN_AMR = geneticVariant.getEXAC().getAN_AMR();
			this.EXAC_AN_CONSANGUINEOUS = geneticVariant.getEXAC().getAN_CONSANGUINEOUS();
			this.EXAC_AN_EAS = geneticVariant.getEXAC().getAN_EAS();
			this.EXAC_AN_FEMALE = geneticVariant.getEXAC().getAN_FEMALE();
			this.EXAC_AN_FIN = geneticVariant.getEXAC().getAN_FIN();
			this.EXAC_AN_MALE = geneticVariant.getEXAC().getAN_MALE();
			this.EXAC_AN_NFE = geneticVariant.getEXAC().getAN_NFE();
			this.EXAC_AN_OTH = geneticVariant.getEXAC().getAN_OTH();
			this.EXAC_AN_POPMAX = geneticVariant.getEXAC().getAN_POPMAX();
			this.EXAC_AN_SAS = geneticVariant.getEXAC().getAN_SAS();
			this.EXAC_BaseQRankSum = geneticVariant.getEXAC().getBaseQRankSum();
			this.EXAC_clinvar_conflicted = geneticVariant.getEXAC().getClinvar_conflicted();
			this.EXAC_clinvar_measureset_id = geneticVariant.getEXAC().getClinvar_measureset_id();
			this.EXAC_clinvar_mut = geneticVariant.getEXAC().getClinvar_mut();
			this.EXAC_clinvar_pathogenic = geneticVariant.getEXAC().getClinvar_pathogenic();
			this.EXAC_ClippingRankSum = geneticVariant.getEXAC().getClippingRankSum();
			this.EXAC_culprit = geneticVariant.getEXAC().getCulprit();
			this.EXAC_DOUBLETON_DIST = geneticVariant.getEXAC().getDOUBLETON_DIST();
			this.EXAC_DP = geneticVariant.getEXAC().getDP();
			this.EXAC_ESP_AC = geneticVariant.getEXAC().getESP_AC();
			this.EXAC_ESP_AF_GLOBAL = geneticVariant.getEXAC().getESP_AF_GLOBAL();
			this.EXAC_ESP_AF_POPMAX = geneticVariant.getEXAC().getESP_AF_POPMAX();
			this.EXAC_FS = geneticVariant.getEXAC().getFS();
			this.EXAC_GQ_MEAN = geneticVariant.getEXAC().getGQ_MEAN();
			this.EXAC_GQ_STDDEV = geneticVariant.getEXAC().getGQ_STDDEV();
			this.EXAC_Hemi_AFR = geneticVariant.getEXAC().getHemi_AFR();
			this.EXAC_Hemi_AMR = geneticVariant.getEXAC().getHemi_AMR();
			this.EXAC_Hemi_EAS = geneticVariant.getEXAC().getHemi_EAS();
			this.EXAC_Hemi_FIN = geneticVariant.getEXAC().getHemi_FIN();
			this.EXAC_Hemi_NFE = geneticVariant.getEXAC().getHemi_NFE();
			this.EXAC_Hemi_OTH = geneticVariant.getEXAC().getHemi_OTH();
			this.EXAC_Hemi_SAS = geneticVariant.getEXAC().getHemi_SAS();
			this.EXAC_Het_AFR = geneticVariant.getEXAC().getHet_AFR();
			this.EXAC_Het_AMR = geneticVariant.getEXAC().getHet_AMR();
			this.EXAC_Het_EAS = geneticVariant.getEXAC().getHet_EAS();
			this.EXAC_Het_FIN = geneticVariant.getEXAC().getHet_FIN();
			this.EXAC_Het_NFE = geneticVariant.getEXAC().getHet_NFE();
			this.EXAC_Het_OTH = geneticVariant.getEXAC().getHet_OTH();
			this.EXAC_Het_SAS = geneticVariant.getEXAC().getHet_SAS();
			this.EXAC_hgvs = geneticVariant.getEXAC().getHgvs();
			this.EXAC_Hom_AFR = geneticVariant.getEXAC().getHom_AFR();
			this.EXAC_Hom_AMR = geneticVariant.getEXAC().getHom_AMR();
			this.EXAC_Hom_CONSANGUINEOUS = geneticVariant.getEXAC().getHom_CONSANGUINEOUS();
			this.EXAC_Hom_EAS = geneticVariant.getEXAC().getHom_EAS();
			this.EXAC_Hom_FIN = geneticVariant.getEXAC().getHom_FIN();
			this.EXAC_Hom_NFE = geneticVariant.getEXAC().getHom_NFE();
			this.EXAC_Hom_OTH = geneticVariant.getEXAC().getHom_OTH();
			this.EXAC_Hom_SAS = geneticVariant.getEXAC().getHom_SAS();
			this.EXAC_InbreedingCoeff = geneticVariant.getEXAC().getInbreedingCoeff();
			this.EXAC_index = geneticVariant.getEXAC().getIndex();
			this.EXAC_K1_RUN = geneticVariant.getEXAC().getK1_RUN();
			this.EXAC_K2_RUN = geneticVariant.getEXAC().getK2_RUN();
			this.EXAC_K3_RUN = geneticVariant.getEXAC().getK3_RUN();
			this.EXAC_KG_AC = geneticVariant.getEXAC().getKG_AC();
			this.EXAC_KG_AF_GLOBAL = geneticVariant.getEXAC().getKG_AF_GLOBAL();
			this.EXAC_KG_AF_POPMAX = geneticVariant.getEXAC().getKG_AF_POPMAX();
			this.EXAC_MQ = geneticVariant.getEXAC().getMQ();
			this.EXAC_MQ0 = geneticVariant.getEXAC().getMQ0();
			this.EXAC_MQRankSum = geneticVariant.getEXAC().getMQRankSum();
			this.EXAC_NCC = geneticVariant.getEXAC().getNCC();
			this.EXAC_POPMAX = geneticVariant.getEXAC().getPOPMAX();
			this.EXAC_POS = geneticVariant.getEXAC().getPOS();
			this.EXAC_QD = geneticVariant.getEXAC().getQD();
			this.EXAC_ReadPosRankSum = geneticVariant.getEXAC().getReadPosRankSum();
			this.EXAC_REF = geneticVariant.getEXAC().getREF();
			this.EXAC_VQSLOD = geneticVariant.getEXAC().getVQSLOD();
		}
	}

	public String getVariant_id() {
		return variant_id;
	}

	public Integer getHET_COUNT() {
		return HET_COUNT;
	}

	public Integer getWT_COUNT() {
		return WT_COUNT;
	}

	public Integer getHOM_COUNT() {
		return HOM_COUNT;
	}

	public Integer getMISS_COUNT() {
		return MISS_COUNT;
	}

	public Double getAllele_freq() {
		return allele_freq;
	}

	public Boolean getHasExac() {
		return hasExac;
	}

	public String getEXAC_AC() {
		return EXAC_AC;
	}

	public String getEXAC_AC_Adj() {
		return EXAC_AC_Adj;
	}

	public String getEXAC_AC_AFR() {
		return EXAC_AC_AFR;
	}

	public String getEXAC_AC_AMR() {
		return EXAC_AC_AMR;
	}

	public String getEXAC_AC_CONSANGUINEOUS() {
		return EXAC_AC_CONSANGUINEOUS;
	}

	public String getEXAC_AC_EAS() {
		return EXAC_AC_EAS;
	}

	public String getEXAC_AC_FEMALE() {
		return EXAC_AC_FEMALE;
	}

	public String getEXAC_AC_FIN() {
		return EXAC_AC_FIN;
	}

	public String getEXAC_AC_Hemi() {
		return EXAC_AC_Hemi;
	}

	public String getEXAC_AC_Het() {
		return EXAC_AC_Het;
	}

	public String getEXAC_AC_Hom() {
		return EXAC_AC_Hom;
	}

	public String getEXAC_AC_MALE() {
		return EXAC_AC_MALE;
	}

	public String getEXAC_AC_NFE() {
		return EXAC_AC_NFE;
	}

	public String getEXAC_AC_OTH() {
		return EXAC_AC_OTH;
	}

	public String getEXAC_AC_POPMAX() {
		return EXAC_AC_POPMAX;
	}

	public String getEXAC_AC_SAS() {
		return EXAC_AC_SAS;
	}

	public String getEXAC_AF() {
		return EXAC_AF;
	}

	public String getEXAC_ALT() {
		return EXAC_ALT;
	}

	public String getEXAC_AN() {
		return EXAC_AN;
	}

	public String getEXAC_AN_Adj() {
		return EXAC_AN_Adj;
	}

	public String getEXAC_AN_AFR() {
		return EXAC_AN_AFR;
	}

	public String getEXAC_AN_AMR() {
		return EXAC_AN_AMR;
	}

	public String getEXAC_AN_CONSANGUINEOUS() {
		return EXAC_AN_CONSANGUINEOUS;
	}

	public String getEXAC_AN_EAS() {
		return EXAC_AN_EAS;
	}

	public String getEXAC_AN_FEMALE() {
		return EXAC_AN_FEMALE;
	}

	public String getEXAC_AN_FIN() {
		return EXAC_AN_FIN;
	}

	public String getEXAC_AN_MALE() {
		return EXAC_AN_MALE;
	}

	public String getEXAC_AN_NFE() {
		return EXAC_AN_NFE;
	}

	public String getEXAC_AN_OTH() {
		return EXAC_AN_OTH;
	}

	public String getEXAC_AN_POPMAX() {
		return EXAC_AN_POPMAX;
	}

	public String getEXAC_AN_SAS() {
		return EXAC_AN_SAS;
	}

	public String getEXAC_BaseQRankSum() {
		return EXAC_BaseQRankSum;
	}

	public String getEXAC_clinvar_conflicted() {
		return EXAC_clinvar_conflicted;
	}

	public String getEXAC_clinvar_measureset_id() {
		return EXAC_clinvar_measureset_id;
	}

	public String getEXAC_clinvar_mut() {
		return EXAC_clinvar_mut;
	}

	public String getEXAC_clinvar_pathogenic() {
		return EXAC_clinvar_pathogenic;
	}

	public String getEXAC_ClippingRankSum() {
		return EXAC_ClippingRankSum;
	}

	public String getEXAC_culprit() {
		return EXAC_culprit;
	}

	public String getEXAC_DOUBLETON_DIST() {
		return EXAC_DOUBLETON_DIST;
	}

	public String getEXAC_DP() {
		return EXAC_DP;
	}

	public String getEXAC_ESP_AC() {
		return EXAC_ESP_AC;
	}

	public String getEXAC_ESP_AF_GLOBAL() {
		return EXAC_ESP_AF_GLOBAL;
	}

	public String getEXAC_ESP_AF_POPMAX() {
		return EXAC_ESP_AF_POPMAX;
	}

	public String getEXAC_FS() {
		return EXAC_FS;
	}

	public String getEXAC_GQ_MEAN() {
		return EXAC_GQ_MEAN;
	}

	public String getEXAC_GQ_STDDEV() {
		return EXAC_GQ_STDDEV;
	}

	public String getEXAC_Hemi_AFR() {
		return EXAC_Hemi_AFR;
	}

	public String getEXAC_Hemi_AMR() {
		return EXAC_Hemi_AMR;
	}

	public String getEXAC_Hemi_EAS() {
		return EXAC_Hemi_EAS;
	}

	public String getEXAC_Hemi_FIN() {
		return EXAC_Hemi_FIN;
	}

	public String getEXAC_Hemi_NFE() {
		return EXAC_Hemi_NFE;
	}

	public String getEXAC_Hemi_OTH() {
		return EXAC_Hemi_OTH;
	}

	public String getEXAC_Hemi_SAS() {
		return EXAC_Hemi_SAS;
	}

	public String getEXAC_Het_AFR() {
		return EXAC_Het_AFR;
	}

	public String getEXAC_Het_AMR() {
		return EXAC_Het_AMR;
	}

	public String getEXAC_Het_EAS() {
		return EXAC_Het_EAS;
	}

	public String getEXAC_Het_FIN() {
		return EXAC_Het_FIN;
	}

	public String getEXAC_Het_NFE() {
		return EXAC_Het_NFE;
	}

	public String getEXAC_Het_OTH() {
		return EXAC_Het_OTH;
	}

	public String getEXAC_Het_SAS() {
		return EXAC_Het_SAS;
	}

	public String getEXAC_hgvs() {
		return EXAC_hgvs;
	}

	public String getEXAC_Hom_AFR() {
		return EXAC_Hom_AFR;
	}

	public String getEXAC_Hom_AMR() {
		return EXAC_Hom_AMR;
	}

	public String getEXAC_Hom_CONSANGUINEOUS() {
		return EXAC_Hom_CONSANGUINEOUS;
	}

	public String getEXAC_Hom_EAS() {
		return EXAC_Hom_EAS;
	}

	public String getEXAC_Hom_FIN() {
		return EXAC_Hom_FIN;
	}

	public String getEXAC_Hom_NFE() {
		return EXAC_Hom_NFE;
	}

	public String getEXAC_Hom_OTH() {
		return EXAC_Hom_OTH;
	}

	public String getEXAC_Hom_SAS() {
		return EXAC_Hom_SAS;
	}

	public String getEXAC_InbreedingCoeff() {
		return EXAC_InbreedingCoeff;
	}

	public String getEXAC_index() {
		return EXAC_index;
	}

	public String getEXAC_K1_RUN() {
		return EXAC_K1_RUN;
	}

	public String getEXAC_K2_RUN() {
		return EXAC_K2_RUN;
	}

	public String getEXAC_K3_RUN() {
		return EXAC_K3_RUN;
	}

	public String getEXAC_KG_AC() {
		return EXAC_KG_AC;
	}

	public String getEXAC_KG_AF_GLOBAL() {
		return EXAC_KG_AF_GLOBAL;
	}

	public String getEXAC_KG_AF_POPMAX() {
		return EXAC_KG_AF_POPMAX;
	}

	public String getEXAC_MQ() {
		return EXAC_MQ;
	}

	public String getEXAC_MQ0() {
		return EXAC_MQ0;
	}

	public String getEXAC_MQRankSum() {
		return EXAC_MQRankSum;
	}

	public String getEXAC_NCC() {
		return EXAC_NCC;
	}

	public String getEXAC_POPMAX() {
		return EXAC_POPMAX;
	}

	public String getEXAC_POS() {
		return EXAC_POS;
	}

	public String getEXAC_QD() {
		return EXAC_QD;
	}

	public String getEXAC_ReadPosRankSum() {
		return EXAC_ReadPosRankSum;
	}

	public String getEXAC_REF() {
		return EXAC_REF;
	}

	public String getEXAC_VQSLOD() {
		return EXAC_VQSLOD;
	}
	
}