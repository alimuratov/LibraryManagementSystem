package main;

import main.users.*;
import main.authentication.*;
import main.book.*;
import main.transaction.*;
import main.exceptions.*;
import main.recommendation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static boolean isRunning = true;
    private static String username;
    private static String parole;
    private static Password password = null;
    private static Customer customer = null;
    private static AuthenticationService authenticationService = AuthenticationService.getInstance();
    private static Admin admin = Admin.getInstance();


    public static void main(String[] args) {
        // AS.registerAdmin(cred, cred);
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            int response = openScreen(scanner);
            switch(response){
                case 1:
                case 2:
                    mainMenu(scanner);
                    break;
                case 3:
                    adminMenu(scanner);
                    break;
                default:
                    System.out.println("Thank you for using our service! Have a good day!");
                    break;
            }
        }
        scanner.close(); // Close the scanner only once here
    }

    private static int openScreen(Scanner scanner) {
        customer = null;
        username = "";
        parole = "";
        System.out.println("Welcome to Library+");
        System.out.println("###############################################");
        System.out.println("Enter 1 to Register | Enter 2 to Log In | Enter 0 to Close");
        System.out.print("Input: ");
        int response = -1;
        while (response < 0 || response > 3) {
            try {
                String strResponse = scanner.next();
                response = Integer.parseInt(strResponse);
                if (response == 1) {
                    boolean done = false;
                    while(!done){
                        try {

                            System.out.print("Define your username: ");
                            username = scanner.next();
                            System.out.print("Define your password: ");
                            parole = scanner.next();
                            customer = authenticationService.registerCustomer(username, parole);
                            done = true;
                        } catch (ExInvalidPassword e){
                            System.out.print(e.getMessage());
                            System.out.println("Try again");
                        } catch (ExTakenUsername e) {
                            System.out.print(e.getMessage());
                            System.out.println("Try again");
                        }
                    }
                } else if (response == 2) {
                    boolean done = false;
                    while (!done) {
                        try {
                            System.out.print("Enter your username: ");
                            username = scanner.next();
                            System.out.print("Enter your password: ");
                            parole = scanner.next();
                            
                            if(username.equals("admin")) {
                                authenticationService.loginAdmin(parole);
                                response = 3;
                            }
                            else
                                authenticationService.login(username, parole);

                            done = true;
                        } catch (Exception e) {
                            System.out.print(e.getMessage());
                            System.out.println("Try again");
                        }
                    }
                } else if (response == 0) 
                    isRunning = false;
                else {
                    System.out.println("GLUP SI KO KURAC, TO UOPSTE NIJE TOKEN KOJI TREBA DA KORISTIS");
                }
            } /*catch (ExInvalidToken e) {
                System.out.print(e.getMessage());
            }*/ catch (NumberFormatException e) {
                System.out.print("Input is not a number. Try again.\nInput: ");
            }
        }
        return response;
    }


    private static void adminMenu(Scanner scanner) {
        System.out.println("Admin Menu");
        return;
    }

    private static void mainMenu(Scanner scanner) {
        System.out.println("Main Menu");
    }
}