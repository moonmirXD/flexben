package com.pluralsight.cmdline.presentation;

import java.util.Scanner;

public class EmployeePresentation {
    private static final String[] filters = {"Flex Points Calculator", "File Reimbursement", "Generate Reimbursement Form", "Logout"};
    private String topic;

    public static String[] getTopicList() {
        return filters;
    }

    public String getTopic() {
        return filters[Integer.parseInt(this.topic)-1];
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    private static String addInput(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
