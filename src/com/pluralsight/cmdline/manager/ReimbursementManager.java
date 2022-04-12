package com.pluralsight.cmdline.manager;

import com.pluralsight.cmdline.ErrorHandler;
import com.pluralsight.cmdline.bean.*;
import com.pluralsight.cmdline.dao.ReimbursementDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReimbursementManager {
    public ReimbursementDao reimbursementDao = new ReimbursementDao();
    private ArrayList<HashMap<String, String>> reimbursementsList = new ArrayList<>();

    public ReimbursementManager(){}

    public void addReimbursement(HashMap<String, String> reimbursement){
        Account user = Authentication.getUser();
        reimbursement.put("Flex Cutoff ID", String.valueOf(getDefaultCutOff().get().getFlexCutOffId()));
        reimbursement.put("Employee ID", String.valueOf(user.getEmployeeId()));
        reimbursement.put("Flex Cutoff Amount", String.valueOf(getDefaultCutOff().get().getCutOffCapAmount()));

        Double addedItemAndAmountTotal = (getTotalAmount() + Double.parseDouble(reimbursement.get("Amount")));
        Double totalCutOffAmount = getDefaultCutOff().get().getCutOffCapAmount();

        if(addedItemAndAmountTotal <= totalCutOffAmount) {
            reimbursementDao.createReimbursement(reimbursement);
        } else {
            ErrorHandler.throwError("The total cap amount is only " + totalCutOffAmount, 400);
        }

    }

    public List<Reimbursement> getReimbursements(){
        return reimbursementDao.getReimbursements();
    }

    public Optional<FlexCutOff> getDefaultCutOff(){
        return reimbursementDao.getCutOff();
    }

    public void deleteReimbursement(String id){
        int stringToNumber = Integer.parseInt(id);
        reimbursementDao.deleteReimbursement(stringToNumber);
    }

    public void updateReimbursement(String id){
        int toNumberId = Integer.parseInt(id);

        Optional<Reimbursement> reimbursement =
                reimbursementDao.getReimbursements().stream().filter(item -> item.getFlexReimbursementId() == toNumberId).collect(Collectors.toList()).stream().findFirst();

        String transactionNumber = String.format("%s %d %s %d",
        Authentication.getCompany().getCode(),
                reimbursement.get().getFlexCutOffId(),
                reimbursement.get().getDateAdded(),
                reimbursement.get().getFlexReimbursementId());

        reimbursementDao.updateReimbursement(toNumberId, transactionNumber);
    }

    public void getTotalReimbursements(){
        List<Reimbursement> list = reimbursementDao.getReimbursements();

        if(list != null) {
            list.clear();
            list = reimbursementDao.getReimbursements();
        }

        double totalReimbursement = 0;
        for (Reimbursement item : list) totalReimbursement += item.getAmount();

        System.out.println("Total amount: " + totalReimbursement);
    }

    public double getTotalAmount(){
        List<Reimbursement> list = reimbursementDao.getReimbursements();

        if(list != null) {
            list.clear();
            list = reimbursementDao.getReimbursements();
        }

        double totalCapAmount = 0;
        for (Reimbursement item : list) totalCapAmount += item.getAmount();

        return totalCapAmount;
    }

    public ArrayList<Category> getCategory(){
        return reimbursementDao.getCategory();
    }
}
