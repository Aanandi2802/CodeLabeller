package com.csci5308.codeLabeller.Models.DTO;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserLoginDetails {
    private String password;
    private String username;
}
