package com.graph.db.domain.output;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.math.NumberUtils;

import com.graph.db.domain.input.annotation.Custom_annotations.Gnomad_exome;
import com.graph.db.domain.input.annotation.Custom_annotations.Gnomad_exome.Fields;
import com.graph.db.domain.input.annotation.Custom_annotations.Gnomad_genome;
import com.graph.db.domain.input.annotation.Custom_annotations.Kaviar;
import com.graph.db.domain.input.annotation.GeneticVariant;
import com.graph.db.domain.input.annotation.Input;
import com.graph.db.domain.input.annotation.Input.InputFields;
import com.graph.db.domain.input.annotation.TranscriptConsequence;
import com.graph.db.domain.output.annotation.Id;
import com.graph.db.domain.output.annotation.Index;

public class GeneticVariantOutput {
	
	@Id(name = "variantId")
	private final String variant_id;
	
	private final String allele_string;
	private final Integer start;
	private final Integer end;
	private final String seq_region_name;
	private final String most_severe_consequence;
	private final Integer strand;
	
	//input[0].fields.
	private Integer AC;
	@Index
	private Double allele_freq;
	private Integer AN;
	private Double ExcessHet;//wrong on the github page, says it's int
	private Double FS;
	private Double InbreedingCoeff;
	private Integer MLEAC;
	private Double MLEAF;
	private Double MQ;
	private Double MQRankSum;
	private Double ReadPosRankSum;
	private Double VQSLOD;
	private String Culprit;
    
    //gnomad_exomes[0].fields.
	private Integer gnomad_exomes_AC_AFR;
	private Integer gnomad_exomes_AC_AMR;
	private Integer gnomad_exomes_AC_ASJ;
	private Integer gnomad_exomes_AC_raw;
	private Double gnomad_exomes_AF_NFE;
	private Double gnomad_exomes_AF_OTH;
	private Double gnomad_exomes_AF_raw;
	private Integer gnomad_exomes_AN_AFR;
	private Integer gnomad_exomes_AC_EAS;
	private Integer gnomad_exomes_AC_Female;
	private Integer gnomad_exomes_AC_OTH;
	private Integer gnomad_exomes_AC_NFE;
	private Integer gnomad_exomes_AC_Male;
	private Integer gnomad_exomes_AC_FIN;
	private Double gnomad_exomes_AF_AFR;
	private Double gnomad_exomes_AF_AMR;
	private Double gnomad_exomes_AF_ASJ;
	private Double gnomad_exomes_AF_EAS;
	private Double gnomad_exomes_AF_FIN;
	private Double gnomad_exomes_AF_Female;
	private Double gnomad_exomes_AF_Male;
	private Integer gnomad_exomes_AN_AMR;
	private Integer gnomad_exomes_AN_ASJ;
	private Integer gnomad_exomes_AN_EAS;
	private Integer gnomad_exomes_AN_FIN;
	private Integer gnomad_exomes_AN_Female;
	private Integer gnomad_exomes_AN_Male;
	private Integer gnomad_exomes_AN_NFE;
	private Integer gnomad_exomes_AN_OTH;
	private Integer gnomad_exomes_AN_raw;
	private Integer gnomad_exomes_Hom_AFR;
	private Integer gnomad_exomes_Hom_AMR;
	private Integer gnomad_exomes_Hom_ASJ;
	private Integer gnomad_exomes_Hom_EAS;
	private Integer gnomad_exomes_Hom_FIN;
	private Integer gnomad_exomes_Hom_Female;
	private Integer gnomad_exomes_Hom_Male;
	private Integer gnomad_exomes_Hom_NFE;
	private Integer gnomad_exomes_Hom_OTH;
	private Integer gnomad_exomes_Hom_raw;
	private Integer gnomad_exomes_Hom;
	
