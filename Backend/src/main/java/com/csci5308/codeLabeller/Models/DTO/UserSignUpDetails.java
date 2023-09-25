package com.csci5308.codeLabeller.Models.DTO;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class UserSignUpDetails {
    private String password;
    private String username;
    private String authority;

    public UserSignUpDetails() {
    }

    public UserSignUpDetails(String password, String username, String authority) {
        this.password = password;
        this.username = username;
        this.authority = authority;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "UserSignUpDetails{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", authority=" + authority +
                '}';
    }
}
