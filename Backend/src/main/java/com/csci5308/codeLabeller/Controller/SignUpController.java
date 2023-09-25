package com.csci5308.codeLabeller.Controller;

import com.csci5308.codeLabeller.Models.DTO.AuthResponse;
import com.csci5308.codeLabeller.Models.DTO.UserSignUpDetails;
import com.csci5308.codeLabeller.Service.UserSignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@EnableMethodSecurity
@RestController
public class SignUpController {

    @Autowired
    UserSignUpService userSignUpService;
    /**
     * This url returns list of pending surveys
     * It expects annotator's username as payload for call
     * @param user This is object of UserSignUpDetails class
     * @return AuthResponse this returns jwtToken
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@RequestBody UserSignUpDetails user) throws RuntimeException{
        return ResponseEntity.ok(userSignUpService.registerUser(user));
    }

}
