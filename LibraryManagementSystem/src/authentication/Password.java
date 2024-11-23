package authentication;

import exceptions.ExInvalidPassword;

public class Password { //this class is added because in the future we might implement something more
                        //sophisticated like activity log of user's passwords or similar
    private String parole; //parole is just String version of Password
    public Password(String parole){
        this.parole = parole;
    }
    
    public static int length(Password password){
        String parole = password.parole;
        return parole.length();
    }

    private static boolean hasLower(String string){
    	char[] stringCopy = string.toCharArray();
        int n = string.length();
        for(int i = 0; i < n; i++)
            if('a' <= stringCopy[i] && stringCopy[i] <= 'z')
                return true;
        
        return false;
    }

    private static boolean hasUpper(String string){
        char[] stringCopy = string.toCharArray();
        int n = string.length();
        for(int i = 0; i < n; i++)
            if('A' <= stringCopy[i] && stringCopy[i] <= 'Z')
                return true;
        
        return false;
    }

    private static boolean hasNumber(String string){
    	char[] stringCopy = string.toCharArray();
        int n = string.length();
        for(int i = 0; i < n; i++)
            if('0' <= stringCopy[i] && stringCopy[i] <= '9')
                return true;
        
        return false;
    }

    public static void validPassword(Password password) throws ExInvalidPassword{
        String parole = password.parole;

        if(length(password) < 6)
            throw new ExInvalidPassword();

        boolean containsNumber = hasNumber(parole), containsUpper = hasUpper(parole), containsLower = hasLower(parole);

        if(!(containsNumber && containsUpper && containsLower))
            throw new ExInvalidPassword(containsUpper, containsLower, containsNumber);
    }

    public boolean equals(Password password){
        return this.parole.equals(password.parole);
    }
}
