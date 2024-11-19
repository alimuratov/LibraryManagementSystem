package main.kocka;
import java.util.ArrayList;

import main.users.Customer;
import main.users.*;

public class AuthenticationService{
    private static Admin admin;
    private static ArrayList<User> users; // librarians and regular customers have the same login type
    private static SessionManager sessionManager = SessionManager.getInstance();

    public static AuthenticationService instance = new AuthenticationService();

    private  AuthenticationService(){
        AuthenticationService.users = new ArrayList<User>();
    }

    public static AuthenticationService getInstance(){
        return instance;
    }

    private static User findUserByName(String name){
        for (User customer: users){
                String username = customer.getUserName();
                if(username.equals(name))
                    return customer;
            }
    
            return null;
        }

    public void addUser(User customer){
        users.add(customer);
    }

    public Admin getAdmin(Password password){
        return checkPassword(admin, password) ? admin : null;
    }

    private static boolean checkPassword(User user, String parole){
        Password password = new Password(parole);
        Password userPassword = user.getPassword();
        return password.equals(userPassword);
    }

    private static boolean checkPassword(User user, Password password){
        Password userPassword = user.getPassword();
        return password.equals(userPassword);
    }

    private static boolean nameAlreadyExists(String name){
        for (User customer: users) {
            String username = customer.getUserName();
            if(username.equals(name))
                return true;
        }

        String adminName = admin.getUserName();
        if(adminName.equals(name))
            return true;

        return false;
    }

    public User login(String name, String parole){
        
        User user = findUserByName(name);

        if(user == null){
            System.out.println("No user found!");
            return null;
        }

        boolean correctPassword = checkPassword(user, parole);

        if(!correctPassword){
            System.out.println("Password is not correct!");
            return null;
        }

        return user;
    }

    public Boolean logout(User user){
        return sessionManager.removeSession(user);
    }

    public User register(String name, String parole){

        if(nameAlreadyExists(name)){
            System.out.println("The username already exists!");
            return null;
        }


        Password password = new Password(parole);
        User user = new Customer(name, password);

        Boolean goodPassword = Password.validPassword(password);

        if(goodPassword){
            User newUser = new User(name, password);
            users.add(newUser);
            return user;
        }

        return null;
    }

    public Customer registerCustomer(String name, String parole){
        if(nameAlreadyExists(name)){
            System.out.println("The username already exists!");
            return null;
        }

        Password password = new Password(parole);
        Customer customer = new Customer(name, password);

        Boolean goodPassword = Password.validPassword(password);

        if(goodPassword){
            users.add(customer);
            return customer;
        }

        return null;

    }

}