package com.pluralsight.cmdline.bean;

import java.util.Date;

public class Account extends Employee {
    int accountId, employeeId;
    String password;
    char isActive;
    Date dateUpdated;

    public Account(int accountId, int employeeId, String password, char isActive) {
        this.accountId = accountId;
        this.employeeId = employeeId;
        this.password = password;
        this.isActive = isActive;
    }

    public Account(int accountId, int employeeId, String password, char isActive, Date dateUpdated) {
        this(accountId, employeeId, password, isActive);
        this.dateUpdated = dateUpdated;
    }

    public Account(int accountId, int employeeId, String password, char isActive, int employeeNumber, int companyId, String firstName, String lastName, String email, int roleId, String roleName, String description) {
        super(employeeNumber,companyId,firstName,lastName,email,roleId,roleName,description);
        this.accountId = accountId;
        this.employeeId = employeeId;
        this.password = password;
        this.isActive = isActive;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getPassword() {
        return password;
    }

    public char getIsActive() {
        return isActive;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }
}