package main;

import users.*;
import authentication.*;
import book.*;
import transaction.*;
import exceptions.*;
import recommendation.*;

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


    public static void main(String args[]) {
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
//     private static void adminMenu(Scanner scanner) {
//         int userInput = -1;
//         while (userInput != 0) {
//             System.out.print("Press 1 to add a facility.\n");
//             System.out.print("Press 2 to change system time\n");
//             System.out.print("Press 0 to log out.\n");
//             System.out.print("Input: ");

//             String strUserInput = scanner.next(); // handle exception where input is not a number
//             userInput = Integer.parseInt(strUserInput);

//             switch (userInput) {
//                 case 1:
//                     addFacility(scanner);
//                     break;
//                 case 2:
//                     changeTime(scanner);
//                     break;
//                 case 0:
//                     // logout
//                     break;
//                 default:
//                     System.out.print("Invalid token. Try again.\n");
//             }
//         }
//     }
// // this facility is shit someone destroy it
//     private static void mainMenu(Scanner scanner) {
//         int userInput = -1;
//         LocalDateTime now = LocalDateTime.now();
//         // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
//         // HH:mm:ss");
//         // String dateNtime = now.format(formatter);
//         // System.out.print(dateNtime + "\n");
//         admin.printTime();
//         while (userInput != 0) {
//             try {
//                 System.out.print("Press 1 to make a booking.\n");
//                 System.out.print("Press 2 to view bookings.\n");
//                 System.out.print("Press 3 to cancel bookings.\n");
//                 System.out.print("Press 4 to check for any cancelled bookings.\n");
//                 System.out.print("Press 5 to review any past bookings.\n");
//                 System.out.print("Press 6 to check facility ratings.\n");
//                 System.out.print("Press 0 to log out.\n");
//                 System.out.print("Input: ");
//                 String strUserInput = scanner.next(); // handle exception where input is not a number

//                 userInput = Integer.parseInt(strUserInput);
//                 switch (userInput) {
//                     case 1:
//                         makeBooking(scanner);
//                         break;
//                     case 2:
//                         viewBooking(scanner);
//                         break;
//                     case 3:
//                         cancelBooking(scanner);
//                         break;
//                     case 4:
//                         customer.checkNotifications();
//                         break;
//                     case 5:
//                         customer.viewCompletedBookings(admin.getClock());
//                         System.out.print("Input booking ID to review a booking: ");
//                         String strID = scanner.next();
//                         int bookID = Integer.parseInt(strID);
//                         System.out.print("Enter comment: ");
//                         scanner.nextLine();
//                         String comment = scanner.nextLine();
//                         System.out.print("Enter rating (a number between 1-5): ");
//                         String strRate = scanner.next();
//                         int rate = Integer.parseInt(strRate);
//                         customer.addReview(bookID, comment, rate);
//                     case 6:
//                         for (int i = 0; i < facilities.size(); i++) {
//                             System.out.print(facilities.get(i) + ": \n");
//                             facilities.get(i).printReviews();
//                         }
//                     case 0:
//                         authInstance.logout(customer);
//                         break;
//                     default:
//                         throw new ExInvalidToken();
//                 }
//             } catch (NumberFormatException e) {
//                 System.out.print("Input is not a number. Try again.\nInput: ");
//             } catch (ExInvalidToken e) {
//                 System.out.print(e.getMessage());
//             } catch (ExWrongPayMethod e) {
//                 System.out.print(e.getMessage());
//             } catch (ExWrongDate e) {
//                 System.out.print(e.getMessage());
//             }
//         }
//     }

//     private static void makeBooking(Scanner scanner) throws ExInvalidToken, ExWrongPayMethod, ExWrongDate {
//         int facilityNo = -1;
//         boolean bookSuccessful = false;
//         while (facilityNo != 0 && !bookSuccessful) {
//             System.out.print("Choose a facility (Enter 0 to go to the main menu).\n");
//             for (int i = 0; i < facilities.size(); i++) {
//                 System.out.print(Integer.toString(i + 1) + ". " + facilities.get(i) + "\n");
//             }
//             System.out.print("Input: ");
//             String strFacility = scanner.next();
//             if (strFacility.equals("/"))
//                 break;
//             facilityNo = Integer.parseInt(strFacility);
//             switch (facilityNo) {
//                 case 1:
//                 case 2:
//                 case 3:
//                 case 4:
//                     System.out.print("\nEnter date of booking (DD-MM-YYYY): ");
//                     String date = scanner.next();
//                     if (date.equals("/"))
//                         break;
//                     SportFacility chosenFacility = facilities.get(facilityNo - 1);
//                     chosenFacility.showAvailableSlots(date);
//                     System.out.print("Enter preferred time slot (e.g. if you want to book at 20:00, type 20): ");
//                     String strTime = scanner.next();
//                     if (strTime.equals("/"))
//                         break;
//                     int time = Integer.parseInt(strTime);
//                     System.out.print("Select a payment method:\n");
//                     System.out.print("1. Credit Card (Enter CC)\n2. PayPal (Enter PL)\n");
//                     System.out.print("Input: ");
//                     String payment = scanner.next();
//                     if (payment.equals("/"))
//                         break;
//                     if (!payment.equals("CC") && !payment.equals("PL"))
//                         throw new ExWrongPayMethod();
//                     // String bookingDate, int startTime, String paymentString
//                     bookSuccessful = admin.receiveBookingRequest(customer, chosenFacility, date, time, payment);
//                     if (bookSuccessful) {
//                         System.out.print("Booking successfully created for " + date + " from " +
//                                 Integer.toString(time) + ":00 to " + Integer.toString(time + 1)
//                                 + ":00. Go to main menu to manage your bookings.\n");
//                         System.out.print(
//                                 "------------------------------------------------------------------------------------------------------------------------------------------\n");
//                     }
//                     break;
//                 default:
//                     throw new ExInvalidToken();
//             }

//         }
//     }

//     private static void viewBooking(Scanner scanner) throws ExInvalidToken {
//         customer.viewBookings();
//         System.out.print("Enter 0 to go to the main menu.\n");
//         int input = -1;
//         while (input != 0) {
//             String strInput = scanner.next();
//             input = Integer.parseInt(strInput);
//             if (input != 0) {
//                 throw new ExInvalidToken();
//             }
//         }
//     }

//     private static void cancelBooking(Scanner scanner) {
//         System.out.print("Enter Booking ID to cancel a booking.\n");
//         customer.viewBookings();
//         System.out.print("Input: ");
//         String bookID = scanner.next();
//         boolean cancelled = admin.receiveCancelRequest(customer, Integer.parseInt(bookID));
//         if (cancelled)
//             System.out.print("Booking cancelled successfully.\n");
//         else
//             System.out.print("Booking does not exist, try again.\n");
//     }

//     private static void changeTime(Scanner scanner) {
//         String date = "";
//         String hour = "";

//         System.out.print("Enter the date you want to change to (DD-MM-YYYY): ");
//         date = scanner.next();
//         System.out.print("Enter the hour you want to change to (If you want to change hour to 13:00, enter 13): ");
//         hour = scanner.next();

//         admin.changeTime(date, hour); // taswar/labiba please change this method so that this runs in loop until date
//                                       // and time are given in the correct format
//     }

//     private static void resetTime(Scanner scanner) {
//         admin.resetTime();
//         System.out.print("The system time has been changed to the present time.\n");
//     }

//     private static void addFacility(Scanner scanner) {
//         System.out.print("Choose a facility to add.\n");
//         System.out.print("1. Swimming\n2. Badminton\n3. Basketball\n4. Tennis\n5. Football\n");
//         System.out.print("Input: ");
//         String strFac = scanner.next();
//         int fac = Integer.parseInt(strFac);
//         String[] facilityStr = { "Swimming", "Badminton", "Basketball", "Tennis", "Football" };
//         facilities = admin.addFacility(facilityStr[fac - 1]);
//     }
}