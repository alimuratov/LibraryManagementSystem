package main.authentication;
import java.util.ArrayList;

import main.exceptions.*;
import main.users.*;

public class AuthenticationService{
    private static Admin admin = Admin.getInstance();
    private static ArrayList<User> users; 
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

    public Admin getAdmin(Password password) throws ExIncorrectPassword{
        checkPassword(admin, password); 
        return admin;
    }

    private static boolean checkPassword(User user, String parole){
        Password password = new Password(parole);
        Password userPassword = user.getPassword();
        return password.equals(userPassword);
    }

    private static void checkPassword(User user, Password password) throws ExIncorrectPassword{
        Password userPassword = user.getPassword();
        if(!password.equals(userPassword))
        	throw new ExIncorrectPassword();
    }

    private static boolean nameAlreadyExists(String name){
        for (User customer: users) {
            String username = customer.getUserName();
            if(username.equals(name))
                return true;
        }

        return false;
    }

    public User login(String name, String parole) throws ExNonExistingUsesrname, ExIncorrectPassword{
        
        User user = findUserByName(name);

        if(user == null){
            throw new ExNonExistingUsesrname();
        }

        boolean correctPassword = checkPassword(user, parole);

        if(!correctPassword){
            throw new ExIncorrectPassword();
        }

        return user;
    }

    public Admin loginAdmin(String parole) throws ExIncorrectPassword{
        Password password = new Password(parole);
        return getAdmin(password);
    }

    public void logout(User user) throws ExUserDoesNotExist{
        sessionManager.removeSession(user);
    }
/*
    public User register(String name, String parole) throws ExInvalidPassword, ExTakenUsername{

        if(nameAlreadyExists(name))
            throw new ExTakenUsername();

        Password password = new Password(parole);
        User user = new Customer(name, password);
        
        try{
            Password.validPassword(password);
        } catch (ExInvalidPassword e){
            throw e;
        }

        User newUser = new User(name, password);
        users.add(newUser);
        return user;
    }
*/
    public Customer registerCustomer(String name, String parole) throws ExInvalidPassword, ExTakenUsername{
        if(nameAlreadyExists(name))
            throw new ExTakenUsername();

        Password password = new Password(parole);
        Customer customer = new Customer(name, password);

        try{
            Password.validPassword(password);
        } catch (ExInvalidPassword e){
            throw e;
        }

        users.add(customer);
        return customer;
    }

}
