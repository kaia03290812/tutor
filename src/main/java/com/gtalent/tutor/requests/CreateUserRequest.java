package com.gtalent.tutor.requests;

public class CreateUserRequest {
    public String username;
    public String email;

    public CreateUserRequest(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public CreateUserRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