	//gnomad_genomes[0].fields.
	private Integer gnomad_genomes_AC_AFR;
	private Integer gnomad_genomes_AC_AMR;
	private Integer gnomad_genomes_AC_ASJ;
	private Integer gnomad_genomes_AC_raw;
	private Double gnomad_genomes_AF_NFE;
	private Double gnomad_genomes_AF_OTH;
	private Double gnomad_genomes_AF_raw;
	private Integer gnomad_genomes_AN_AFR;
	private Integer gnomad_genomes_AC_EAS;
	private Integer gnomad_genomes_AC_Female;
	private Integer gnomad_genomes_AC_OTH;
	private Integer gnomad_genomes_AC_NFE;
	private Integer gnomad_genomes_AC_Male;
	private Integer gnomad_genomes_AC_FIN;
	private Double gnomad_genomes_AF_AFR;
	private Double gnomad_genomes_AF_AMR;
	private Double gnomad_genomes_AF_ASJ;
	private Double gnomad_genomes_AF_EAS;
	private Double gnomad_genomes_AF_FIN;
	private Double gnomad_genomes_AF_Female;
	private Double gnomad_genomes_AF_Male;
	private Integer gnomad_genomes_AN_AMR;
	private Integer gnomad_genomes_AN_ASJ;
	private Integer gnomad_genomes_AN_EAS;
	private Integer gnomad_genomes_AN_FIN;
	private Integer gnomad_genomes_AN_Female;
	private Integer gnomad_genomes_AN_Male;
	private Integer gnomad_genomes_AN_NFE;
	private Integer gnomad_genomes_AN_OTH;
	private Integer gnomad_genomes_AN_raw;
	private Integer gnomad_genomes_Hom_AFR;
	private Integer gnomad_genomes_Hom_AMR;
	private Integer gnomad_genomes_Hom_ASJ;
	private Integer gnomad_genomes_Hom_EAS;
	private Integer gnomad_genomes_Hom_FIN;
	private Integer gnomad_genomes_Hom_Female;
	private Integer gnomad_genomes_Hom_Male;
	private Integer gnomad_genomes_Hom_NFE;
	private Integer gnomad_genomes_Hom_OTH;
	private Integer gnomad_genomes_Hom_raw;
	private Integer gnomad_genomes_Hom;
	
	//kaviar[0].fields.
	private Integer kaviar_AN;
	private Integer kaviar_AC;
	private Double kaviar_AF;

	//fields copied from transcript_consequences
	@Index
	private final Double exac_AF;
	private final Double exac_AF_ADJ;
	private final Integer exac_AF_AFR;
	private final Double exac_AF_AMR;
	private final Integer exac_AF_CONSANGUINEOUS;
	private final Integer exac_AF_EAS;
	private final Integer exac_AF_FEMALE;
	private final Integer exac_AF_FIN;
	private final Double exac_AF_MALE;
	private final Integer exac_AF_NFE;
	private final Integer exac_AF_OTH;
	private final Double exac_AF_POPMAX;
	private final Double exac_AF_SAS;
	private final Integer cadd_phred;
	private final Double cadd_raw;
	
