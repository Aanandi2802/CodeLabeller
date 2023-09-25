package ControllerTests;

import com.csci5308.codeLabeller.Controller.AdminController;
import com.csci5308.codeLabeller.Controller.AnnotatorController;
import com.csci5308.codeLabeller.Models.DTO.AnnotatorHighlightTagResponse;
import com.csci5308.codeLabeller.Models.DTO.StartSurveyResponse;
import com.csci5308.codeLabeller.Models.DTO.SurveyResponse;
import com.csci5308.codeLabeller.Service.AnnotatorService;
import com.csci5308.codeLabeller.Service.StartSurveyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

class AnnotatorControllerTest {
    @Mock
    AnnotatorService annotatorService;
    @Mock
    StartSurveyService startSurveyService;
    private MockMvc mockMvc;
    @InjectMocks
    private AnnotatorController annotatorController;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(annotatorController).build();
    }

    @Test
    void getAllSurveys() throws Exception {
        List<SurveyResponse> surveyList = new ArrayList<>();
        surveyList.add(new SurveyResponse());

        auth();

        Mockito.when(annotatorService.getAllSurveys()).thenReturn(surveyList);
        mockMvc.perform(MockMvcRequestBuilders.get("/annotator/username/survey/all/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)));
    }

    @Test
    void startSurvey() throws Exception {
        Page<StartSurveyResponse> startSurveyPage = new PageImpl<>(new ArrayList<>());
        AnnotatorHighlightTagResponse annotatorHighlightTagResponse = new AnnotatorHighlightTagResponse();

        auth();

        ObjectMapper objectMapper = new ObjectMapper();
        Mockito.when(startSurveyService.startTheSurvey(Mockito.anyLong(), Mockito.anyInt())).thenReturn(startSurveyPage);

        mockMvc.perform(MockMvcRequestBuilders.post("/annotator/username/survey/1/start/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(annotatorHighlightTagResponse)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllApprovedSurveys() throws Exception {
        List<SurveyResponse> surveyList = new ArrayList<>();
        surveyList.add(new SurveyResponse());

        auth();

        Mockito.when(annotatorService.getAllApprovedSurveys(Mockito.anyString())).thenReturn(surveyList);
        mockMvc.perform(MockMvcRequestBuilders.get("/annotator/username/survey/approved/all/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)));

    }

    @Test
    void getAllPendingSurveys() throws Exception {
        List<SurveyResponse> surveyList = new ArrayList<>();
        surveyList.add(new SurveyResponse());

        auth();

        Mockito.when(annotatorService.getAllPendingSurveys(Mockito.anyString())).thenReturn(surveyList);
        mockMvc.perform(MockMvcRequestBuilders.get("/annotator/username/survey/pending/all/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)));
    }

    void auth(){
        String username = "johndoe";
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}