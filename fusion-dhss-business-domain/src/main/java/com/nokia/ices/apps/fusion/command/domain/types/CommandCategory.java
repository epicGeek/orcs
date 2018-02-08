package com.nokia.ices.apps.fusion.command.domain.types;

public enum CommandCategory {
    SECURITY {
        @Override
        public String toString() {
            return "安全管理";
        }
    },
    ENVIRONMENT {
        @Override
        public String toString() {
            return "软硬件维护";
        }
    },
    REMOTE {
        @Override
        public String toString() {
            return "局数据查询";
        }

    },
    NETWORK {
        @Override
        public String toString() {
            return "网络接口维护";
        }

    },
    EMS {
        @Override
        public String toString() {
            return "通知管理";
        }
    },
    USER_DATA {
        @Override
        public String toString() {
            return "用户数据管理";
        }

    },
    其他{
    	@Override
        public String toString() {
            return "";
        }
    }
    
}
