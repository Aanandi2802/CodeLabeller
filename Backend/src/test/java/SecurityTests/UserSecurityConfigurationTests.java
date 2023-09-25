package SecurityTests;

import com.csci5308.codeLabeller.Security.UserSecurityConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@ExtendWith(MockitoExtension.class)
public class UserSecurityConfigurationTests {

    @InjectMocks
    UserSecurityConfiguration userSecurityConfiguration;

    @Mock
    DataSource dataSource;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void passwordEncoderTest(){
        PasswordEncoder passwordEncoder = userSecurityConfiguration.passwordEncoder();
        Assertions.assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void authenticationProviderTest(){
        UserDetailsManager userDetailsManager  = Mockito.mock(UserDetailsManager.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        UserSecurityConfiguration userSecurityConfiguration1 = Mockito.spy(userSecurityConfiguration);
        Mockito.when(userSecurityConfiguration1.userDetailsManager())
                .thenReturn(userDetailsManager);
        Mockito.when(userSecurityConfiguration1.passwordEncoder())
                .thenReturn(passwordEncoder);

        AuthenticationProvider dao = userSecurityConfiguration1.authenticationProvider();
        Assertions.assertTrue(dao instanceof AuthenticationProvider);


    }
}
