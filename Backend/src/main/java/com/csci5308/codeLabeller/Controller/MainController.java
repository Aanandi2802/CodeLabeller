package com.csci5308.codeLabeller.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@EnableMethodSecurity
@RestController
public class MainController {

    /**
     * This url returns string with name of annotator
     * It expects preauthorize and allow only two authorization [ADMIN, ANNOTATOR]
     * @return username
     */
    @GetMapping("/annotator")
    @PreAuthorize("hasAuthority('ANNOTATOR')")
    public String getLabeller(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName()+" is Annotator";
    }
}
