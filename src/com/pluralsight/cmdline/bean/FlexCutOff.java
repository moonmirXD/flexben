package com.pluralsight.cmdline.bean;

import java.util.Date;

public class FlexCutOff {
    int flexCutOffId, flexCycleId;
    Date startDate, endDate;
    char isActive;
    double cutOffCapAmount;
    String cutOffDescription;

    public FlexCutOff(int flexCutOffId, int flexCycleId, Date startDate, Date endDate, char isActive, double cutOffCapAmount, String cutOffDescription) {
        this.flexCutOffId = flexCutOffId;
        this.flexCycleId = flexCycleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.cutOffCapAmount = cutOffCapAmount;
        this.cutOffDescription = cutOffDescription;
    }

    public int getFlexCutOffId() {
        return flexCutOffId;
    }

    public int getFlexCycleId() {
        return flexCycleId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public char getIsActive() {
        return isActive;
    }

    public double getCutOffCapAmount() {
        return cutOffCapAmount;
    }

    public String getCutOffDescription() {
        return cutOffDescription;
    }
}
