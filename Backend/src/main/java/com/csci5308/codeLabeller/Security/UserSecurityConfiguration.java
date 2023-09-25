package com.csci5308.codeLabeller.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

/**
 * password encoder and authentication provider configuration.
 */
@Configuration
public class UserSecurityConfiguration {
    @Autowired
    DataSource dataSource;

    /**
     * bean for password encoder.
     * @return: password encoder object.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * bean for user details manager.
     * @return: user details manager.
     */
    @Bean
    public UserDetailsManager userDetailsManager(){
        return new JdbcUserDetailsManager(dataSource);
    }

    /**
     * authentication provider.
     * @return: authentication provider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuth = new DaoAuthenticationProvider();
        daoAuth.setUserDetailsService(userDetailsManager());
        daoAuth.setPasswordEncoder(passwordEncoder());
        return daoAuth;
    }
}
