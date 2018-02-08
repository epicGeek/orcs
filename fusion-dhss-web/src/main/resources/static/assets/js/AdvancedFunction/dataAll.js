window.results = {
    "rulePars": {
        "Gprs": {
            "tabName": "Gprs",
            "nwa": {
                "showType": "text",
                "zhName": "newtork access",
                "path": "/objects/hlr/nwa"
            },
            "actIMSIGprs": {
                "showType": "text",
                "zhName": "actIMSIGprs",
                "path": "/objects/hlr/actIMSIGprs"
            },
            "generalChargingCharacteristics": {
                "zhName": "General Charging Characteristics",
                "chargingCharacteristics": {
                    "showType": "text",
                    "zhName": "Charging Characteristics",
                    "path": "/objects/hlr/generalChargingCharacteristics/chargingCharacteristics"
                },
                "chargingCharacteristicsProfile": {
                    "showType": "text",
                    "zhName": "Charging Characteristics Profile",
                    "path": "/objects/hlr/generalChargingCharacteristics/chargingCharacteristicsProfile"
                },
                "chargingCharacteristicsBehavior": {
                    "showType": "text",
                    "zhName": "Charging Characteristics Behavior",
                    "path": "/objects/hlr/generalChargingCharacteristics/chargingCharacteristicsBehavior"
                }
            },
            "pdpContext": {
                "zhName": "pdpContext",
                "id": {
                    "showType": "text",
                    "zhName": "pdpContext ID",
                    "path": "/objects/hlr/pdpContext/id"
                },
                "type": {
                    "showType": "text",
                    "zhName": "type",
                    "path": "/objects/hlr/pdpContext/type"
                },
                "qosProfile": {
                    "showType": "text",
                    "zhName": "qosProfile",
                    "path": "/objects/hlr/pdpContext/qosProfile"
                },
                "apn": {
                    "showType": "text",
                    "zhName": "apn",
                    "path": "/objects/hlr/pdpContext/apn"
                },
                "apnArea": {
                    "showType": "text",
                    "zhName": "apnArea",
                    "path": "/objects/hlr/pdpContext/apnArea"
                }
            }
        },
        "General": {
            "tabName": "General",
            "umtsSubscriber": {
                "zhName": "umtsSubscriber",
                "accTypeGSM": {
                    "showType": "checkbox",
                    "true": {
                        "path": "/objects/hlr/umtsSubscriber/accTypeGSM",
                        "value": "true",
                        "valueDes": "ACC_TYPE_GSM"
                    }
                },
                "accTypeGERAN": {
                    "showType": "checkbox",
                    "true": {
                        "path": "/objects/hlr/umtsSubscriber/accTypeGERAN",
                        "value": "true",
                        "valueDes": "ACC_TYPE_GERAN"
                    }
                },
                "accTypeUTRAN": {
                    "showType": "checkbox",
                    "true": {
                        "path": "/objects/hlr/umtsSubscriber/accTypeUTRAN",
                        "value": "true",
                        "valueDes": "ACC_TYPE_UTRAN"
                    }
                }
            },
            "imsiActive": {
                "showType": "radio",
                "zhName": "用户激活",
                "true": {
                    "path": "/objects/hlr/imsiActivea",
                    "value": "true",
                    "valueDes": "true"
                },
                "false": {
                    "path": "/objects/hlr/imsiActivea",
                    "value": "false",
                    "valueDes": "false"
                }
            },
            "mscat": {
                "showType": "radio",
                "zhName": "用户数据类型",
                "true": {
                    "path": "/objects/hlr/mscat",
                    "value": "10",
                    "valueDes": "normal"
                },
                "false": {
                    "path": "/objects/hlr/mscat",
                    "value": "20",
                    "valueDes": "test"
                }
            },
            "ts11": {
                "zhName": "Tele services",
                "msisdn": {
                    "showType": "text",
                    "zhName": "msisdn",
                    "path": "/objects/hlr/ts11/msisdn"
                }
            },
            "ts21": {
                "zhName": "Short message (mobile terminating)",
                "msisdn": {
                    "showType": "text",
                    "zhName": "msisdn",
                    "path": "/objects/hlr/ts21/msisdn"
                }
            },
            "ts22": {
                "zhName": "Short message (mobile originating)",
                "msisdn": {
                    "showType": "text",
                    "zhName": "msisdn",
                    "path": "/objects/hlr/ts22/msisdn"
                }
            },
            "bs30genr": {
                "showType": "text",
                "zhName": "Data circuit duplex synchronous services",
                "path": "/objects/hlr/bs30genr/msisdn"
            },
            "imeisv": {
                "showType": "text",
                "zhName": "imeisv",
                "path": "/objects/hlr/imeisv"
            }
        },
        "Auc": {
            "tabName": "Auc",
            "imsi": {
                "showType": "text",
                "zhName": "imsi",
                "path": "/objects/auc/imsi"
            },
            "encKey": {
                "showType": "text",
                "zhName": "加密ki",
                "path": "/objects/auc/encKey"
            },
            "test": {
                "zhName": "test",
                "algoId": {
                    "showType": "text",
                    "zhName": "算法",
                    "path": "/objects/auc/algoId"
                },
                "kdbId": {
                    "showType": "text",
                    "zhName": "密钥ID",
                    "path": "/objects/auc/kdbId"
                }
            },
            "acsub": {
                "showType": "radio",
                "zhName": "加密用户类型",
                "gsm": {
                    "path": "/objects/auc/acsub",
                    "value": "1",
                    "valueDes": "GSM"
                },
                "umts": {
                    "path": "/objects/auc/acsub",
                    "value": "2",
                    "valueDes": "UMTS"
                }
            },
            "amf": {
                "showType": "text",
                "zhName": "amf",
                "path": "/objects/auc/amf"
            }
        },
        "CamelServices": {
            "tabName": "Camel Services",
            "ocsi": {
                "zhName": "ocsi",
                "operatorServiceName": {
                    "showType": "text",
                    "zhName": "Operator Service Name",
                    "path": "/objects/hlr/ocsi/operatorServiceName"
                },
                "csiState": {
                    "showType": "text",
                    "zhName": "State",
                    "path": "/objects/hlr/ocsi/csiState"
                },
                "csiNotify": {
                    "showType": "text",
                    "zhName": "Notify",
                    "path": "/objects/hlr/ocsi/csiNotify"
                }
            },
            "tcsi": {
                "zhName": "tcsi",
                "operatorServiceName": {
                    "showType": "text",
                    "zhName": "Operator Service Name",
                    "path": "/objects/hlr/tcsi/operatorServiceName"
                },
                "csiState": {
                    "showType": "text",
                    "zhName": "State",
                    "path": "/objects/hlr/tcsi/csiState"
                },
                "csiNotify": {
                    "showType": "text",
                    "zhName": "Notify",
                    "path": "/objects/hlr/tcsi/csiNotify"
                }
            }
        },
        "Restrictions": {
            "tabName": "Restrictions",
            "rr": {
                "showType": "text",
                "zhName": "RoamingProfile",
                "path": "/objects/hlr/rr"
            },
            "sr": {
                "showType": "text",
                "zhName": "sam",
                "path": "/objects/hlr/sr"
            }
        },
        "MobileData": {
            "tabName": "Mobile Data",
            "vlrMobData": {
                "zhName": "VLR Mobile Data",
                "vlrIdValid": {
                    "showType": "text",
                    "zhName": "vlrIdValid",
                    "path": "/objects/hlr/vlrMobData/vlrIdValid"
                },
                "mscNumber": {
                    "showType": "text",
                    "zhName": "mscNumber",
                    "path": "/objects/hlr/vlrMobData/mscNumber"
                },
                "isdnNumberOfVLR": {
                    "showType": "text",
                    "zhName": "vlrNumber",
                    "path": "/objects/hlr/vlrMobData/isdnNumberOfVLR"
                },
                "msPurged": {
                    "showType": "text",
                    "zhName": "msPurged by VLR",
                    "path": "/objects/hlr/vlrMobData/msPurged"
                },
                "mobileTerminatingCallPossible": {
                    "showType": "text",
                    "zhName": "mobile Terminating Call Possible",
                    "path": "/objects/hlr/vlrMobData/mobileTerminatingCallPossible"
                },
                "plmnAllowed": {
                    "showType": "text",
                    "zhName": "plmn Allowed",
                    "path": "/objects/hlr/vlrMobData/plmnAllowed"
                },
                "roamingAreaAllowed": {
                    "showType": "text",
                    "zhName": "roaming AreaAllowed",
                    "path": "/objects/hlr/vlrMobData/roamingAreaAllowed"
                },
                "mscAreaRestrictedReceived": {
                    "showType": "text",
                    "zhName": "msc Area Restricted Received",
                    "path": "/objects/hlr/vlrMobData/mscAreaRestrictedReceived"
                },
                "supportedLCSCapabilitySetsForVLR": {
                    "showType": "text",
                    "zhName": "supported LCS Capability Sets for VLR",
                    "path": "/objects/hlr/vlrMobData/supportedLCSCapabilitySetsForVLR"
                },
                "supportedCAMELPhaseByVLR": {
                    "showType": "text",
                    "zhName": "supported CAMEL Phase By VLR",
                    "path": "/objects/hlr/vlrMobData/supportedCAMELPhaseByVLR"
                },
                "supportedMAPVersionForLUP": {
                    "showType": "text",
                    "zhName": "supported MAP Version For LUP",
                    "path": "/objects/hlr/vlrMobData/supportedMAPVersionForLUP"
                },
                "locUpdateCSTimestamp": {
                    "showType": "text",
                    "zhName": "location Update CS Timestamp",
                    "path": "/objects/hlr/vlrMobData/locUpdateCSTimestamp"
                }
            },
            "sgsnMobData": {
                "zhName": "SGSN Mobile Data",
                "msPurged": {
                    "showType": "text",
                    "zhName": "msPurged by SGSN",
                    "path": "/objects/hlr/sgsnMobData/msPurged"
                },
                "isdnNumberOfSGSN": {
                    "showType": "text",
                    "zhName": "SGSN number",
                    "path": "/objects/hlr/sgsnMobData/isdnNumberOfSGSN"
                },
                "sgsnExtQos": {
                    "showType": "text",
                    "zhName": "sgsnExtQos",
                    "path": "/objects/hlr/sgsnMobData/sgsnExtQos"
                },
                "sgsnIdValid": {
                    "showType": "text",
                    "zhName": "sgsnIdValid",
                    "path": "/objects/hlr/sgsnMobData/sgsnIdValid"
                },
                "plmnAllowed": {
                    "showType": "text",
                    "zhName": "plmnAllowed",
                    "path": "/objects/hlr/sgsnMobData/plmnAllowed"
                },
                "gprsAllowed": {
                    "showType": "text",
                    "zhName": "gprsAllowed",
                    "path": "/objects/hlr/sgsnMobData/gprsAllowed"
                },
                "sgsnCamelNot": {
                    "showType": "text",
                    "zhName": "sgsnCamelNot",
                    "path": "/objects/hlr/sgsnMobData/sgsnCamelNot"
                },
                "locUpdatePSTimestamp": {
                    "showType": "text",
                    "zhName": "location Update PS Timestamp",
                    "path": "/objects/hlr/sgsnMobData/locUpdatePSTimestamp"
                },
                "sgsnAreaRestRcvd": {
                    "showType": "text",
                    "zhName": "sgsnAreaRestRcvd",
                    "path": "/objects/hlr/sgsnMobData/sgsnAreaRestRcvd"
                },
                "roamingAreaAllowed": {
                    "showType": "text",
                    "zhName": "roamingAreaAllowed",
                    "path": "/objects/hlr/sgsnMobData/roamingAreaAllowed"
                },
                "supportedCAMELPhaseBySGSN": {
                    "showType": "text",
                    "zhName": "supportedCAMELPhaseBySGSN",
                    "path": "/objects/hlr/sgsnMobData/supportedCAMELPhaseBySGSN"
                },
                "supportedMAPVersionForLUP": {
                    "showType": "text",
                    "zhName": "supportedMAPVersionForLUP",
                    "path": "/objects/hlr/sgsnMobData/supportedMAPVersionForLUP"
                },
                "featuresNotSupportedBySGSN": {
                    "showType": "text",
                    "zhName": "featuresNotSupportedBySGSN",
                    "path": "/objects/hlr/sgsnMobData/featuresNotSupportedBySGSN"
                }
            }
        },
        "OperatorDeterminedBarring": {
            "tabName": "Operator Determined Barring",
            "odboc": {
                "showType": "checkbox",
                "zhName": "呼出限制",
                "odboc0": {
                    "path": "/objects/hlr/odboc",
                    "value": "0",
                    "valueDes": "None"
                },
                "odboc1": {
                    "path": "/objects/hlr/odboc",
                    "value": "1",
                    "valueDes": "All outgoing calls"
                },
                "odboc2": {
                    "path": "/objects/hlr/odboc",
                    "value": "2",
                    "valueDes": "All outgoing international calls"
                },
                "odboc3": {
                    "path": "/objects/hlr/odboc",
                    "value": "3",
                    "valueDes": "Barring of all outgoing international calls  except those directed to the HPLMN country"
                },
                "odboc4": {
                    "path": "/objects/hlr/odboc",
                    "value": "4",
                    "valueDes": "All outgoing calls when roaming outside the HPLMN country"
                },
                "odboc5": {
                    "path": "/objects/hlr/odboc",
                    "value": "5",
                    "valueDes": "All outgoing calls when roaming outside HPLMN country except SMS"
                }
            },
            "odbic": {
                "showType": "checkbox",
                "zhName": "呼入限制",
                "odbic0": {
                    "path": "/objects/hlr/odbic",
                    "value": "0",
                    "valueDes": "None"
                },
                "odbic1": {
                    "path": "/objects/hlr/odbic",
                    "value": "1",
                    "valueDes": "All incoming calls"
                },
                "odbic2": {
                    "path": "/objects/hlr/odbic",
                    "value": "2",
                    "valueDes": "All incoming calls when roaming outside the HPLMN country"
                },
                "odbic3": {
                    "path": "/objects/hlr/odbic",
                    "value": "3",
                    "valueDes": "All incoming calls when roaming outside HPLMN country except SMS"
                }
            },
            "odbBaroam": {
                "showType": "checkbox",
                "zhName": "漫游限制",
                "odbic0": {
                    "path": "/objects/hlr/odbr",
                    "value": "0",
                    "valueDes": "None"
                },
                "odbic1": {
                    "path": "/objects/hlr/odbr",
                    "value": "1",
                    "valueDes": "Outside the HPLMN"
                },
                "odbic2": {
                    "path": "/objects/hlr/odbr",
                    "value": "2",
                    "valueDes": "Outside the HPLMN country"
                }
            },
            "osb1": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/osb1",
                    "value": "true",
                    "valueDes": "osb1"
                }
            },
            "osb2": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/osb2",
                    "value": "true",
                    "valueDes": "osb2"
                }
            },
            "osb3": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/osb3",
                    "value": "true",
                    "valueDes": "osb3"
                }
            },
            "osb4": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/osb4",
                    "value": "true",
                    "valueDes": "osb4"
                }
            },
            "odbgprs": {
                "showType": "select",
                "zhName": "odbgprs",
                "odbgprs0": {
                    "path": "/objects/hlr/odbgprs",
                    "value": "0",
                    "valueDes": "None"
                },
                "odbgprs1": {
                    "path": "/objects/hlr/odbgprs",
                    "value": "1",
                    "valueDes": "While roaming in VPLMN"
                },
                "odbgprs2": {
                    "path": "/objects/hlr/odbgprs",
                    "value": "2",
                    "valueDes": "In all PLMN"
                }
            }
        },
        "Eps": {
            "tabName": "Eps",
            "epsPdnContext": {
                "zhName": "epsPdnContext",
                "contextId": {
                    "showType": "text",
                    "zhName": "epsPdnContext id",
                    "path": "/objects/hlr/epsPdnContext/contextId"
                },
                "apn": {
                    "showType": "text",
                    "zhName": "apn",
                    "path": "/objects/hlr/epsPdnContext/apn"
                },
                "type": {
                    "showType": "text",
                    "zhName": "type",
                    "path": "/objects/hlr/epsPdnContext/type"
                },
                "pdnGwDynamicAllocation": {
                    "showType": "text",
                    "zhName": "pdnGwDynamicAllocation",
                    "path": "/objects/hlr/epsPdnContext/pdnGwDynamicAllocation"
                },
                "vplmnAddressAllowed": {
                    "showType": "text",
                    "zhName": "vplmnAddressAllowed",
                    "path": "/objects/hlr/epsPdnContext/vplmnAddressAllowed"
                },
                "pdnGwIPv4": {
                    "showType": "text",
                    "zhName": "pdnGwIPv4",
                    "path": "/objects/hlr/epsPdnContext/pdnGwIPv4"
                },
                "maxBandwidthUp": {
                    "showType": "text",
                    "zhName": "maxBandwidthUp",
                    "path": "/objects/hlr/epsPdnContext/maxBandwidthUp"
                },
                "maxBandwidthDown": {
                    "showType": "text",
                    "zhName": "maxBandwidthDown",
                    "path": "/objects/hlr/epsPdnContext/maxBandwidthDown"
                },
                "qos": {
                    "showType": "text",
                    "zhName": "qos",
                    "path": "/objects/hlr/epsPdnContext/qos"
                }
            },
            "eps": {
                "zhName": "eps",
                "defaultPdnContextId": {
                    "showType": "text",
                    "zhName": "default PdnContext Id",
                    "path": "/objects/hlr/eps/defaultPdnContextId"
                },
                "maxBandwidthUp": {
                    "showType": "text",
                    "zhName": "max Bandwidth Up",
                    "path": "/objects/hlr/eps/maxBandwidthUp"
                },
                "maxBandwidthDown": {
                    "showType": "text",
                    "zhName": "max Bandwidth Down",
                    "path": "/objects/hlr/eps/maxBandwidthDown"
                },
                "msisdn": {
                    "showType": "text",
                    "zhName": "msisdn",
                    "path": "/objects/hlr/eps/msisdn"
                },
                "msPurgedEps": {
                    "showType": "text",
                    "zhName": "msPurgedEps",
                    "path": "/objects/hlr/eps/msPurgedEps"
                },
                "odbPOAccessEps": {
                    "showType": "text",
                    "zhName": "odbPOAccessEps",
                    "path": "/objects/hlr/eps/odbPOAccessEps"
                },
                "rfspIndex": {
                    "showType": "text",
                    "zhName": "rfspIndex",
                    "path": "/objects/hlr/eps/rfspIndex"
                },
                "imsVoiceOverPS": {
                    "showType": "text",
                    "zhName": "imsVoiceOverPS",
                    "path": "/objects/hlr/eps/imsVoiceOverPS"
                },
                "vplmnIdS6a": {
                    "showType": "text",
                    "zhName": "vplmnIdS6a",
                    "path": "/objects/hlr/eps/vplmnIdS6a"
                },
                "epsPsRoamAreaMmeName": {
                    "showType": "text",
                    "zhName": "epsPsRoamAreaMmeName",
                    "path": "/objects/hlr/eps/epsPsRoamAreaMmeName"
                },
                "sessionTransferNumber": {
                    "showType": "text",
                    "zhName": "sessionTransferNumber",
                    "path": "/objects/hlr/eps/sessionTransferNumber"
                },
                "plmnStatus": {
                    "showType": "text",
                    "zhName": "plmnStatus",
                    "path": "/objects/hlr/eps/plmnStatus"
                },
                "pdnChargingCharacteristics": {
                    "zhName": "pdnChargingCharacteristics",
                    "chargingCharacteristics": {
                        "showType": "text",
                        "zhName": "Charging Characteristics",
                        "path": "/objects/hlr/epsPdnContext/pdnChargingCharacteristics/chargingCharacteristics"
                    },
                    "chargingCharacteristicsProfile": {
                        "showType": "text",
                        "zhName": "Charging Characteristics Profile",
                        "path": "/objects/hlr/epsPdnContext/pdnChargingCharacteristics/chargingCharacteristicsProfile"
                    },
                    "chargingCharacteristicsBehavior": {
                        "showType": "text",
                        "zhName": "Charging Characteristics Behavior",
                        "path": "/objects/hlr/epsPdnContext/pdnChargingCharacteristics/chargingCharacteristicsBehavior"
                    }
                }
            }
        },
        "SupplementaryServices": {
            "tabName": "Supplementary Services",
            "clip": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/clip",
                    "value": "true",
                    "valueDes": "Calling line identification presentation"
                }
            },
            "clipOverride": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/clipOverride",
                    "value": "true",
                    "valueDes": "clipOverride"
                }
            },
            "caw": {
                "zhName": "call waitting",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/caw/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/caw/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/caw/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "natss14": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/natss14",
                    "value": "true",
                    "valueDes": "srbt"
                }
            },
            "mrbt": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/mrbt",
                    "value": "true",
                    "valueDes": "mrbt"
                }
            },
            "hold": {
                "showType": "checkbox",
                "true": {
                    "path": "/objects/hlr/hold",
                    "value": "true",
                    "valueDes": "call hold"
                }
            },
            "baoc": {
                "zhName": "Barring all outgoing calls",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/baoc/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/baoc/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/baoc/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "boic": {
                "zhName": "Barring of outgoing international calls",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/boic/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/boic/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/boic/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "boicexhc": {
                "zhName": "Barring of outgoing international calls except those directed to HPLMN country",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/boic/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/boic/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/boic/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "baic": {
                "zhName": "Barring all incoming calls",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/baic/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/baic/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/baic/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "bicroam": {
                "zhName": "Barring of incoming calls while roaming outside HPLMN country",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/bicroam/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/bicroam/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/bicroam/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "cfu": {
                "zhName": "Call forwarding unconditional",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/cfu/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/cfu/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/cfu/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "cfb": {
                "zhName": "Call forwarding on mobile subscriber busy",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/cfb/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/cfb/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/cfb/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "cfnrc": {
                "zhName": "Call forwarding on mobile subscriber not reachable",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/cfnrc/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/cfnrc/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/cfnrc/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            },
            "cfnry": {
                "zhName": "Call forwarding on no reply",
                "basicServiceGroup": {
                    "showType": "text",
                    "zhName": "basicServiceGroup",
                    "path": "/objects/hlr/cfnry/basicServiceGroup"
                },
                "status": {
                    "showType": "select",
                    "zhName": "status",
                    "status0": {
                        "path": "/objects/hlr/cfnry/status",
                        "value": "4",
                        "valueDes": "4 provisioned"
                    },
                    "status1": {
                        "path": "/objects/hlr/cfnry/status",
                        "value": "5",
                        "valueDes": "5 provisioned + active"
                    }
                }
            }
        }
    },
    "keyAll": {
        "/objects/hlr/sgsnMobData/featuresNotSupportedBySGSN": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "featuresNotSupportedBySGSN",
                "path": "extCamel",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/vlrMobData/mscAreaRestrictedReceived": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "msc Area Restricted Received",
                "path": "false",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/pdnChargingCharacteristics/chargingCharacteristicsBehavior": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "Charging Characteristics Behavior",
                "path": "5",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "Charging Characteristics Behavior",
                "path": "5",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/eps/maxBandwidthUp": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "max Bandwidth Up",
                "path": "50000000",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/pdnChargingCharacteristics/chargingCharacteristics": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "Charging Characteristics",
                "path": "prepaid",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "Charging Characteristics",
                "path": "prepaid",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/sgsnMobData/sgsnCamelNot": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "sgsnCamelNot",
                "path": "false",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/sgsnMobData/sgsnIdValid": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "sgsnIdValid",
                "path": "false",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/vlrMobData/plmnAllowed": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "plmn Allowed",
                "path": "true",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/vlrMobData/vlrIdValid": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "vlrIdValid",
                "path": "false",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/contextId": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "epsPdnContext id",
                "path": "2",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "epsPdnContext id",
                "path": "1",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/pdpContext/apnArea": [
            {
                "tabName": "Gprs",
                "showType": "text",
                "zhName": "apnArea",
                "path": "HPLMN",
                "value": null,
                "valueDes": null,
                "checked": false
            }
        ],
        "/objects/hlr/odbgprs": [
            {
                "tabName": "Operator Determined Barring",
                "showType": "select",
                "zhName": "odbgprs",
                "path": "0",
                "value": "1",
                "valueDes": "While roaming in VPLMN",
                "checked": false
            }
        ],
        "/objects/hlr/pdpContext/id": [
            {
                "tabName": "Gprs",
                "showType": "text",
                "zhName": "pdpContext ID",
                "path": "1",
                "value": null,
                "valueDes": null,
                "checked": false
            }
        ],
        "/objects/hlr/vlrMobData/msPurged": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "msPurged by VLR",
                "path": "false",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/eps/msisdn": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "msisdn",
                "path": "8615911199420",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/pdnGwDynamicAllocation": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "pdnGwDynamicAllocation",
                "path": "true",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "pdnGwDynamicAllocation",
                "path": "true",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/pdnChargingCharacteristics/chargingCharacteristicsProfile": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "Charging Characteristics Profile",
                "path": "4",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "Charging Characteristics Profile",
                "path": "4",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/sgsnMobData/sgsnExtQos": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "sgsnExtQos",
                "path": "false",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/auc/algoId": [
            {
                "tabName": "Auc",
                "showType": "text",
                "zhName": "算法",
                "path": "99",
                "value": "20",
                "valueDes": "test",
                "checked": false
            }
        ],
        "/objects/hlr/clipOverride": [
            {
                "tabName": "Supplementary Services",
                "showType": "checkbox",
                "zhName": "Charging Characteristics Behavior",
                "path": "false",
                "value": "true",
                "valueDes": "Calling line identification presentation",
                "checked": false
            }
        ],
        "/objects/hlr/vlrMobData/mobileTerminatingCallPossible": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "mobile Terminating Call Possible",
                "path": "true",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/odbic": [
            {
                "tabName": "Operator Determined Barring",
                "showType": "checkbox",
                "zhName": "呼入限制",
                "path": "3",
                "value": "2",
                "valueDes": "All incoming calls when roaming outside the HPLMN country",
                "checked": false
            }
        ],
        "/objects/auc/kdbId": [
            {
                "tabName": "Auc",
                "showType": "text",
                "zhName": "密钥ID",
                "path": "1",
                "value": "20",
                "valueDes": "test",
                "checked": false
            }
        ],
        "/objects/auc/acsub": [
            {
                "tabName": "Auc",
                "showType": "radio",
                "zhName": "加密用户类型",
                "path": "2",
                "value": "1",
                "valueDes": "GSM",
                "checked": false
            }
        ],
        "/objects/hlr/sgsnMobData/gprsAllowed": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "gprsAllowed",
                "path": "true",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/clip": [
            {
                "tabName": "Supplementary Services",
                "showType": "checkbox",
                "zhName": "Charging Characteristics Behavior",
                "path": "true",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/eps/maxBandwidthDown": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "max Bandwidth Down",
                "path": "256000000",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/vplmnAddressAllowed": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "vplmnAddressAllowed",
                "path": "true",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "vplmnAddressAllowed",
                "path": "true",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/umtsSubscriber/accTypeUTRAN": [
            {
                "tabName": "General",
                "showType": "checkbox",
                "zhName": "umtsSubscriber",
                "path": "true",
                "value": "true",
                "valueDes": "ACC_TYPE_GERAN",
                "checked": true
            }
        ],
        "/objects/hlr/pdpContext/apn": [
            {
                "tabName": "Gprs",
                "showType": "text",
                "zhName": "apn",
                "path": "CMNET",
                "value": null,
                "valueDes": null,
                "checked": false
            }
        ],
        "/objects/hlr/sgsnMobData/msPurged": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "msPurged by SGSN",
                "path": "false",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/sr": [
            {
                "tabName": "Restrictions",
                "showType": "text",
                "zhName": "sam",
                "path": "2",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/auc/amf": [
            {
                "tabName": "Auc",
                "showType": "text",
                "zhName": "amf",
                "path": "0000",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/odbr": [
            {
                "tabName": "Operator Determined Barring",
                "showType": "checkbox",
                "zhName": "漫游限制",
                "path": "0",
                "value": "1",
                "valueDes": "Outside the HPLMN",
                "checked": false
            }
        ],
        "/objects/hlr/pdpContext/type": [
            {
                "tabName": "Gprs",
                "showType": "text",
                "zhName": "type",
                "path": "2",
                "value": null,
                "valueDes": null,
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/apn": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "apn",
                "path": "IMS",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "apn",
                "path": "cmnet",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/ts21/msisdn": [
            {
                "tabName": "General",
                "showType": "text",
                "zhName": "msisdn",
                "path": "8615911199420",
                "value": "20",
                "valueDes": "test",
                "checked": false
            }
        ],
        "/objects/hlr/umtsSubscriber/accTypeGERAN": [
            {
                "tabName": "General",
                "showType": "checkbox",
                "zhName": "umtsSubscriber",
                "path": "true",
                "value": "true",
                "valueDes": "ACC_TYPE_GSM",
                "checked": true
            }
        ],
        "/objects/hlr/sgsnMobData/supportedMAPVersionForLUP": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "supportedMAPVersionForLUP",
                "path": "3",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/tcsi/operatorServiceName": [
            {
                "tabName": "Camel Services",
                "showType": "text",
                "zhName": "Operator Service Name",
                "path": "VOLTE_T",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/vlrMobData/supportedCAMELPhaseByVLR": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "supported CAMEL Phase By VLR",
                "path": "1",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/eps/defaultPdnContextId": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "default PdnContext Id",
                "path": "1",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/vlrMobData/roamingAreaAllowed": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "roaming AreaAllowed",
                "path": "true",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/sgsnMobData/supportedCAMELPhaseBySGSN": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "supportedCAMELPhaseBySGSN",
                "path": "1",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/vlrMobData/supportedMAPVersionForLUP": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "supported MAP Version For LUP",
                "path": "3",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/sgsnMobData/sgsnAreaRestRcvd": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "sgsnAreaRestRcvd",
                "path": "false",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/maxBandwidthDown": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "maxBandwidthDown",
                "path": "128000000",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "maxBandwidthDown",
                "path": "256000000",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/maxBandwidthUp": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "maxBandwidthUp",
                "path": "128000000",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "maxBandwidthUp",
                "path": "50000000",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/type": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "type",
                "path": "both",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "type",
                "path": "both",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/nwa": [
            {
                "tabName": "Gprs",
                "showType": "text",
                "zhName": "newtork access",
                "path": "3",
                "value": null,
                "valueDes": null,
                "checked": false
            }
        ],
        "/objects/auc/encKey": [
            {
                "tabName": "Auc",
                "showType": "text",
                "zhName": "加密ki",
                "path": "FF4E48CB2E5BD0CDAF878F94E82091B7",
                "value": "20",
                "valueDes": "test",
                "checked": false
            }
        ],
        "/objects/hlr/ts22/msisdn": [
            {
                "tabName": "General",
                "showType": "text",
                "zhName": "msisdn",
                "path": "8615911199420",
                "value": "20",
                "valueDes": "test",
                "checked": false
            }
        ],
        "/objects/hlr/odboc": [
            {
                "tabName": "Operator Determined Barring",
                "showType": "checkbox",
                "zhName": "呼出限制",
                "path": "1",
                "value": "4",
                "valueDes": "All outgoing calls when roaming outside the HPLMN country",
                "checked": false
            }
        ],
        "/objects/hlr/tcsi/csiState": [
            {
                "tabName": "Camel Services",
                "showType": "text",
                "zhName": "State",
                "path": "1",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/tcsi/csiNotify": [
            {
                "tabName": "Camel Services",
                "showType": "text",
                "zhName": "Notify",
                "path": "2",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/sgsnMobData/plmnAllowed": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "plmnAllowed",
                "path": "true",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/actIMSIGprs": [
            {
                "tabName": "Gprs",
                "showType": "text",
                "zhName": "actIMSIGprs",
                "path": "false",
                "value": null,
                "valueDes": null,
                "checked": false
            }
        ],
        "/objects/auc/imsi": [
            {
                "tabName": "Auc",
                "showType": "text",
                "zhName": "imsi",
                "path": "460029111909420",
                "value": "20",
                "valueDes": "test",
                "checked": false
            }
        ],
        "/objects/hlr/umtsSubscriber/accTypeGSM": [
            {
                "tabName": "General",
                "showType": "checkbox",
                "zhName": "umtsSubscriber",
                "path": "true",
                "value": "true",
                "valueDes": null,
                "checked": true
            }
        ],
        "/objects/hlr/sgsnMobData/roamingAreaAllowed": [
            {
                "tabName": "Mobile Data",
                "showType": "text",
                "zhName": "roamingAreaAllowed",
                "path": "true",
                "value": "2",
                "valueDes": "UMTS",
                "checked": false
            }
        ],
        "/objects/hlr/pdpContext/qosProfile": [
            {
                "tabName": "Gprs",
                "showType": "text",
                "zhName": "qosProfile",
                "path": "PROF1",
                "value": null,
                "valueDes": null,
                "checked": false
            }
        ],
        "/objects/hlr/mscat": [
            {
                "tabName": "General",
                "showType": "radio",
                "zhName": "用户数据类型",
                "path": "20",
                "value": "10",
                "valueDes": "normal",
                "checked": false
            }
        ],
        "/objects/hlr/eps/msPurgedEps": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "msPurgedEps",
                "path": "true",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ],
        "/objects/hlr/ts11/msisdn": [
            {
                "tabName": "General",
                "showType": "text",
                "zhName": "msisdn",
                "path": "8615911199420",
                "value": "20",
                "valueDes": "test",
                "checked": false
            }
        ],
        "/objects/hlr/epsPdnContext/qos": [
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "qos",
                "path": "QCI_5",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            },
            {
                "tabName": "Eps",
                "showType": "text",
                "zhName": "qos",
                "path": "QCI_9",
                "value": "2",
                "valueDes": "In all PLMN",
                "checked": false
            }
        ]
    }
}