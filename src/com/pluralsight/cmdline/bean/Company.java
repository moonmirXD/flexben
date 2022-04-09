package com.pluralsight.cmdline.bean;

public class Company {
    int companyId;
    String code, name, description;

    public Company(){}
    public Company(int companyId, String code, String name, String description) {
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
