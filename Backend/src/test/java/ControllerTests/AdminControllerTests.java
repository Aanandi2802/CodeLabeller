package ControllerTests;

import com.csci5308.codeLabeller.Controller.AdminController;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import com.csci5308.codeLabeller.Models.DTO.AdminSnippetsAnnotationsDTO;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import com.csci5308.codeLabeller.Models.DTO.SnippetResponse;
import com.csci5308.codeLabeller.Models.DTO.SurveyResponse;
import com.csci5308.codeLabeller.Service.AnnotationService;
import com.csci5308.codeLabeller.Service.SnippetService;
import com.csci5308.codeLabeller.Service.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminControllerTests {

    @Mock
    private AnnotationService annotationService;

    @Mock
    private SnippetService snippetService;

    @Mock
    private SurveyService surveyService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void getAllAnnotationsTests() throws Exception{
        List<AnnotationResponse> list = new ArrayList<>();
        AnnotationResponse response = new AnnotationResponse();
        response.setAnnotationID(1L);
        response.setName("John");
        list.add(response);

        String username = "johndoe";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(annotationService.getAllAnnotations(username,1L)).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/johndoe/survey/1/annotation/all/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)));

    }

    @Test
    public void getAnnotation() throws Exception{
        AnnotationResponse response = new AnnotationResponse();
        response.setAnnotationID(1L);
        String name = "John";
        response.setName(name);

        String username = "johndoe";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(annotationService.getAnnotation(username,1L, 1L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/johndoe/survey/1/annotation/1/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(name)));

    }

    @Test
    public void getAllSnippets() throws Exception{
        List<SnippetResponse> list = new ArrayList<>();
        SnippetResponse response = new SnippetResponse();
        response.setSnippetID(1L);
        list.add(response);

        String username = "johndoe";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(snippetService.getAllSnippets(username,1L)).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/johndoe/survey/1/snippet/all/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)));

    }

    @Test
    public void getSnippet() throws Exception{
        SnippetResponse response = new SnippetResponse();
        response.setSnippetID(1L);

        String username = "johndoe";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(snippetService.getSnippet(username,1L, 1L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/johndoe/survey/1/snippet/1/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.snippetID", Matchers.is(1)));
    }

    @Test
    public void getAllSurveys() throws Exception{
        List<SurveyResponse> list = new ArrayList<>();
        SurveyResponse response = new SurveyResponse();
        response.setSurveyID(1L);
        list.add(response);

        String username = "johndoe";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(surveyService.getAllSurveys(username)).thenReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/johndoe/survey/all/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)));

    }

    @Test
    public void getSurvey() throws Exception{
        SurveyResponse response = new SurveyResponse();
        response.setSurveyID(1L);


        String username = "johndoe";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(surveyService.getSurvey(1L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/johndoe/survey/1/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.surveyID", Matchers.is(1)));
    }

    @Test
    public void saveSurvey() throws Exception{
        AdminSnippetsAnnotationsDTO asadDTO = new AdminSnippetsAnnotationsDTO();
        CodeSurvey codeSurvey = new CodeSurvey();

        Mockito.when(surveyService.createSurvey(asadDTO)).thenReturn(codeSurvey);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/johndoe/survey/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(asadDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
