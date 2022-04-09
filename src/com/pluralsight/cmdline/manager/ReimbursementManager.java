package com.pluralsight.cmdline.manager;

import com.pluralsight.cmdline.bean.*;
import com.pluralsight.cmdline.dao.ReimbursementDao;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ReimbursementManager {
    public ReimbursementDao reimbursementDao = new ReimbursementDao();
    private ArrayList<HashMap<String, String>> reimbursementsList = new ArrayList<>();

    public ReimbursementManager(){}

    public void addReimbursement(HashMap<String, String> reimbursement){;
        Account user = Authentication.getUser();
        reimbursement.put("Flex Cutoff ID", String.valueOf(getDefaultCutOff().get().getFlexCutOffId()));
        reimbursement.put("Employee ID", String.valueOf(user.getEmployeeId()));
        reimbursement.put("Flex Cutoff Amount", String.valueOf(getDefaultCutOff().get().getCutOffCapAmount()));
        reimbursementDao.createReimbursement(reimbursement);
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
        int stringToNumber = Integer.parseInt(id);
        reimbursementDao.updateReimbursement(stringToNumber);
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

    public ArrayList<Category> getCategory(){
        return reimbursementDao.getCategory();
    }
}
