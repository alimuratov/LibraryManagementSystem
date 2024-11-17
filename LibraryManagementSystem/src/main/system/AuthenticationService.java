package main.system;
import java.util.ArrayList;

import main.users.*;

public class AuthenticationService{
    private static ArrayList<User> users;
    private static SessionManagerStub session = SessionManagerStub.getInstance();

    public static AuthenticationService instance = new AuthenticationService();

    private  AuthenticationService(){
        AuthenticationService.users = new ArrayList<User>();
    }
    
    private static User findUserByName(String name){
    for (User user: users){
            String userName = user.getUserName();

            if(userName.equals(name))
                return user;
        }

        return null;
    }

    public static AuthenticationService getInstance(){
        return instance;
    }

    private static boolean checkPassword(User user, String parole){
        Password password = new Password(parole);
        Password userPassword = user.getPassword();
        return password.equals(userPassword);
    }

    private static boolean nameAlreadyExists(String name){
        for (User user: users) {
            String userName = user.getUserName();
            if(userName.equals(name))
                return true;
        }
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
        return session.removeSession(user);
    }

    public Customer register(String name, String parole){

        if(nameAlreadyExists(name)){
            System.out.println("The username already exists!");
            return null;
        }


        Password password = new Password(parole);
        Customer user = new Customer(name, password);

        Boolean goodPassword = Password.validPassword(password);

        if(goodPassword){
            Customer newUser = new Customer(name, password);
            users.add(newUser);
            return user;
        }

        return null;
    }

    public void addUser(User user){
        users.add(user);
    }
}