package main.exceptions;

import java.util.ArrayList;

public class ExInvalidPassword extends Exception{
    private static String constructString(boolean hasUpper, boolean hasLower, boolean hasNumber){
        ArrayList<String> needed = new ArrayList<String>();
        
        if(!hasNumber)
            needed.add("at least one digit");
        if(!hasUpper)
            needed.add("at least one uppercase letter");
        if(!hasLower)
            needed.add("at least one lowercase letter");
        
        int n = needed.size();
        
        String result = "";

        if(n == 1)
            result = result + needed.get(0);
        else if(n == 2) 
            result = result + needed.get(0) + " and " + needed.get(1);
        else
            result = result + needed.get(0) + ", " + needed.get(1) + ", and " + needed.get(2);
        
        result = result + "\n";
        return result;
    }

    public ExInvalidPassword(){
        super("Password must contain at least 6 characters\n");
    }

    public ExInvalidPassword(boolean hasUpper, boolean hasLower, boolean hasNumber){
        super("Password must contain " + constructString(hasUpper, hasLower, hasNumber));
    }
}