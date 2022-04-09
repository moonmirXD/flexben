package com.pluralsight.cmdline;

public class ErrorHandler {
    String message;
    int statusCode;

    public ErrorHandler(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        throwError(message, statusCode);
    }

    public static void throwError(String message, int statusCode){
        switch(statusCode) {
            case 404:
                System.out.println("No input found! " + message);
                break;
            case 400:
                System.out.println("Bad request! " + message);
                break;
            case 502:
                System.out.println("Error code is " + statusCode + ". \nError Message: " + message);
                break;
            default:
                System.out.println("The error code is not yet available!");
        }
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
