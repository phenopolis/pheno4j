package com.graph.db.domain.input.annotation;

public final class Input {
	private InputFields fields;
//	private String allele;
//	private String name;
    
	public InputFields getFields() {
		return fields;
	}
	
	public static final class InputFields {
		private Double InbreedingCoeff;
		private Double AF;
		private String culprit;
		private Integer AN;
		private Integer AC;
		private Double FS;
		private Integer MLEAC;
		private Double ReadPosRankSum;
		private Double VQSLOD;
		private Double MLEAF;
		private Double ExcessHet;
		private Double MQRankSum;
		private Double MQ;
//		private Double SOR;
//		private Double ClippingRankSum;
//		private Double BaseQRankSum;
//		private Integer MQ0;
//		private Double QD;
//		private Integer DP;

		public Double getInbreedingCoeff() {
			return InbreedingCoeff;
		}

		public Double getAF() {
			return AF;
		}

		public String getCulprit() {
			return culprit;
		}

		public Integer getAN() {
			return AN;
		}

		public Integer getAC() {
			return AC;
		}

		public Double getFS() {
			return FS;
		}

		public Integer getMLEAC() {
			return MLEAC;
		}

		public Double getReadPosRankSum() {
			return ReadPosRankSum;
		}

		public Double getVQSLOD() {
			return VQSLOD;
		}

		public Double getMLEAF() {
			return MLEAF;
		}

		public Double getExcessHet() {
			return ExcessHet;
		}

		public Double getMQRankSum() {
			return MQRankSum;
		}

		public Double getMQ() {
			return MQ;
		}
	}
}