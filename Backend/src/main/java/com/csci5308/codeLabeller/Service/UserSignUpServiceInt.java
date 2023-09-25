package com.csci5308.codeLabeller.Service;

import com.csci5308.codeLabeller.Models.DTO.AuthResponse;
import com.csci5308.codeLabeller.Models.DTO.UserLoginDetails;
import com.csci5308.codeLabeller.Models.DTO.UserSignUpDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserSignUpServiceInt {
    AuthResponse registerUser(UserSignUpDetails user) throws RuntimeException;
    AuthResponse authenticate(UserLoginDetails userLoginDetails);
}
