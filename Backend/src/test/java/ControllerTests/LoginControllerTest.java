package ControllerTests;

import com.csci5308.codeLabeller.Controller.LoginController;
import com.csci5308.codeLabeller.Models.DTO.AuthResponse;
import com.csci5308.codeLabeller.Models.DTO.UserLoginDetails;
import com.csci5308.codeLabeller.Repsoitory.UserSignUpRepository;
import com.csci5308.codeLabeller.Service.UserSignUpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.InvalidKeyException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @Mock
    UserSignUpService userSignUpService;
    @Mock
    UserSignUpRepository userSignUpRepository;

    @InjectMocks
    LoginController loginController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    private MockMvc mockMvc;

    @Test
    public void testLoginSuccess() throws Exception {
        UserLoginDetails user = new UserLoginDetails();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        ObjectMapper objectMapper = new ObjectMapper();
        when(userSignUpService.authenticate(user)).thenReturn(new AuthResponse("token"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        //when(userSignUpRepository.findByUsername(user.getUsername())).thenReturn((UserDetails) user);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").value("token"));
    }
}

//    @Test
//    public void testInvalidUsernameOrPassword() throws Exception {
//        UserLoginDetails user = new UserLoginDetails();
//        user.setUsername("testuser");
//        user.setPassword("invalidpassword");
//        ObjectMapper objectMapper = new ObjectMapper();
//        when(userSignUpService.authenticate(user)).thenThrow(new InvalidKeyException("Invalid username or password"));
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.authResponse").value("Invalid username or password"));
//    }
//}