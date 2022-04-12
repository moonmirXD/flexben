package com.pluralsight.cmdline.bean;

import java.util.Date;

public class Reimbursement extends ReimbursementDetail{
    int flexReimbursementId, employeeId, flexCutOffId;
    double totalReimbursementAmount;
    Date dateSubmitted, dateUpdated;
    String status, transactionNumber;

    public Reimbursement(int flexReimbursementId, int employeeId, int flexCutOffId, String transactionNumber,
                         double totalReimbursementAmount, Date dateSubmitted, String status) {
        this.flexReimbursementId = flexReimbursementId;
        this.employeeId = employeeId;
        this.flexCutOffId = flexCutOffId;
        this.transactionNumber = transactionNumber;
        this.totalReimbursementAmount = totalReimbursementAmount;
        this.dateSubmitted = dateSubmitted;
        this.status = status;
    }

    public Reimbursement(int flexReimbursementId,
                         int employeeId,
                         int flexCutOffId,
                         String transactionNumber,
                         double totalReimbursementAmount,
                         Date dateSubmitted,
                         String status,
                         int flexReimbursementDetailId,
                         int orNumber,
                         int categoryId,
                         String nameOfEstablishment,
                         String tinOfEstablishment,
                         double amount,
                         Date dateAdded
    ) {
        super(flexReimbursementDetailId, orNumber, categoryId, nameOfEstablishment, tinOfEstablishment,
                tinOfEstablishment, amount, dateAdded);
        this.flexReimbursementId = flexReimbursementId;
        this.employeeId = employeeId;
        this.flexCutOffId = flexCutOffId;
        this.transactionNumber = transactionNumber;
        this.totalReimbursementAmount = totalReimbursementAmount;
        this.dateSubmitted = dateSubmitted;
        this.status = status;
    }

    public int getFlexReimbursementId() {
        return flexReimbursementId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getFlexCutOffId() {
        return flexCutOffId;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public double getTotalReimbursementAmount() {
        return totalReimbursementAmount;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public String getStatus() {
        return status;
    }
}
