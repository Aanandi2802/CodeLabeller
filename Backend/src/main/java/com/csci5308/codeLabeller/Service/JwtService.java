package com.csci5308.codeLabeller.Service;

import com.csci5308.codeLabeller.Enums.JwtEnum;
import com.csci5308.codeLabeller.Enums.JwtNumbers;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This service handles all methods related to JWT token:
 * validating it.
 * creating it.
 * setting its expiration.
 */
@Service
public class JwtService {
    private String secretStringKey = "2A472D4B6150645367566B58703273357638792F423F4528482B4D6251655468";
    private Key secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretStringKey));

    /**
     * generates JWT token.
     * @param userDetails: user trying to login.
     * @return: jwt token string.
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(JwtEnum.Authority.toString(), userDetails.getAuthorities());
        return encodeTheUserWithClaims(userDetails, claimsMap);
    }

    /**
     * encode the token with additional claims added.
     * @param userDetails: user
     * @param claimsMap: claims added.
     * @return: modified token.
     */
    public String encodeTheUserWithClaims(UserDetails userDetails, Map<String, Object> claimsMap) {
        String token = Jwts.builder()
                .setClaims(claimsMap)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + hoursToMiliseconds(JwtNumbers.JwtTokenHours.getValue())))
                .signWith(secretKey, SignatureAlgorithm.HS256).compact();

        return token;
    }

    /**
     * helper function to convert hour to milisecond.
     * @param hour: hour
     * @return: time in miliseconds.
     */
    public long hoursToMiliseconds(int hour) {
        int milisecond = hour * JwtNumbers.Seconds.getValue() * JwtNumbers.Minutes.getValue() * JwtNumbers.Miliseconds.getValue();
        return milisecond;
    }

    /**
     * fetches username
     * @param jwtToken: jwtToken
     * @return: username contained in token.
     */
    public String getUsername(String jwtToken) {
        Claims claims = getAllClaims(jwtToken);
        return claims.getSubject();
    }

    /**
     * fetches expiration date.
     * @param jwtToken: jwt token
     * @return: date of expiration.
     */
    public Date getEpirationDate(String jwtToken) {
        return getAllClaims(jwtToken).getExpiration();
    }

    /**
     * checks of token is valid.
     * @param jwtToken: jwt token
     * @param userDetails: user.
     * @return: boolean if toke is valid or not.
     */
    public boolean isValid(String jwtToken, UserDetails userDetails) {
        String username = getUsername(jwtToken);
        if(!isExpired(jwtToken) && userDetails.getUsername().equals(username)) {
            return true;
        }
        return false;
    }


    /**
     * checks if token is expired.
     * @param jwtToken: jwt token.
     * @return: boolean if token is expired or not.
     */
    private boolean isExpired(String jwtToken) {
        if(getEpirationDate(jwtToken).before(new Date())){
            return true;
        }
        return false;
    }

    /**
     * fetches all claims contained by token.
     * @param jwtToken: jwt token
     * @return: claims added.
     */
    private Claims getAllClaims(String jwtToken){
         return (Claims)Jwts.parserBuilder().setSigningKey(secretKey).build().parse(jwtToken).getBody();
    }
}
