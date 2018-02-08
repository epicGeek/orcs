package com.nokia.ices.apps.fusion.command.domain.types;

public enum SubtoolCmdType {
    USER_MANAGE {
        @Override
        public String toString() {
            return "用户管理";
        }
    },
    GPRS {
        @Override
        public String toString() {
            return "GPRS";
        }
    },
    OTHER {
        @Override
        public String toString() {
            return "补充业务";
        }
    },
    ODB {
        @Override
        public String toString() {
            return "ODB限制";
        }
    },
    NETWORK {
        @Override
        public String toString() {
            return "智能网";
        }
    },
    SPECIAL {
        @Override
        public String toString() {
            return "特殊功能";
        }
    },
    BASIC {
        @Override
        public String toString() {
            return "基本业务";
        }
    },
    VOLTE {
        @Override
        public String toString() {
            return "VOLTE";
        }
    },
    LTE {
        @Override
        public String toString() {
            return "LTE";
        }
    },
    EMS {
        @Override
        public String toString() {
            return "EMS";
        }
    },
    
    其他{
    	@Override
        public String toString() {
            return "";
        }
    }
}
