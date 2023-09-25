package com.csci5308.codeLabeller.Repsoitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Repository;

/**
 * user sign up repository
 */
@Repository
public class UserSignUpRepository {

    @Autowired
    UserDetailsManager jdbcUserDetailsManager;

    /**
     * calling jdbc manager to register the user.
     * @param user: user details object.
     */
    public void registerTheUser(UserDetails user){
        jdbcUserDetailsManager.createUser(user);
    }

    /**
     * find username based on username
     * @param username: username
     * @return: user details object.
     */
    public UserDetails findByUsername(String username){
        return jdbcUserDetailsManager.loadUserByUsername(username);
    }

}
