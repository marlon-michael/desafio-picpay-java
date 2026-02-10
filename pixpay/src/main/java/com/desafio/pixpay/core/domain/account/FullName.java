package com.desafio.pixpay.core.domain.account;

public class FullName {
    private String fullname;

    public FullName setFullnameAndValidate(String fullname){
        String invalidRegex = ".*[\\\\;\"'<>/|\\-\\-\\*\\(\\)\\[\\]{}].*";
        boolean isValid = !fullname.matches(invalidRegex);
        if (!isValid){
            throw new IllegalArgumentException("The full name cannot contain this special character: \\\\ / | * ( ) [ ] { } ; ' \\\" < >.");
        }
        if (fullname.length() < 1 || fullname.length() > 40) {
            throw new IllegalArgumentException("The full name must be between 1 and 40 characters long.");
        }
        this.fullname = fullname.trim();
        return this;
    }

    public FullName fromPersistence(String fullname){
        this.fullname = fullname;
        return this;
    }

    public String getFullName(){
        return this.fullname;
    }

    public String getFirstName(){
        if (this.fullname.indexOf(" ") == -1) return this.fullname;
        return this.fullname.substring(0, this.fullname.indexOf(" "));
    }
}
