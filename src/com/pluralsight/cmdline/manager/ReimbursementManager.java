package com.pluralsight.cmdline.manager;

import com.pluralsight.cmdline.ErrorHandler;
import com.pluralsight.cmdline.bean.*;
import com.pluralsight.cmdline.dao.ReimbursementDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReimbursementManager {
    public ReimbursementDao reimbursementDao = new ReimbursementDao();
    private ArrayList<HashMap<String, String>> reimbursementsList = new ArrayList<>();
    private ArrayList<Reimbursement> list = (ArrayList<Reimbursement>) reimbursementDao.getReimbursements();
    public ReimbursementManager(){}

    public void addReimbursement(HashMap<String, String> reimbursement){
        Account user = Authentication.getUser();

        Double addedItemAndAmountTotal = (getTotalAmount() + Double.parseDouble(reimbursement.get("Amount")));
        Double totalCutOffAmount = getDefaultCutOff().get().getCutOffCapAmount();

        reimbursement.put("Flex Cutoff ID", String.valueOf(getDefaultCutOff().get().getFlexCutOffId()));
        reimbursement.put("Employee ID", String.valueOf(user.getEmployeeId()));
        reimbursement.put("Flex Cutoff Amount", String.valueOf(addedItemAndAmountTotal));

        if(addedItemAndAmountTotal <= totalCutOffAmount) {
            reimbursementDao.createReimbursement(reimbursement);
            reimbursementDao.updateTotalReimbursementAmount(getTotalAmount());
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
        reimbursementDao.updateTotalReimbursementAmount(getTotalAmount());
    }

    public void updateReimbursement(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();

        int employeeId = Authentication.getUser().getEmployeeId();

        Optional<Reimbursement> reimbursement =
                reimbursementDao.getReimbursements().stream().filter(item -> item.getEmployeeId() == employeeId && item.getStatus().equalsIgnoreCase("draft"))
                        .collect(Collectors.toList()).stream().findFirst();

        String transactionNumber = String.format("%s-%d-%s-%d",
        Authentication.getCompany().getCode(),
                reimbursement.get().getFlexCutOffId(),
                dateFormat.format(date),
                reimbursement.get().getFlexReimbursementId());

        reimbursementDao.updateReimbursement(employeeId, transactionNumber);
    }

    public void getTotalReimbursements(){
        List<Reimbursement> list = reimbursementDao.getReimbursements();

        if(list != null) {
            list.clear();
            list = reimbursementDao.getReimbursements().stream().filter(item -> item.getStatus().equalsIgnoreCase("draft")).collect(Collectors.toList());
        }

        double totalReimbursement = 0;
        for (Reimbursement item : list) totalReimbursement += item.getAmount();

        System.out.println("Total amount: " + totalReimbursement);
    }

    public double getTotalAmount(){
        List<Reimbursement> list = reimbursementDao.getReimbursements();

        if(list != null) {
            list.clear();
            list = reimbursementDao.getReimbursements().stream().filter(item -> item.getStatus().equalsIgnoreCase("draft")).collect(Collectors.toList());
        }

        double totalReimbursementAmount = 0;
        for (Reimbursement item : this.list) totalReimbursementAmount += item.getAmount();

        return totalReimbursementAmount;
    }

    public ArrayList<Category> getCategory(){
        return reimbursementDao.getCategory();
    }
}
