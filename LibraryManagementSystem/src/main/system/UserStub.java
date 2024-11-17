package main.system;

public class UserStub {
    public UserStub(){

    }

    public UserStub(String username, Password password){

    }

    public String getUserName(){
        return "Marko";
    }

    public Password getPassword(){
        return new Password("TheStrongestPassword");
    }
}
