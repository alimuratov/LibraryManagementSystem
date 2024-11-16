import java.util.ArrayList;
import java.util.List;

public class AuthenticationService{
    private static List<UserStub> users;
    private static SessionManagerStub session = SessionManagerStub.getInstance();

    public AuthenticationService(){
        AuthenticationService.users = new ArrayList<UserStub>();
    }

    private static UserStub findUserByName(String name){
        for (UserStub user: users){
            String userName = user.getUserName();

            if(userName.equals(name))
                return user;
        }

        return null;
    }
/*
    private static UserStub findUser(String name, String parole){
        Password password = new Password(parole);
        for (UserStub user : users) {

            Password userPassword = user.getPassword();
            String userUserName = user.getUserName();

            if(userUserName.equals(name) && userPassword.equals(password)){
                return user;
            }
        }
        return null;
    }
*/
    private static boolean checkPassword(UserStub user, String parole){
        Password password = new Password(parole);
        Password userPassword = user.getPassword();
        return password.equals(userPassword);
    }

    public static UserStub login(String name, String parole){
        
        UserStub user = findUserByName(name);

        if(user == null)
            System.out.println("No user found!");

        boolean correctPassword = checkPassword(user, parole);

        if(!correctPassword){
            System.out.println("Password is not correct!");
            return null;
        }

        return user;
    }

    public static Boolean logout(UserStub user){
        return session.removeSession(user);
    }

    public UserStub register(String name, String parole){

        UserStub user = findUserByName(name);

        if(user != null){
            System.out.println("The username already exists!");
            return null;
        }
        Password password = new Password(parole);
        Boolean goodPassword = Password.validPassword(password);

        if(goodPassword){
            UserStub newUser = new UserStub(name, password);
            users.add(newUser);
            return user;
        }

        return null;
    }
}