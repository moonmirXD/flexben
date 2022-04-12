package com.pluralsight.cmdline;

import com.pluralsight.cmdline.bean.Authentication;
import com.pluralsight.cmdline.bean.Reimbursement;
import com.pluralsight.cmdline.manager.ReimbursementManager;
import com.pluralsight.cmdline.presentation.AdminPresentation;
import com.pluralsight.cmdline.presentation.EmployeePresentation;
import com.pluralsight.cmdline.presentation.ReimbursementPresentation;

import java.util.List;
import java.util.Scanner;

enum Level {
    EMPLOYEE(1),
    ADMIN(2);

    private int role;

    Level(int role){
        this.role = role;
    }

    public int getRole(){
        return role;
    }
}

public class Main {
    private static Authentication authentication = new Authentication();
    private static EmployeePresentation employeePresentation = new EmployeePresentation();
    private static AdminPresentation adminPresentation = new AdminPresentation();
    private static ReimbursementPresentation reimbursementPresentation = new ReimbursementPresentation();

    public static void main(String[] args) {
        showTable(Level.values());
        String role = addInput();

        if(Integer.parseInt(role) > 2)  {
            ErrorHandler.throwError("", 404);
        }

        selectRole(Integer.parseInt(role));
    }

    private static void selectRole(int roleNumber){
        showMessage("Please enter your email address:");
        String email = addInput();
        showMessage("Please enter your password:");
        String password = addInput();
        Authentication isAuthenticate = new Authentication(email, password);

        if(!isAuthenticate.authenticate()) {
            ErrorHandler.throwError("Employee email or password is incorrect!", 400);
            restartApplication();
        }

        showMessage("What do you want to do?");

        final int Employee = 1;
        final int Admin = 2;
        switch(roleNumber) {
            case Employee:
                showMessage(String.valueOf(Level.EMPLOYEE));
                proceedToEmployee();
                break;
            case Admin:
                showMessage(String.valueOf(Level.ADMIN));
                proceedToAdmin();
                break;
        }
    }

    private static void proceedToEmployee() {
        try {
            while(true){
                showTopics(employeePresentation.getTopicList());
                String topic = addInput();
                employeePresentation.setTopic(topic);
                selectedTopic(employeePresentation.getTopic());
            }

        }
        catch(Exception e) {
            ErrorHandler.throwError("*proceedToEmployee function* You must enter a valid input!", 400);
        }
    }

    private static void proceedToAdmin(){
        try {
            while(true){
                showTopics(adminPresentation.getTopicList());
                String topic = addInput();
                adminPresentation.setTopic(topic);
                adminPresentation.selectedTopic(adminPresentation.getTopic());
            }
        }
        catch(Exception e) {
            ErrorHandler.throwError("*proceedToAdmin function* You must enter a valid input!", 400);
        }
    }

    private static void selectedTopic(String topic){
            switch(topic) {
                case "Flex Points Calculator":
                    System.out.println("Go to calculator");
                    flexCalculator();
                    break;
                case "File Reimbursement":
                    System.out.println("Go to File Reimbursement");
                    reimbursementPresentation.selectTopics();
                    break;
                case "Generate Reimbursement Form":
                    System.out.println("Go to Generate Reimbursement Form");
                    reimbursementPresentation.generateForm();
                    break;
                case "Logout":
                    System.out.println("Logout user");
                    restartApplication();
                    break;
                default:
                    ErrorHandler.throwError("Please choose from 1 to 4!", 400);
            }
    }

    private static double getTotalReimbursements(){
        List<Reimbursement> list = reimbursementPresentation.getReimbursements();

        if(list != null) {
            list.clear();
            list = reimbursementPresentation.getReimbursements();
        }

        double totalReimbursement = 0;
        for (Reimbursement item : list) totalReimbursement += item.getAmount();

        return totalReimbursement;
    }

    private static void flexCalculator(){
        showMessage("Please enter your flex credits: ");
        String numberOfFlexCredits = addInput();

        showMessage("Please enter your monthly rate: ");
        String monthlyRate = addInput();

        double flexPoints = (Double.parseDouble(monthlyRate)/21.75) * Double.parseDouble(numberOfFlexCredits);
        System.out.printf("Flex points: %.2f", flexPoints);
    }

    private static String addInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static void showTable(Object[] data){
        int index = 0;
        for(Object d: data) {
            showMessage(String.format("[%d] %s", index+1, d)); index++;
        }
    }

    private static void showTopics(String[] list){
        int index = 1;
        for (String li:list) {
            showMessage(String.format("[%d] %s", index, li)); index++;
        }
    }

    private static void showMessage(String message){
        System.out.println(message);
    }

    private static void restartApplication(){
        String[] args = new String[0];
        main(args);
    }
}
