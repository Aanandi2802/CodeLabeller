package com.csci5308.codeLabeller.Service;

import com.csci5308.codeLabeller.Enums.UserAuthority;
import com.csci5308.codeLabeller.Models.Annotator;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import com.csci5308.codeLabeller.Models.DTO.AuthResponse;
import com.csci5308.codeLabeller.Models.DTO.UserLoginDetails;
import com.csci5308.codeLabeller.Models.DTO.UserSignUpDetails;
import com.csci5308.codeLabeller.Repsoitory.AnnotationsRepository;
import com.csci5308.codeLabeller.Repsoitory.AnnotatorRepository;
import com.csci5308.codeLabeller.Repsoitory.UserSignUpRepository;
import com.csci5308.codeLabeller.Security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * this service helps:
 * register the user
 * authenticate the user.
 */
@Service
public class UserSignUpService implements UserSignUpServiceInt {

    @Autowired
    UserSignUpRepository userSignUpRepository;
    @Autowired
    AnnotatorRepository annotatorRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtService jwtService;

    /**
     * this register the user in dbg.
     * @param user: user information.
     * @return: this response contians jwt token.
     */
    public AuthResponse registerUser(UserSignUpDetails user) throws RuntimeException{
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority());
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(grantedAuthority);
        UserDetails userDetails = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), authorities);
        try {
            userSignUpRepository.registerTheUser(userDetails);
        } catch(DuplicateKeyException e){
            throw new DuplicateKeyException("User already exists",e);
        }
        if(user.getAuthority().equals(UserAuthority.ANNOTATOR.toString())){
            Annotator annotator = new Annotator();
            annotator.setUsername(user.getUsername());
            annotatorRepository.save(annotator);
        }
        String jwtToken = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        return authResponse;
    }

    /**
     * this helps in authneticating the user.
     * @param userLoginDetails: DTO for user login details.
     * @return: this response contians jwt token.
     */
    public AuthResponse authenticate(UserLoginDetails userLoginDetails){
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDetails.getUsername(),
                        userLoginDetails.getPassword()
                )
        );
        UserDetails user = userSignUpRepository.findByUsername(userLoginDetails.getUsername());
        String jwtToken = jwtService.generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        return authResponse;
    }
}
