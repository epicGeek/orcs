package com.nokia.ices.apps.fusion.system.repository.types;

public enum SystemOperationLogOpType {
    /**
     * “Login”(登录应用资源)
     * “Logout” （登出应用资源）
     * “AddPrivilege”（用户添加和权限相关的任何信息，包括添加用户、用户组、权限、角色等）
     * “DelPrivilege”（用户删除和权限相关的任何信息，包括添加用户、用户组、权限、角色等）
     * “UpdatePrivilege”（用户更新和权限相关的任何信息，包括添加用户、用户组、权限、角色等）
     * “View”（业务数据查阅）
     * “Add”（业务数据增加）
     * “Update”（业务数据更新）
     * “Del”（业务数据删除）
     * “Other”（其它类别）
     */
    Login,Logout,AddPrivilege,DelPrivilege,UpdatePrivilege,View,Add,Update,Del,Other
}
