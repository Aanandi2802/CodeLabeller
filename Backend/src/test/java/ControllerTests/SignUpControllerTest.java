package ControllerTests;

import com.csci5308.codeLabeller.Controller.MainController;
import com.csci5308.codeLabeller.Controller.SignUpController;
import com.csci5308.codeLabeller.Models.DTO.AuthResponse;
import com.csci5308.codeLabeller.Models.DTO.UserSignUpDetails;
import com.csci5308.codeLabeller.Service.UserSignUpService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SignUpControllerTest {
    @Mock
    UserSignUpService userSignUpService;
    @InjectMocks
    SignUpController signUpController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController).build();
    }

    private MockMvc mockMvc;
    @Test
    void signUp_Exception() throws Exception {
        UserSignUpDetails user = new UserSignUpDetails();
        user.setUsername("Testuser");
        user.setPassword("TestPassword");
        ObjectMapper objectMapper = new ObjectMapper();
        when(userSignUpService.registerUser(user)).thenReturn(new AuthResponse("token"));
        mockMvc.perform(MockMvcRequestBuilders.post("/signup").contentType(MediaType.APPLICATION_JSON)
                        .contentType(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void signUp_ReturnsOkResponse() throws Exception {
        UserSignUpDetails userSignUpDetails = new UserSignUpDetails();
        userSignUpDetails.setUsername("testuser");
        userSignUpDetails.setPassword("password");
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken("TOKEN");
        when(userSignUpService.registerUser(userSignUpDetails)).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"testuser\", \"password\": \"password\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}