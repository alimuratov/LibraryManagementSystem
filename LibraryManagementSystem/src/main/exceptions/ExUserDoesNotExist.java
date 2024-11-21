package main.exceptions;

public class ExUserDoesNotExist extends Exception {
    public ExUserDoesNotExist(){
        super("The user does not exist\n");
    }
}
