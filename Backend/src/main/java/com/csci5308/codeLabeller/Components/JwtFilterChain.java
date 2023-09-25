package com.csci5308.codeLabeller.Components;

import com.csci5308.codeLabeller.Enums.JwtEnum;
import com.csci5308.codeLabeller.Enums.JwtNumbers;
import com.csci5308.codeLabeller.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * jet filter chain to handle jwt token in header.
 */
@Component
public class JwtFilterChain extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsManager userDetailsManager;

    /**
     * internal filter to extract jwt token, extract all information from it
     * and setting the context holder.
     * @param request: http request
     * @param response: http response
     * @param filterChain: filter chain object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(JwtEnum.AUTHORIZATION.toString());
        String username;
        String jwtToken;
        UserDetails userDetails = null;
        WebAuthenticationDetails webAuthenticationDetails = null;

        if(authHeader!=null && authHeader.startsWith(JwtEnum.Bearer.toString()+" ")){
            jwtToken = authHeader.substring(JwtNumbers.BearerMark.getValue());
            username = jwtService.getUsername(jwtToken);
            userDetails = userDetailsManager.loadUserByUsername(username);
            if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
                if(jwtService.isValid(jwtToken, userDetails)){
                    UsernamePasswordAuthenticationToken unpa;
                    String usrDetName = userDetails.getUsername();
                    String usrDetPass = userDetails.getPassword();
                    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                    unpa = new UsernamePasswordAuthenticationToken(usrDetName, usrDetPass, authorities);
                    webAuthenticationDetails =  new WebAuthenticationDetailsSource().buildDetails(request);
                    unpa.setDetails(webAuthenticationDetails);
                    SecurityContextHolder.getContext().setAuthentication(unpa);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
