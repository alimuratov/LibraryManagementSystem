package main.exceptions;

public class ExTakenUsername extends Exception{
    public ExTakenUsername(){
        super("The username is already taken\n");
    }
}
