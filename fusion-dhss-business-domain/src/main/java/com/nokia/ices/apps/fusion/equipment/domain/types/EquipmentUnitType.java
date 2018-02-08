package com.nokia.ices.apps.fusion.equipment.domain.types;

public enum EquipmentUnitType {
	PCC, OMU, AHUB, PGW_DSA {
		public String toString() {
			return "PGW-DSA";
		}
	},
	SOAP_GW {
		public String toString() {
			return "SOAP-GW";
		}
	},
	DRA, TIAMS, SWITCH, PGW, NTHLRFE, VC, R_DSA {
		public String toString() {
			return "R-DSA";
		}
	},
	ADM_SERVER {
		public String toString() {
			return "ADM-SERVER";
		}
	},
	INSTALL_SERVER {
		public String toString() {
			return "INSTALL-SERVER";
		}
	},
	CISCO_SWITCH {
		public String toString() {
			return "CISCO-SWITCH";
		}
	},
	BE_DSA {
		public String toString() {
			return "BE-DSA";
		}
	},
	OA, HSSFE, HSS_DRA {
		public String toString() {
			return "HSS-DRA";
		}
	},
	SGW, CE, SLH,ILON
}
