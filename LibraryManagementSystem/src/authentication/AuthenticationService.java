package authentication;
import java.util.ArrayList;

import exceptions.*;
import users.*;

public class AuthenticationService{
    private static Admin admin;
    private static ArrayList<Customer> users; // librarians and regular customers have the same login type
    private static SessionManager sessionManager = SessionManager.getInstance();

    public static AuthenticationService instance = new AuthenticationService();

    private  AuthenticationService(){
        AuthenticationService.users = new ArrayList<Customer>();
    }

    public static AuthenticationService getInstance(){
        return instance;
    }

    private static Customer findUserByName(String name){
        for (Customer customer: users){
                String username = customer.getUserName();
                if(username.equals(name))
                    return customer;
            }
    
            return null;
    }

    public void addUser(Customer customer){
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

        return false;
    }

    public Customer login(String name, String parole) throws ExNonExistingUsesrname, ExIncorrectPassword{
        
        Customer user = findUserByName(name);

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