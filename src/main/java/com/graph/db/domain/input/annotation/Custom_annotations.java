package com.graph.db.domain.input.annotation;

public final class Custom_annotations {
	private Input input[];
//	private Custom_annotations.Gnomad_genomes_coding_autosome gnomad_genomes_coding_autosomes[];
//	private Custom_annotations.Dbsnp dbsnp[];
	private Custom_annotations.Kaviar kaviar[];
	private Custom_annotations.Gnomad_exome gnomad_exomes[];
	private Custom_annotations.Gnomad_genome gnomad_genomes[];

	public Input[] getInput() {
		return input;
	}

	public Custom_annotations.Gnomad_exome[] getGnomad_exomes() {
		return gnomad_exomes;
	}

	public Custom_annotations.Kaviar[] getKaviar() {
		return kaviar;
	}

	public Custom_annotations.Gnomad_genome[] getGnomad_genomes() {
		return gnomad_genomes;
	}

/*	public static final class Dbsnp {
		private String allele;
		private Dbsnp.Fields fields;
		private String name;

		public static final class Fields {
			public Integer sAO;
			public Integer sSR;
			public String gENEINFO;
			public String vP;
			public Integer wGT;
			public String vC;
			public Fields.VLD vLD;

			public static final class VLD {
				public VLD() {
				}
			}
		}
	}*/

	public static final class Gnomad_exome {
		private Gnomad_exome.Fields fields;
		// private String name;
		// private String allele;

		public Gnomad_exome.Fields getFields() {
			return fields;
		}

		public static final class Fields {
			private String AC_AFR;
			private String AC_AMR;
			private String AC_ASJ;
			private String AC_raw;
			private String AF_NFE;
			private String AF_OTH;
			private String AF_raw;
			private String AN_AFR;
//			private Integer hom_ASJ;
//			private Integer aN_ASJ;
//			private Integer aF_ASJ;
//			private Integer hom_OTH;
//			private Integer hom_Male;
//			private Integer aF_FIN;
//			private Integer aN_AMR;
//			private Integer aC_Female;
//			private Integer hom_AFR;
//			private Integer aN_raw;
//			private Integer aC_OTH;
//			private Integer hom_Female;
//			private Integer gC_raw;
//			private Integer aC_EAS;
//			private Integer hom_raw;
//			private Integer aN_EAS;
//			private Double aF_AMR;
//			private Integer hom_AMR;
//			private Integer hom_EAS;
//			private Integer hom_NFE;
//			private Integer aN_OTH;
//			private Integer aN_FIN;
//			private Integer gC;
//			private Double aF_Male;
//			private Integer hom_SAS;
//			private Integer aF_EAS;
//			private Integer aN_Male;
//			private Integer aC_SAS;
//			private Double aF_SAS;
//			private Integer aN_Female;
//			private Integer aC_NFE;
//			private Integer hom_FIN;
//			private Integer aC_Male;
//			private Double aF_AFR;
//			private Integer hom;
//			private Integer aN_NFE;
//			private Integer aC_FIN;
//			private Integer aN_SAS;
//			private Double aF_Female;
			public String getAC_AFR() {
				return AC_AFR;
			}

			public String getAC_AMR() {
				return AC_AMR;
			}

			public String getAC_ASJ() {
				return AC_ASJ;
			}

			public String getAC_raw() {
				return AC_raw;
			}

			public String getAF_NFE() {
				return AF_NFE;
			}

			public String getAF_OTH() {
				return AF_OTH;
			}

			public String getAF_raw() {
				return AF_raw;
			}

			public String getAN_AFR() {
				return AN_AFR;
			}
		}
	}

	public static final class Kaviar {
		private Kaviar.Fields fields;
//		private String allele;
//		private String name;

		public Kaviar.Fields getFields() {
			return fields;
		}

		public static final class Fields {
			private Integer AN;
			private Integer AC;
			private Double AF;

			public Integer getAN() {
				return AN;
			}

			public Integer getAC() {
				return AC;
			}

			public Double getAF() {
				return AF;
			}
		}
	}

	public static final class Gnomad_genome {
		private Gnomad_genome.Fields fields;
//		private String name;
//		private String allele;
		
		public Fields getFields() {
			return fields;
		}

		public static final class Fields {
			private String AC_AFR;
			private String AC_AMR;
			private String AC_ASJ;
			private String AC_raw;
			private String AF_NFE;
			private String AF_OTH;
			private String AF_raw;
			private String AN_AFR;
			
//			private Double aF_Male;
//			private Integer gC;
//			private Integer aN_FIN;
//			private Integer hom_NFE;
//			private Integer aN_OTH;
//			private Integer aF_AMR;
//			private Integer hom_AMR;
//			private Integer hom_EAS;
//			private Integer aC_FIN;
//			private Integer aF_Female;
//			private Integer hom;
//			private Integer aN_NFE;
//			private Integer hom_FIN;
//			private Double aF_AFR;
//			private Integer aC_Male;
//			private Integer aC_NFE;
//			private Integer aN_Female;
//			private Integer aN_Male;
//			private Integer aF_EAS;
//			private Integer hom_AFR;
//			private Integer aC_Female;
//			private Integer aN_AMR;
//			private Integer aN_raw;
//			private Integer aF_FIN;
//			private Integer hom_Male;
//			private Integer aF_ASJ;
//			private Integer hom_OTH;
//			private Integer hom_ASJ;
//			private Integer aN_ASJ;
//			private Integer aC_EAS;
//			private Integer hom_raw;
//			private Integer aN_EAS;
//			private Integer hom_Female;
//			private Integer aC_OTH;
//			private Integer gC_raw;

			public String getAC_AFR() {
				return AC_AFR;
			}

			public String getAC_AMR() {
				return AC_AMR;
			}

			public String getAC_ASJ() {
				return AC_ASJ;
			}

			public String getAC_raw() {
				return AC_raw;
			}

			public String getAF_NFE() {
				return AF_NFE;
			}

			public String getAF_OTH() {
				return AF_OTH;
			}

			public String getAF_raw() {
				return AF_raw;
			}

			public String getAN_AFR() {
				return AN_AFR;
			}
		}
	}
}