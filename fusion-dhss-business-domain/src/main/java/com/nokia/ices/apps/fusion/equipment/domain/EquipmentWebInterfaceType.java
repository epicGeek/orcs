package com.nokia.ices.apps.fusion.equipment.domain;

public enum EquipmentWebInterfaceType {
    LUMAF, LEMAF, TSP, VC, ILO, OA, 
    PGW_OPEN_HLR {
        @Override
        public String toString() {
            return "PGW开通(HLR)";
        }
    },
    PGW_OPEN_HSS {
        @Override
        public String toString() {
            return "PGW开通(HSS)";
        }
    },
    SOAPGW_OPEN {
        @Override
        public String toString() {
            return "SOAPGW开通";
        }
    },
    ADM,ILON
}
