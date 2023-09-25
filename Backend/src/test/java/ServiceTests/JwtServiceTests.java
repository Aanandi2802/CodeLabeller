package ServiceTests;

import com.csci5308.codeLabeller.Enums.JwtEnum;
import com.csci5308.codeLabeller.Enums.UserAuthority;
import com.csci5308.codeLabeller.Service.JwtService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTests {

    @InjectMocks
    JwtService jwtService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void generateTokenTest(){
        String token = "1234";
        String username = "johndoe";
        String password = "1234";
        List<GrantedAuthority> list = new ArrayList<>();
        GrantedAuthority ga = new SimpleGrantedAuthority(UserAuthority.ANNOTATOR.toString());
        list.add(ga);
        UserDetails userDetails = new User(username,password,list);
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(JwtEnum.Authority.toString(), userDetails.getAuthorities());

        JwtService jwtServiceSpy = Mockito.spy(jwtService);

        Mockito.doReturn(token)
                .when(jwtServiceSpy)
                .encodeTheUserWithClaims(userDetails,claimsMap);

        String response = jwtServiceSpy.generateToken(userDetails);

        Assertions.assertTrue(response==token);
    }

    @Test
    public void hoursToMilisecondsTest(){
        int hour = 10;
        long mil = jwtService.hoursToMiliseconds(hour);
        Assertions.assertTrue(mil==hour*60*60*1000);
    }
}
