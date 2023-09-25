package RepositoryTests;

import com.csci5308.codeLabeller.Repsoitory.UserSignUpRepository;
import com.csci5308.codeLabeller.Service.AnnotationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public class UserSignUpRepositoryTests {

    @Mock
    private UserDetailsManager jdbcUserDetailsManager;

    @InjectMocks
    private UserSignUpRepository userSignUpRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerTheUserTest(){
        UserDetails user  = Mockito.mock(UserDetails.class);

        userSignUpRepository.registerTheUser(user);


        Mockito.verify(jdbcUserDetailsManager).createUser(user);

    }

    @Test
    public void findByUsernameTest(){
        String username = "johndoe";
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(jdbcUserDetailsManager.loadUserByUsername(username)).thenReturn(userDetails);

        UserDetails user = userSignUpRepository.findByUsername(username);

        Assertions.assertTrue(userDetails.equals(user));
    }


}
