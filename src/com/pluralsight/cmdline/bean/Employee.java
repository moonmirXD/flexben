package com.pluralsight.cmdline.bean;

public class Employee extends Role{
    int employeeNumber, companyId;
    String firstName, lastName, email;

    public Employee(){}

    public Employee(int employeeNumber, int companyId, String firstName, String lastName, String email, int roleId, String roleName, String description) {
        super(roleId,roleName,description);
        this.employeeNumber = employeeNumber;
        this.companyId = companyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Employee(int employeeNumber, int companyId, String firstName, String lastName, String email) {
        this.employeeNumber = employeeNumber;
        this.companyId = companyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

}
