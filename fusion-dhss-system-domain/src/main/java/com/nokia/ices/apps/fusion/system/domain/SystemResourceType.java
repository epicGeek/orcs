package com.nokia.ices.apps.fusion.system.domain;

public enum SystemResourceType {
    MENU {
        @Override
        public String toString() {
            return "菜单";
        }

    },
    OPERATION {
        @Override
        public String toString() {
            return "操作";
        }
    },
    BUTTON {
        @Override
        public String toString() {
            return "按钮";
        }
    },
    AREA {
        @Override
        public String toString() {
            return "地区";
        }
    },
    EQUIPMENT {
        @Override
        public String toString() {
            return "设备";
        }
    },
    COMMAND_GROUP {
        @Override
        public String toString() {
            return "指令组";
        }
    },
    OTHER {
        @Override
        public String toString() {
            return "其他";
        }
    },
}
