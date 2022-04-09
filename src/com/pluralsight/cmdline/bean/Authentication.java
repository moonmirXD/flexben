package com.pluralsight.cmdline.bean;

import com.pluralsight.cmdline.ErrorHandler;
import com.pluralsight.cmdline.dao.AuthenticationDao;

public class Authentication {
    private int employeeID;
    private String password, email;
    private char isActive;
    private static Account user;

    AuthenticationDao authenticationDao = new AuthenticationDao();

    public Authentication(){}

    public Authentication(String email, String password) {
        this.email = email;
        this.password = password;
        this.isActive = 'Y';
    }

    public Authentication(String email, String password, char isActive) {
        this(email, password);
        this.isActive = isActive;
    }

    public boolean authenticate(){
        if(this.isActive == 'N') {
            ErrorHandler.throwError("The user is inactive", 400);
            return false;
        }

        if(!authenticationDao.getAccounts().stream().anyMatch(account -> this.email.equals(account.getEmail()) && this.password.equals(account.getPassword()))) {
           return false;
        }

        Account act = authenticationDao.getAccounts().stream().filter(account -> this.email.equals(account.getEmail())).findFirst().get();
        setUser(act);
        System.out.println("Welcome " + act.getFirstName() + " " + act.getLastName() + " [" + act.getDescription() +
                "]!");
        return true;
    }

    public static Company getCompany(){
        return AuthenticationDao.getCompany(getUser().companyId);
    }

    public void setUser(Account user){
            this.user =  user;
    }

    public static Account getUser(){
        return user;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
