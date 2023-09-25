package ServiceTests;

import com.csci5308.codeLabeller.Enums.UserAuthority;
import com.csci5308.codeLabeller.Models.Annotator;
import com.csci5308.codeLabeller.Models.DTO.AuthResponse;
import com.csci5308.codeLabeller.Models.DTO.UserLoginDetails;
import com.csci5308.codeLabeller.Models.DTO.UserSignUpDetails;
import com.csci5308.codeLabeller.Repsoitory.AnnotatorRepository;
import com.csci5308.codeLabeller.Repsoitory.UserSignUpRepository;
import com.csci5308.codeLabeller.Service.JwtService;
import com.csci5308.codeLabeller.Service.UserSignUpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UserSignUpServiceTests {

    @Mock
    UserSignUpRepository userSignUpRepository;
    @Mock
    AnnotatorRepository annotatorRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @Mock
    AuthenticationManager authManager;

    @InjectMocks
    UserSignUpService userSignUpService;

    @Test
    public void registerUserTest(){
        String username = "sghai";
        String password = "1234";
        String authority = UserAuthority.ANNOTATOR.toString();
        UserSignUpDetails user = new UserSignUpDetails();
        user.setUsername(username);
        user.setPassword(password);
        user.setAuthority(authority);
        Annotator annotator = Mockito.mock(Annotator.class);


        Mockito.doNothing().when(userSignUpRepository).registerTheUser(any(UserDetails.class));
        Mockito.when(annotatorRepository.save(any(Annotator.class))).thenReturn(annotator);
        Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn(password);

        String jwtToken = "1234";

        Mockito.when(jwtService.generateToken(any(UserDetails.class))).thenReturn(jwtToken);

        AuthResponse authResponse = userSignUpService.registerUser(user);

        Assertions.assertTrue(authResponse.getJwtToken().equals(jwtToken));
    }

    @Test
    public void authenticateTest(){
        String username = "sghai";
        String password = "1234";
        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setUsername(username);
        userLoginDetails.setPassword(password);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        UserDetails userDetails = Mockito.mock(UserDetails.class);

        Mockito.when(userSignUpRepository.findByUsername(userLoginDetails.getUsername())).thenReturn(userDetails);
        String jwtToken = "1234";
        Mockito.when(jwtService.generateToken(userDetails)).thenReturn(jwtToken);

        AuthResponse authResponse = userSignUpService.authenticate(userLoginDetails);
        Assertions.assertTrue(authResponse.getJwtToken().equals(jwtToken));

    }
}
