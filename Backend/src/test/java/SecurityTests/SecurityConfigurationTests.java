package SecurityTests;

import com.csci5308.codeLabeller.Security.SecurityConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigurationTests {

    @InjectMocks
    SecurityConfiguration securityConfiguration;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void authManagerTest() throws Exception {
        AuthenticationConfiguration config = Mockito.mock(AuthenticationConfiguration.class);
        AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

        Mockito.when(config.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager response = securityConfiguration.authManager(config);

        Assertions.assertTrue(response.equals(authenticationManager));
    }
}
