package main.exceptions;

public class ExIncorrectPassword extends Exception{
    public ExIncorrectPassword(){
        super("Incorrect password!\n");
    }
}
