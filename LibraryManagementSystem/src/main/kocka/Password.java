package main.kocka;

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
        for(char c: stringCopy)
            if('a' <= c && c <= 'z')
                return true;
        
        return false;
    }

    private static boolean hasUpper(String string){
        char[] stringCopy = string.toCharArray();
        for(char c: stringCopy)
            if('A' <= c && c <= 'A')
                return true;
        
        return false;
    }

    private static boolean hasNumber(String string){
        char[] stringCopy = string.toCharArray();
        for(char c: stringCopy)
            if('0' <= c && c <= '9')
                return true;
        
        return false;
    }

    public static boolean validPassword(Password password){
        String parole = password.parole;

        if(length(password) < 6)
            return false;

        boolean containsNumber = hasNumber(parole), containsUpper = hasUpper(parole), containsLower = hasLower(parole);

        return containsNumber && containsUpper && containsLower;
    }

    public boolean equals(Password password){
        return this.parole.equals(password.parole);
    }
}
