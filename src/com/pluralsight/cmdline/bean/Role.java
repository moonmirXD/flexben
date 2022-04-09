package com.pluralsight.cmdline.bean;

public class Role {
    int roleId;
    String roleName, description;

    public Role(){

    }

    public Role(int roleId, String roleName, String description) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getDescription() {
        return description;
    }
}

