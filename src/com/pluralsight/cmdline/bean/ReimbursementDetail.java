package com.pluralsight.cmdline.bean;

import java.util.Date;

public class ReimbursementDetail {
    int flexReimbursementDetailId, flexReimbursementId, orNumber, categoryId;
    String nameOfEstablishment, tinOfEstablishment, status;
    double amount;
    Date dateAdded;

    public ReimbursementDetail(){}

    public ReimbursementDetail(int flexReimbursementDetailId,
                               int orNumber,
                               int categoryId,
                               String nameOfEstablishment,
                               String tinOfEstablishment,
                               String status,
                               double amount,
                               Date dateAdded
    ) {
        this.flexReimbursementDetailId = flexReimbursementDetailId;
        this.orNumber = orNumber;
        this.categoryId = categoryId;
        this.nameOfEstablishment = nameOfEstablishment;
        this.tinOfEstablishment = tinOfEstablishment;
        this.status = status;
        this.amount = amount;
        this.dateAdded = dateAdded;
    }

    public int getFlexReimbursementDetailId() {
        return flexReimbursementDetailId;
    }

    public int getFlexReimbursementId() {
        return flexReimbursementId;
    }

    public int getOrNumber() {
        return orNumber;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getNameOfEstablishment() {
        return nameOfEstablishment;
    }

    public String getTinOfEstablishment() {
        return tinOfEstablishment;
    }

    public String getStatus() {
        return status;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDateAdded() {
        return dateAdded;
    }
}
