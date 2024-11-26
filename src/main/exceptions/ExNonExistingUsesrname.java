package main.exceptions;

public class ExNonExistingUsesrname extends Exception{
    public ExNonExistingUsesrname(){
        super("Username not found!\n");
    }
}
