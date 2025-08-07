package com.gtalent.tutor.responses;

public class CreateUserResponse {
    private String usernme;


    public CreateUserResponse() {
    }

    public CreateUserResponse(String usernme) {

        this.usernme = usernme;

    }

    public String getUsernme() {
        return usernme;
    }

    public void setUsernme(String usernme) {
        this.usernme = usernme;
    }


}