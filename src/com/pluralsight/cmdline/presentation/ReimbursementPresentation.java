package com.pluralsight.cmdline.presentation;

import com.pluralsight.cmdline.ErrorHandler;
import com.pluralsight.cmdline.bean.Account;
import com.pluralsight.cmdline.bean.Authentication;
import com.pluralsight.cmdline.bean.Category;
import com.pluralsight.cmdline.bean.Reimbursement;
import com.pluralsight.cmdline.manager.ReimbursementManager;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReimbursementPresentation {
    private ReimbursementManager reimbursementManager = new ReimbursementManager();

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Category> reimbursementRemoveList = new ArrayList<>();
    private static HashMap<String, String> reimbursement = new HashMap<String, String>();

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date date = new Date();

    private static final String[] fields = {"Category ID", "Date", "OR Number", "Name of Establishment", "TIN of " +
            "Establishment", "Amount"};
    private static final String[] topics = {"Add Reimbursement Item", "Remove Reimbursement Item", "Submit Reimbursement", "Generate Form", "Quit"};

    {

    }

    public void selectTopics(){
        boolean isQuit = true;
        while(isQuit) {
            showTable(this.topics);
            String selectedTopic = addInput();

            switch(topics[Integer.parseInt(selectedTopic) -1]) {
                case "Add Reimbursement Item":
                    addReimbursementItem();
                    reimbursementManager.getTotalReimbursements();
                    break;
                case "Remove Reimbursement Item":
                    removeReimbursementItem();
                    reimbursementManager.getTotalReimbursements();
                    break;
                case "Submit Reimbursement":
                    submitReimbursementItem();
                    break;
                case "Generate Form":
                    generateForm();
                    break;
                case "Quit":
                    isQuit = false;
                    break;
            }
        }
    }

    public void generateForm(){
        Account user = Authentication.getUser();
        List<Reimbursement> list = reimbursementManager.getReimbursements();

        Optional<Reimbursement> getFirstReimbursement =
                reimbursementManager.getReimbursements().stream().filter(reimbursement -> reimbursement.getEmployeeId() == user.getEmployeeId()).findFirst();

        double totalAmount = reimbursementManager.getTotalAmount();

        try {
            PrintWriter out =
                    new PrintWriter(user.getLastName()+ "_" + user.getFirstName() + "_" + Authentication.getCompany().getCode() +".txt");

            out.println("Employee Number: " + user.getEmployeeNumber());
            out.println("Employee Name: " +  user.getLastName() + ", " + user.getFirstName());
            out.println("Date Submitted: " + getFirstReimbursement.get().getDateSubmitted());
            out.println("Transaction Number: " + getFirstReimbursement.get().getTransactionNumber());
            out.println("Amount: Php " + totalAmount);
            out.println("Status: " + getFirstReimbursement.get().getStatus() + "\n");

            out.println("=== DETAILS ===");

            if(categories.isEmpty()){
                categories = reimbursementManager.getCategory();
            }

            if(list != null) {
                list.clear();
                list = reimbursementManager.getReimbursements();
            }

            List<Reimbursement> finalList = list;
            categories.forEach(category -> {
                out.println("CATEGORY: " + category.getName() + "\n");
                    for (Reimbursement act: finalList){
                        if(category.getCategoryId() == act.getCategoryId()) {
                            out.println("Item # " + act.getFlexReimbursementDetailId());
                            out.println("Date: " + act.getDateAdded());
                            out.println("OR Number: " + act.getOrNumber());
                            out.println("Name of Establishment: " + act.getNameOfEstablishment());
                            out.println("TIN of Establishment: " + act.getTinOfEstablishment());
                            out.println("Amount: Php " + act.getAmount());
                            out.println("Status: " + act.getStatus());
                            out.println("\n");
                        }
                    }
            });
            showMessage("Form is generated!" + " " + user.getLastName()+ "_" + user.getFirstName() + "_" + Authentication.getCompany().getCode() +".txt");
            out.close();
        }catch(Exception e) {
            ErrorHandler.throwError("There's a problem in generating text file!", 500);
        }
    }

    private void submitReimbursementItem(){
        List<Reimbursement> list = reimbursementManager.getReimbursements().stream().filter(item -> item.getStatus().equalsIgnoreCase(
                "draft")).collect(Collectors.toList());

        if(list.isEmpty()) {
            ErrorHandler.throwError("Please file reimbursement first!", 400);
        } else {
            showMessage("Are you sure you want to submit [y/n]");
            String confirmation = addInput();

            if(confirmation.equalsIgnoreCase("y")) {
                reimbursementManager.updateReimbursement();
            } else {
                showMessage("Try again!");
            }
        }
    }


    private void removeReimbursementItem(){
        List<Reimbursement> list = reimbursementManager.getReimbursements().stream().filter(item -> item.getStatus().equalsIgnoreCase(
                "draft")).collect(Collectors.toList());

        if(list.isEmpty()) {
            ErrorHandler.throwError("Please file reimbursement first!", 400);
        }
        else {
            displayReimbursementItems();
            showMessage("To exit press any number but not the item number.");
            showMessage("Please enter the item #:");
            String flexReimbursementDetailId = addInput();
            showMessage("Are you sure you want to remove #" + flexReimbursementDetailId + " item? [y/n]");
            String confirmation = addInput();

            if(confirmation.equalsIgnoreCase("y")) {
                reimbursementManager.deleteReimbursement(flexReimbursementDetailId);
            } else {
                showMessage("Try again!");
            }
        }
    }

    private void displayReimbursementItems(){
        List<Reimbursement> list = reimbursementManager.getReimbursements();

        if(list != null) {
            list.clear();
            list = reimbursementManager.getReimbursements().stream().filter(item -> item.getStatus().equalsIgnoreCase(
                    "draft")).collect(Collectors.toList());
        }

        list.forEach(item -> {
            System.out.println(
                    "Item #" + item.getFlexReimbursementDetailId()
                            + "\nDate Submitted: " +item.getDateAdded()
                            + "\nOR Number: " + item.getOrNumber()
                            + "\nName of Establishment: " + item.getNameOfEstablishment()
                            + "\nTin of Establishment: " + item.getTinOfEstablishment()
                            + "\nReimbursement Amount: " + item.getAmount()
                            + "\nTotal Reimbursement Amount: " + item.getTotalReimbursementAmount()
                            + "\nStatus: " + item.getStatus() + "\n"
            );
        });
    }

    private void addReimbursementItem() {
        try {
            System.out.println("Please enter the following details.");

            for (String f: fields) {
                showMessage(f + ":");

                if(f.equals("Date")) {
                    showMessage("Date format must be yyyy-mm-dd");
                }

                if(f.equals("Category ID")){
                    displayCategories();
                }

                String input = addInput();

                if(f.equals("Date")) {
                    input = validateDate(input);
                }

                if(f.equals("Amount") && Double.parseDouble(input) < 500) {
                    while(Double.parseDouble(input) < 500) {
                        showMessage("Amount must be 500 or higher. \nAmount:");
                        input = addInput();
                    }
                }
                reimbursement.put(f, input);
            }

            reimbursementManager.addReimbursement(this.reimbursement);
        }
        catch(Exception e) {
            ErrorHandler.throwError("*addReimbursementItem* There's wrong in the input", 400);
        }
    }

    private String validateDate(String input){
        while(input.compareTo(dateFormat.format(date)) > 0) {
            showMessage("The date is invalid. Please enter again the specified date in following format yyyy-mm-dd: ");
            input = addInput();
        }
        return input;
    }

    private void displayCategories(){
        if(categories.isEmpty()){
            categories = reimbursementManager.getCategory();
        }
        categories.forEach((category) -> showMessage(String.format("[%d]: Code - %s | %s", category.getCategoryId(), category.getCode(), category.getName())));
        showMessage("Please choose category number.");
    }

    public List<Reimbursement> getReimbursements() {
        List<Reimbursement> list = reimbursementManager.getReimbursements();
        return list;
    }


    public static HashMap<String, String> getReimbursementItemInputs() {
        return reimbursement;
    }

    private static void showMessage(String message){
        System.out.println(message);
    }

    private static void showTable(String[] topics){
        int index = 1;
        for (String topic: topics){
            showMessage(String.format("[%d] %s", index, topic)); index++;
        }
    }

    private static String addInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
