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
			private String AC_EAS;
			private String AC_Female;
			private String AC_OTH;
			private String AC_NFE;
			private String AC_Male;
			private String AC_FIN;
			private String AF_AFR;
			private String AF_AMR;
			private String AF_ASJ;
			private String AF_EAS;
			private String AF_FIN;
			private String AF_Female;
			private String AF_Male;
			private String AN_AMR;
			private String AN_ASJ;
			private String AN_EAS;
			private String AN_FIN;
			private String AN_Female;
			private String AN_Male;
			private String AN_NFE;
			private String AN_OTH;
			private String AN_raw;
			private String Hom_AFR;
			private String Hom_AMR;
			private String Hom_ASJ;
			private String Hom_EAS;
			private String Hom_FIN;
			private String Hom_Female;
			private String Hom_Male;
			private String Hom_NFE;
			private String Hom_OTH;
			private String Hom_raw;
			private String Hom;
//			private Integer gC_raw;
//			private Integer gC;
//			private Integer hom_SAS;
//			private Integer aC_SAS;
//			private Double aF_SAS;
//			private Integer aN_SAS;
			
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
			public String getAC_EAS() {
				return AC_EAS;
			}
			public String getAC_Female() {
				return AC_Female;
			}
			public String getAC_OTH() {
				return AC_OTH;
			}
			public String getAC_NFE() {
				return AC_NFE;
			}
			public String getAC_Male() {
				return AC_Male;
			}
			public String getAC_FIN() {
				return AC_FIN;
			}
			public String getAF_AFR() {
				return AF_AFR;
			}
			public String getAF_AMR() {
				return AF_AMR;
			}
			public String getAF_ASJ() {
				return AF_ASJ;
			}
			public String getAF_EAS() {
				return AF_EAS;
			}
			public String getAF_FIN() {
				return AF_FIN;
			}
			public String getAF_Female() {
				return AF_Female;
			}
			public String getAF_Male() {
				return AF_Male;
			}
			public String getAN_AMR() {
				return AN_AMR;
			}
			public String getAN_ASJ() {
				return AN_ASJ;
			}
			public String getAN_EAS() {
				return AN_EAS;
			}
			public String getAN_FIN() {
				return AN_FIN;
			}
			public String getAN_Female() {
				return AN_Female;
			}
			public String getAN_Male() {
				return AN_Male;
			}
			public String getAN_NFE() {
				return AN_NFE;
			}
			public String getAN_OTH() {
				return AN_OTH;
			}
			public String getAN_raw() {
				return AN_raw;
			}
			public String getHom_AFR() {
				return Hom_AFR;
			}
			public String getHom_AMR() {
				return Hom_AMR;
			}
			public String getHom_ASJ() {
				return Hom_ASJ;
			}
			public String getHom_EAS() {
				return Hom_EAS;
			}
			public String getHom_FIN() {
				return Hom_FIN;
			}
			public String getHom_Female() {
				return Hom_Female;
			}
			public String getHom_Male() {
				return Hom_Male;
			}
			public String getHom_NFE() {
				return Hom_NFE;
			}
			public String getHom_OTH() {
				return Hom_OTH;
			}
			public String getHom_raw() {
				return Hom_raw;
			}
			public String getHom() {
				return Hom;
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
			private String AC_EAS;
			private String AC_Female;
			private String AC_OTH;
			private String AC_NFE;
			private String AC_Male;
			private String AC_FIN;
			private String AF_AFR;
			private String AF_AMR;
			private String AF_ASJ;
			private String AF_EAS;
			private String AF_FIN;
			private String AF_Female;
			private String AF_Male;
			private String AN_AMR;
			private String AN_ASJ;
			private String AN_EAS;
			private String AN_FIN;
			private String AN_Female;
			private String AN_Male;
			private String AN_NFE;
			private String AN_OTH;
			private String AN_raw;
			private String Hom_AFR;
			private String Hom_AMR;
			private String Hom_ASJ;
			private String Hom_EAS;
			private String Hom_FIN;
			private String Hom_Female;
			private String Hom_Male;
			private String Hom_NFE;
			private String Hom_OTH;
			private String Hom_raw;
			private String Hom;
//			private Integer gC_raw;
//			private Integer gC;
//			private Integer hom_SAS;
//			private Integer aC_SAS;
//			private Double aF_SAS;
//			private Integer aN_SAS;
			
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
			public String getAC_EAS() {
				return AC_EAS;
			}
			public String getAC_Female() {
				return AC_Female;
			}
			public String getAC_OTH() {
				return AC_OTH;
			}
			public String getAC_NFE() {
				return AC_NFE;
			}
			public String getAC_Male() {
				return AC_Male;
			}
			public String getAC_FIN() {
				return AC_FIN;
			}
			public String getAF_AFR() {
				return AF_AFR;
			}
			public String getAF_AMR() {
				return AF_AMR;
			}
			public String getAF_ASJ() {
				return AF_ASJ;
			}
			public String getAF_EAS() {
				return AF_EAS;
			}
			public String getAF_FIN() {
				return AF_FIN;
			}
			public String getAF_Female() {
				return AF_Female;
			}
			public String getAF_Male() {
				return AF_Male;
			}
			public String getAN_AMR() {
				return AN_AMR;
			}
			public String getAN_ASJ() {
				return AN_ASJ;
			}
			public String getAN_EAS() {
				return AN_EAS;
			}
			public String getAN_FIN() {
				return AN_FIN;
			}
			public String getAN_Female() {
				return AN_Female;
			}
			public String getAN_Male() {
				return AN_Male;
			}
			public String getAN_NFE() {
				return AN_NFE;
			}
			public String getAN_OTH() {
				return AN_OTH;
			}
			public String getAN_raw() {
				return AN_raw;
			}
			public String getHom_AFR() {
				return Hom_AFR;
			}
			public String getHom_AMR() {
				return Hom_AMR;
			}
			public String getHom_ASJ() {
				return Hom_ASJ;
			}
			public String getHom_EAS() {
				return Hom_EAS;
			}
			public String getHom_FIN() {
				return Hom_FIN;
			}
			public String getHom_Female() {
				return Hom_Female;
			}
			public String getHom_Male() {
				return Hom_Male;
			}
			public String getHom_NFE() {
				return Hom_NFE;
			}
			public String getHom_OTH() {
				return Hom_OTH;
			}
			public String getHom_raw() {
				return Hom_raw;
			}
			public String getHom() {
				return Hom;
			}
		}
	}
}