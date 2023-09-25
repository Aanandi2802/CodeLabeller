package com.csci5308.codeLabeller.Models.DTO;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class AuthResponse {

    private String jwtToken;
}