	public GeneticVariantOutput(GeneticVariant geneticVariant) {
		this.variant_id = geneticVariant.getVariant_id();
		this.allele_string = geneticVariant.getAllele_string();
		this.start = geneticVariant.getStart();
		this.end = geneticVariant.getEnd();
		this.seq_region_name = geneticVariant.getSeq_region_name();
		this.most_severe_consequence = geneticVariant.getMost_severe_consequence();
		this.strand = geneticVariant.getStrand();
		
		if ((geneticVariant.getCustom_annotations() != null) && (geneticVariant.getCustom_annotations().getInput() != null) ) {
			Input input = geneticVariant.getCustom_annotations().getInput()[0];
			InputFields fields = input.getFields();
			this.InbreedingCoeff = fields.getInbreedingCoeff();
			this.allele_freq = fields.getAF();
			this.Culprit = fields.getCulprit();
			this.AN = fields.getAN();
			this.AC = fields.getAC();
			this.FS = fields.getFS();
			this.MLEAC = fields.getMLEAC();
			this.ReadPosRankSum = fields.getReadPosRankSum();
			this.VQSLOD = fields.getVQSLOD();
			this.MLEAF = fields.getMLEAF();
			this.ExcessHet = fields.getExcessHet();
			this.MQRankSum = fields.getMQRankSum();
			this.MQ = fields.getMQ();
		}
		
		if ((geneticVariant.getCustom_annotations() != null) && (geneticVariant.getCustom_annotations().getGnomad_exomes() != null) ) {
			Gnomad_exome gnomad_exome = geneticVariant.getCustom_annotations().getGnomad_exomes()[0];
			Fields fields = gnomad_exome.getFields();
			
			this.gnomad_exomes_AC_AFR = convertToInteger(fields.getAC_AFR());
			this.gnomad_exomes_AC_AMR = convertToInteger(fields.getAC_AMR());
			this.gnomad_exomes_AC_ASJ = convertToInteger(fields.getAC_ASJ());
			this.gnomad_exomes_AC_raw = convertToInteger(fields.getAC_raw());
			this.gnomad_exomes_AF_NFE = convertToDouble(fields.getAF_NFE());
			this.gnomad_exomes_AF_OTH = convertToDouble(fields.getAF_OTH());
			this.gnomad_exomes_AF_raw = convertToDouble(fields.getAF_raw());
			this.gnomad_exomes_AN_AFR = convertToInteger(fields.getAN_AFR());
			this.gnomad_exomes_AC_EAS = convertToInteger(fields.getAC_EAS());
			this.gnomad_exomes_AC_Female = convertToInteger(fields.getAC_Female());
			this.gnomad_exomes_AC_OTH = convertToInteger(fields.getAC_OTH());
			this.gnomad_exomes_AC_NFE = convertToInteger(fields.getAC_NFE());
			this.gnomad_exomes_AC_Male = convertToInteger(fields.getAC_Male());
			this.gnomad_exomes_AC_FIN = convertToInteger(fields.getAC_FIN());
			this.gnomad_exomes_AF_AFR = convertToDouble(fields.getAF_AFR());
			this.gnomad_exomes_AF_AMR = convertToDouble(fields.getAF_AMR());
			this.gnomad_exomes_AF_ASJ = convertToDouble(fields.getAF_ASJ());
			this.gnomad_exomes_AF_EAS = convertToDouble(fields.getAF_EAS());
			this.gnomad_exomes_AF_FIN = convertToDouble(fields.getAF_FIN());
			this.gnomad_exomes_AF_Female = convertToDouble(fields.getAF_Female());
			this.gnomad_exomes_AF_Male = convertToDouble(fields.getAF_Male());
			this.gnomad_exomes_AN_AMR = convertToInteger(fields.getAN_AMR());
			this.gnomad_exomes_AN_ASJ = convertToInteger(fields.getAN_ASJ());
			this.gnomad_exomes_AN_EAS = convertToInteger(fields.getAN_EAS());
			this.gnomad_exomes_AN_FIN = convertToInteger(fields.getAN_FIN());
			this.gnomad_exomes_AN_Female = convertToInteger(fields.getAN_Female());
			this.gnomad_exomes_AN_Male = convertToInteger(fields.getAN_Male());
			this.gnomad_exomes_AN_NFE = convertToInteger(fields.getAN_NFE());
			this.gnomad_exomes_AN_OTH = convertToInteger(fields.getAN_OTH());
			this.gnomad_exomes_AN_raw = convertToInteger(fields.getAN_raw());
			this.gnomad_exomes_Hom_AFR = convertToInteger(fields.getHom_AFR());
			this.gnomad_exomes_Hom_AMR = convertToInteger(fields.getHom_AMR());
			this.gnomad_exomes_Hom_ASJ = convertToInteger(fields.getHom_ASJ());
			this.gnomad_exomes_Hom_EAS = convertToInteger(fields.getHom_EAS());
			this.gnomad_exomes_Hom_FIN = convertToInteger(fields.getHom_FIN());
			this.gnomad_exomes_Hom_Female = convertToInteger(fields.getHom_Female());
			this.gnomad_exomes_Hom_Male = convertToInteger(fields.getHom_Male());
			this.gnomad_exomes_Hom_NFE = convertToInteger(fields.getHom_NFE());
			this.gnomad_exomes_Hom_OTH = convertToInteger(fields.getHom_OTH());
			this.gnomad_exomes_Hom_raw = convertToInteger(fields.getHom_raw());
			this.gnomad_exomes_Hom = convertToInteger(fields.getHom());
		}
		
		if ((geneticVariant.getCustom_annotations() != null) && (geneticVariant.getCustom_annotations().getGnomad_genomes() != null) ) {
			Gnomad_genome gnomad_genome = geneticVariant.getCustom_annotations().getGnomad_genomes()[0];
			com.graph.db.domain.input.annotation.Custom_annotations.Gnomad_genome.Fields fields = gnomad_genome.getFields();
			
			this.gnomad_genomes_AC_AFR = convertToInteger(fields.getAC_AFR());
			this.gnomad_genomes_AC_AMR = convertToInteger(fields.getAC_AMR());
			this.gnomad_genomes_AC_ASJ = convertToInteger(fields.getAC_ASJ());
			this.gnomad_genomes_AC_raw = convertToInteger(fields.getAC_raw());
			this.gnomad_genomes_AF_NFE = convertToDouble(fields.getAF_NFE());
			this.gnomad_genomes_AF_OTH = convertToDouble(fields.getAF_OTH());
			this.gnomad_genomes_AF_raw = convertToDouble(fields.getAF_raw());
			this.gnomad_genomes_AN_AFR = convertToInteger(fields.getAN_AFR());
			this.gnomad_genomes_AC_EAS = convertToInteger(fields.getAC_EAS());
			this.gnomad_genomes_AC_Female = convertToInteger(fields.getAC_Female());
			this.gnomad_genomes_AC_OTH = convertToInteger(fields.getAC_OTH());
			this.gnomad_genomes_AC_NFE = convertToInteger(fields.getAC_NFE());
			this.gnomad_genomes_AC_Male = convertToInteger(fields.getAC_Male());
			this.gnomad_genomes_AC_FIN = convertToInteger(fields.getAC_FIN());
			this.gnomad_genomes_AF_AFR = convertToDouble(fields.getAF_AFR());
			this.gnomad_genomes_AF_AMR = convertToDouble(fields.getAF_AMR());
			this.gnomad_genomes_AF_ASJ = convertToDouble(fields.getAF_ASJ());
			this.gnomad_genomes_AF_EAS = convertToDouble(fields.getAF_EAS());
			this.gnomad_genomes_AF_FIN = convertToDouble(fields.getAF_FIN());
			this.gnomad_genomes_AF_Female = convertToDouble(fields.getAF_Female());
			this.gnomad_genomes_AF_Male = convertToDouble(fields.getAF_Male());
			this.gnomad_genomes_AN_AMR = convertToInteger(fields.getAN_AMR());
			this.gnomad_genomes_AN_ASJ = convertToInteger(fields.getAN_ASJ());
			this.gnomad_genomes_AN_EAS = convertToInteger(fields.getAN_EAS());
			this.gnomad_genomes_AN_FIN = convertToInteger(fields.getAN_FIN());
			this.gnomad_genomes_AN_Female = convertToInteger(fields.getAN_Female());
			this.gnomad_genomes_AN_Male = convertToInteger(fields.getAN_Male());
			this.gnomad_genomes_AN_NFE = convertToInteger(fields.getAN_NFE());
			this.gnomad_genomes_AN_OTH = convertToInteger(fields.getAN_OTH());
			this.gnomad_genomes_AN_raw = convertToInteger(fields.getAN_raw());
			this.gnomad_genomes_Hom_AFR = convertToInteger(fields.getHom_AFR());
			this.gnomad_genomes_Hom_AMR = convertToInteger(fields.getHom_AMR());
			this.gnomad_genomes_Hom_ASJ = convertToInteger(fields.getHom_ASJ());
			this.gnomad_genomes_Hom_EAS = convertToInteger(fields.getHom_EAS());
			this.gnomad_genomes_Hom_FIN = convertToInteger(fields.getHom_FIN());
			this.gnomad_genomes_Hom_Female = convertToInteger(fields.getHom_Female());
			this.gnomad_genomes_Hom_Male = convertToInteger(fields.getHom_Male());
			this.gnomad_genomes_Hom_NFE = convertToInteger(fields.getHom_NFE());
			this.gnomad_genomes_Hom_OTH = convertToInteger(fields.getHom_OTH());
			this.gnomad_genomes_Hom_raw = convertToInteger(fields.getHom_raw());
			this.gnomad_genomes_Hom = convertToInteger(fields.getHom());
		}
		
		if ((geneticVariant.getCustom_annotations() != null) && (geneticVariant.getCustom_annotations().getKaviar() != null) ) {
			Kaviar kaviar = geneticVariant.getCustom_annotations().getKaviar()[0];
			com.graph.db.domain.input.annotation.Custom_annotations.Kaviar.Fields fields = kaviar.getFields();
			this.kaviar_AN = fields.getAN();
			this.kaviar_AC = fields.getAC();
			this.kaviar_AF = fields.getAF();
		}
		
		this.exac_AF = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af);
		this.exac_AF_ADJ = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_adj);
		this.exac_AF_AFR = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_afr);
		this.exac_AF_AMR = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_amr);
		this.exac_AF_CONSANGUINEOUS = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_consanguineous);
		this.exac_AF_EAS = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_eas);
		this.exac_AF_FEMALE = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_female);
		this.exac_AF_FIN = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_fin);
		this.exac_AF_MALE = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_male);
		this.exac_AF_NFE = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_nfe);
		this.exac_AF_OTH = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_oth);
		this.exac_AF_POPMAX = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_popmax);
		this.exac_AF_SAS = findUniqueValue(geneticVariant, TranscriptConsequence::getExac_af_sas);
		this.cadd_phred = findUniqueValue(geneticVariant, TranscriptConsequence::getCadd_phred);
		this.cadd_raw = findUniqueValue(geneticVariant, TranscriptConsequence::getCadd_raw);
	}

	private <R> R findUniqueValue(GeneticVariant geneticVariant, Function<? super TranscriptConsequence, R> f) {
		Set<R> set = collectValues(geneticVariant, f);
		return getDistinctValue(geneticVariant, set);
	}
	
	private <R> Set<R> collectValues(GeneticVariant geneticVariant, Function<? super TranscriptConsequence, R> f) {
		if (isEmpty(geneticVariant.getTranscript_consequences())) {
			return Collections.emptySet();
		} else {
			return geneticVariant.getTranscript_consequences().stream()
					.map(f)
					.collect(toSet());
		}
	}

	private <R> R getDistinctValue(GeneticVariant geneticVariant, Set<R> set) {
		if (set.size() == 1) {
			return set.iterator().next();
		} else if (set.size() > 1) {
			throw new RuntimeException(geneticVariant.getVariant_id() + " has more than 1 value: " + set);
		} else {
			return null;
		}
	}
	
	private Integer convertToInteger(String string) {
		if (NumberUtils.isNumber(string)) {
			return Integer.valueOf(string);
		} else {
			return null;
		}
	}
	
	private Double convertToDouble(String string) {
		if (NumberUtils.isNumber(string)) {
			return Double.valueOf(string);
		} else {
			return null;
		}
	}

	public String getVariant_id() {
		return variant_id;
	}

	public String getAllele_string() {
		return allele_string;
	}

	public Integer getStart() {
		return start;
	}

	public Integer getEnd() {
		return end;
	}

	public String getSeq_region_name() {
		return seq_region_name;
	}

	public String getMost_severe_consequence() {
		return most_severe_consequence;
	}

	public Integer getStrand() {
		return strand;
	}

	public Integer getAC() {
		return AC;
	}

	public Double getAllele_freq() {
		return allele_freq;
	}

	public Integer getAN() {
		return AN;
	}

	public Double getExcessHet() {
		return ExcessHet;
	}

	public Double getFS() {
		return FS;
	}

	public Double getInbreedingCoeff() {
		return InbreedingCoeff;
	}

	public Integer getMLEAC() {
		return MLEAC;
	}

	public Double getMLEAF() {
		return MLEAF;
	}

	public Double getMQ() {
		return MQ;
	}

	public Double getMQRankSum() {
		return MQRankSum;
	}

	public Double getReadPosRankSum() {
		return ReadPosRankSum;
	}

	public Double getVQSLOD() {
		return VQSLOD;
	}

	public String getCulprit() {
		return Culprit;
	}

	public Integer getGnomad_exomes_AC_AFR() {
		return gnomad_exomes_AC_AFR;
	}

	public Integer getGnomad_exomes_AC_AMR() {
		return gnomad_exomes_AC_AMR;
	}

	public Integer getGnomad_exomes_AC_ASJ() {
		return gnomad_exomes_AC_ASJ;
	}

	public Integer getGnomad_exomes_AC_raw() {
		return gnomad_exomes_AC_raw;
	}

	public Double getGnomad_exomes_AF_NFE() {
		return gnomad_exomes_AF_NFE;
	}

	public Double getGnomad_exomes_AF_OTH() {
		return gnomad_exomes_AF_OTH;
	}

	public Double getGnomad_exomes_AF_raw() {
		return gnomad_exomes_AF_raw;
	}

	public Integer getGnomad_exomes_AN_AFR() {
		return gnomad_exomes_AN_AFR;
	}

	public Integer getGnomad_exomes_AC_EAS() {
		return gnomad_exomes_AC_EAS;
	}

	public Integer getGnomad_exomes_AC_Female() {
		return gnomad_exomes_AC_Female;
	}

	public Integer getGnomad_exomes_AC_OTH() {
		return gnomad_exomes_AC_OTH;
	}

	public Integer getGnomad_exomes_AC_NFE() {
		return gnomad_exomes_AC_NFE;
	}

	public Integer getGnomad_exomes_AC_Male() {
		return gnomad_exomes_AC_Male;
	}

	public Integer getGnomad_exomes_AC_FIN() {
		return gnomad_exomes_AC_FIN;
	}

	public Double getGnomad_exomes_AF_AFR() {
		return gnomad_exomes_AF_AFR;
	}

	public Double getGnomad_exomes_AF_AMR() {
		return gnomad_exomes_AF_AMR;
	}

	public Double getGnomad_exomes_AF_ASJ() {
		return gnomad_exomes_AF_ASJ;
	}

	public Double getGnomad_exomes_AF_EAS() {
		return gnomad_exomes_AF_EAS;
	}

	public Double getGnomad_exomes_AF_FIN() {
		return gnomad_exomes_AF_FIN;
	}

	public Double getGnomad_exomes_AF_Female() {
		return gnomad_exomes_AF_Female;
	}

	public Double getGnomad_exomes_AF_Male() {
		return gnomad_exomes_AF_Male;
	}

	public Integer getGnomad_exomes_AN_AMR() {
		return gnomad_exomes_AN_AMR;
	}

	public Integer getGnomad_exomes_AN_ASJ() {
		return gnomad_exomes_AN_ASJ;
	}

	public Integer getGnomad_exomes_AN_EAS() {
		return gnomad_exomes_AN_EAS;
	}

	public Integer getGnomad_exomes_AN_FIN() {
		return gnomad_exomes_AN_FIN;
	}

	public Integer getGnomad_exomes_AN_Female() {
		return gnomad_exomes_AN_Female;
	}

	public Integer getGnomad_exomes_AN_Male() {
		return gnomad_exomes_AN_Male;
	}

	public Integer getGnomad_exomes_AN_NFE() {
		return gnomad_exomes_AN_NFE;
	}

	public Integer getGnomad_exomes_AN_OTH() {
		return gnomad_exomes_AN_OTH;
	}

	public Integer getGnomad_exomes_AN_raw() {
		return gnomad_exomes_AN_raw;
	}

	public Integer getGnomad_exomes_Hom_AFR() {
		return gnomad_exomes_Hom_AFR;
	}

	public Integer getGnomad_exomes_Hom_AMR() {
		return gnomad_exomes_Hom_AMR;
	}

	public Integer getGnomad_exomes_Hom_ASJ() {
		return gnomad_exomes_Hom_ASJ;
	}

	public Integer getGnomad_exomes_Hom_EAS() {
		return gnomad_exomes_Hom_EAS;
	}

	public Integer getGnomad_exomes_Hom_FIN() {
		return gnomad_exomes_Hom_FIN;
	}

	public Integer getGnomad_exomes_Hom_Female() {
		return gnomad_exomes_Hom_Female;
	}

	public Integer getGnomad_exomes_Hom_Male() {
		return gnomad_exomes_Hom_Male;
	}

	public Integer getGnomad_exomes_Hom_NFE() {
		return gnomad_exomes_Hom_NFE;
	}

	public Integer getGnomad_exomes_Hom_OTH() {
		return gnomad_exomes_Hom_OTH;
	}

	public Integer getGnomad_exomes_Hom_raw() {
		return gnomad_exomes_Hom_raw;
	}

	public Integer getGnomad_exomes_Hom() {
		return gnomad_exomes_Hom;
	}

	public Integer getGnomad_genomes_AC_AFR() {
		return gnomad_genomes_AC_AFR;
	}

	public Integer getGnomad_genomes_AC_AMR() {
		return gnomad_genomes_AC_AMR;
	}

	public Integer getGnomad_genomes_AC_ASJ() {
		return gnomad_genomes_AC_ASJ;
	}

	public Integer getGnomad_genomes_AC_raw() {
		return gnomad_genomes_AC_raw;
	}

	public Double getGnomad_genomes_AF_NFE() {
		return gnomad_genomes_AF_NFE;
	}

	public Double getGnomad_genomes_AF_OTH() {
		return gnomad_genomes_AF_OTH;
	}

	public Double getGnomad_genomes_AF_raw() {
		return gnomad_genomes_AF_raw;
	}

	public Integer getGnomad_genomes_AN_AFR() {
		return gnomad_genomes_AN_AFR;
	}

	public Integer getGnomad_genomes_AC_EAS() {
		return gnomad_genomes_AC_EAS;
	}

	public Integer getGnomad_genomes_AC_Female() {
		return gnomad_genomes_AC_Female;
	}

	public Integer getGnomad_genomes_AC_OTH() {
		return gnomad_genomes_AC_OTH;
	}

	public Integer getGnomad_genomes_AC_NFE() {
		return gnomad_genomes_AC_NFE;
	}

	public Integer getGnomad_genomes_AC_Male() {
		return gnomad_genomes_AC_Male;
	}

	public Integer getGnomad_genomes_AC_FIN() {
		return gnomad_genomes_AC_FIN;
	}

	public Double getGnomad_genomes_AF_AFR() {
		return gnomad_genomes_AF_AFR;
	}

	public Double getGnomad_genomes_AF_AMR() {
		return gnomad_genomes_AF_AMR;
	}

	public Double getGnomad_genomes_AF_ASJ() {
		return gnomad_genomes_AF_ASJ;
	}

	public Double getGnomad_genomes_AF_EAS() {
		return gnomad_genomes_AF_EAS;
	}

	public Double getGnomad_genomes_AF_FIN() {
		return gnomad_genomes_AF_FIN;
	}

	public Double getGnomad_genomes_AF_Female() {
		return gnomad_genomes_AF_Female;
	}

	public Double getGnomad_genomes_AF_Male() {
		return gnomad_genomes_AF_Male;
	}

	public Integer getGnomad_genomes_AN_AMR() {
		return gnomad_genomes_AN_AMR;
	}

	public Integer getGnomad_genomes_AN_ASJ() {
		return gnomad_genomes_AN_ASJ;
	}

	public Integer getGnomad_genomes_AN_EAS() {
		return gnomad_genomes_AN_EAS;
	}

	public Integer getGnomad_genomes_AN_FIN() {
		return gnomad_genomes_AN_FIN;
	}

	public Integer getGnomad_genomes_AN_Female() {
		return gnomad_genomes_AN_Female;
	}

	public Integer getGnomad_genomes_AN_Male() {
		return gnomad_genomes_AN_Male;
	}

	public Integer getGnomad_genomes_AN_NFE() {
		return gnomad_genomes_AN_NFE;
	}

	public Integer getGnomad_genomes_AN_OTH() {
		return gnomad_genomes_AN_OTH;
	}

	public Integer getGnomad_genomes_AN_raw() {
		return gnomad_genomes_AN_raw;
	}

	public Integer getGnomad_genomes_Hom_AFR() {
		return gnomad_genomes_Hom_AFR;
	}

	public Integer getGnomad_genomes_Hom_AMR() {
		return gnomad_genomes_Hom_AMR;
	}

	public Integer getGnomad_genomes_Hom_ASJ() {
		return gnomad_genomes_Hom_ASJ;
	}

	public Integer getGnomad_genomes_Hom_EAS() {
		return gnomad_genomes_Hom_EAS;
	}

	public Integer getGnomad_genomes_Hom_FIN() {
		return gnomad_genomes_Hom_FIN;
	}

	public Integer getGnomad_genomes_Hom_Female() {
		return gnomad_genomes_Hom_Female;
	}

	public Integer getGnomad_genomes_Hom_Male() {
		return gnomad_genomes_Hom_Male;
	}

	public Integer getGnomad_genomes_Hom_NFE() {
		return gnomad_genomes_Hom_NFE;
	}

	public Integer getGnomad_genomes_Hom_OTH() {
		return gnomad_genomes_Hom_OTH;
	}

	public Integer getGnomad_genomes_Hom_raw() {
		return gnomad_genomes_Hom_raw;
	}

	public Integer getGnomad_genomes_Hom() {
		return gnomad_genomes_Hom;
	}

	public Integer getKaviar_AN() {
		return kaviar_AN;
	}

	public Integer getKaviar_AC() {
		return kaviar_AC;
	}

	public Double getKaviar_AF() {
		return kaviar_AF;
	}

	public Double getExac_AF() {
		return exac_AF;
	}

	public Double getExac_AF_ADJ() {
		return exac_AF_ADJ;
	}

	public Integer getExac_AF_AFR() {
		return exac_AF_AFR;
	}

	public Double getExac_AF_AMR() {
		return exac_AF_AMR;
	}

	public Integer getExac_AF_CONSANGUINEOUS() {
		return exac_AF_CONSANGUINEOUS;
	}

	public Integer getExac_AF_EAS() {
		return exac_AF_EAS;
	}

	public Integer getExac_AF_FEMALE() {
		return exac_AF_FEMALE;
	}

	public Integer getExac_AF_FIN() {
		return exac_AF_FIN;
	}

	public Double getExac_AF_MALE() {
		return exac_AF_MALE;
	}

	public Integer getExac_AF_NFE() {
		return exac_AF_NFE;
	}

	public Integer getExac_AF_OTH() {
		return exac_AF_OTH;
	}

	public Double getExac_AF_POPMAX() {
		return exac_AF_POPMAX;
	}

	public Double getExac_AF_SAS() {
		return exac_AF_SAS;
	}

	public Integer getCadd_phred() {
		return cadd_phred;
	}

	public Double getCadd_raw() {
		return cadd_raw;
	}
}