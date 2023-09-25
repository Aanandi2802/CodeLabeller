package ServiceTests;

import com.csci5308.codeLabeller.Models.CodeAnnotations;
import com.csci5308.codeLabeller.Models.CodeSnippet;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import com.csci5308.codeLabeller.Models.DTO.*;
import com.csci5308.codeLabeller.Repsoitory.SurveyRepository;
import com.csci5308.codeLabeller.Service.AnnotationService;
import com.csci5308.codeLabeller.Service.SnippetService;
import com.csci5308.codeLabeller.Service.SurveyService;
import jakarta.inject.Inject;
import org.aspectj.apache.bcel.classfile.Code;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTests {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private AnnotationService annotationService;
    @Mock
    private SnippetService snippetService;
    @InjectMocks
    private SurveyService surveyService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createSurveyTest(){
        String username = "sghai";
        String surveyName = "survey";
        String surveyLanguage = ".java";
        long surveyThreshold = 10;

        AdminSnippetsAnnotationsDTO asaDTO = new AdminSnippetsAnnotationsDTO();
        asaDTO.setUsername(username);
        asaDTO.setSurveyName(surveyName);
        asaDTO.setSurveyLanguage(surveyLanguage);
        asaDTO.setSurveyThreshold(surveyThreshold);

        CodeSurvey survey = new CodeSurvey();
        survey.setUsername(asaDTO.getUsername());
        survey.setSurveyName(asaDTO.getSurveyName());
        survey.setSurveyLanguage(asaDTO.getSurveyLanguage());
        survey.setSurveyThreshold(asaDTO.getSurveyThreshold());

        Mockito.when(surveyRepository.save(any(CodeSurvey.class))).thenReturn(survey);

        CodeSurvey csResponse = surveyService.createSurvey(asaDTO);

        boolean checkUsername = csResponse.getUsername().equals(username);
        boolean checkSurveyName = csResponse.getSurveyName().equals(surveyName);
        Assertions.assertTrue(checkSurveyName&&checkSurveyName);
    }

    @Test
    public void getSurvey(){
        long surveyId = 1;
        String surveyName = "john";

        CodeSurvey codeSurvey = Mockito.mock(CodeSurvey.class);

        SurveyService surveyServiceSpy = Mockito.spy(SurveyService.class);
        SurveyResponse surveyResponse = Mockito.mock(SurveyResponse.class);

        Mockito.doReturn(codeSurvey).when(surveyServiceSpy).getCodeSurvey(surveyId);
        Mockito.doReturn(surveyResponse).when(surveyServiceSpy).makeSurveyResponse(codeSurvey);
        Mockito.when(surveyResponse.getSurveyName()).thenReturn(surveyName);
        Mockito.when(codeSurvey.getSurveyName()).thenReturn(surveyName);

        SurveyResponse surveyResponseOutput = surveyServiceSpy.getSurvey(surveyId);

        Assertions.assertTrue(codeSurvey.getSurveyName().equals(surveyResponseOutput.getSurveyName()));
    }

    @Test
    public void getAllSurveys(){
        String surveyName = "survey";

        CodeSurvey survey = new CodeSurvey();
        survey.setSurveyName(surveyName);

        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setSurveyName(surveyName);

        List<CodeSurvey> listCodeSurveys = new ArrayList<>();
        listCodeSurveys.add(survey);

        SurveyService surveyServiceSpy = Mockito.spy(surveyService);
        Mockito.when(surveyRepository.findAll()).thenReturn(listCodeSurveys);
        Mockito.doReturn(surveyResponse)
                .when(surveyServiceSpy)
                .makeSurveyResponse(any(CodeSurvey.class));

        List<SurveyResponse> listCodeSurveysResponse = surveyServiceSpy.getAllSurveys();

        Assertions.assertTrue(listCodeSurveysResponse.get(0).getSurveyName().equals(surveyName));
    }

    @Test
    public void getAllSurveysUsername(){
        String surveyName = "survey";
        String userName = "johndoe";

        CodeSurvey survey = new CodeSurvey();
        survey.setSurveyName(surveyName);

        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setSurveyName(surveyName);

        List<CodeSurvey> listCodeSurveys = new ArrayList<>();
        listCodeSurveys.add(survey);

        SurveyService surveyServiceSpy = Mockito.spy(surveyService);
        Mockito.when(surveyRepository.findAllByUsername(userName)).thenReturn(listCodeSurveys);
        Mockito.doReturn(surveyResponse).when(surveyServiceSpy).makeSurveyResponse(any(CodeSurvey.class));

        List<SurveyResponse> listCodeSurveysResponse = surveyServiceSpy.getAllSurveys(userName);

        Assertions.assertTrue(listCodeSurveysResponse.get(0).getSurveyName().equals(surveyName));
    }

    @Test
    public void makeSurveyResponseTest(){
        String surveyName = "survey";

        CodeSurvey survey = Mockito.mock(CodeSurvey.class);
        CodeAnnotations codeAnnotations = Mockito.mock(CodeAnnotations.class);
        CodeSnippet codeSnippet = Mockito.mock(CodeSnippet.class);
        AnnotationResponse annotationResponse = Mockito.mock(AnnotationResponse.class);
        SnippetResponse snippetResponse = Mockito.mock(SnippetResponse.class);

        Mockito.lenient().when(annotationService.makeAnnotationResponse(codeAnnotations))
                .thenReturn(annotationResponse);
        Mockito.lenient().when(snippetService.makeSnippetResponse(codeSnippet))
                .thenReturn(snippetResponse);
        Mockito.lenient().when(survey.getSurveyName())
                .thenReturn(surveyName);
        Mockito.lenient().when(survey.getAnnotationList())
                .thenReturn(new HashSet<>(){{
                    add(codeAnnotations);
                }});
        Mockito.lenient().when(survey.getSnippetList())
                .thenReturn(new HashSet<>(){{
                    add(codeSnippet);
                }});

        SurveyResponse surveyResponse = surveyService.makeSurveyResponse(survey);

        Assertions.assertTrue(survey.getSurveyName().equals(surveyResponse.getSurveyName()));
    }

    @Test
    public void getCodeSurveyTest(){
        long surveyId = 1;
        CodeSurvey survey = Mockito.mock(CodeSurvey.class);
        Mockito.when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
        CodeSurvey codeSurvey = surveyService.getCodeSurvey(surveyId);
        Assertions.assertTrue(survey.equals(codeSurvey));

    }

//    @Test
//    public void startSurveyTest(){
//        long surveyId = 1;
//        int page = 0;
//        long snippetId = 1;
//        byte[] bytes = null;
//
//        CodeSurvey survey = Mockito.mock(CodeSurvey.class);
//        Page<CodeSnippet> codeSnippetPage = Mockito.mock(Page.class);
//        Page<StartSurveyResponse> startSurveyResponsePage = Mockito.mock(Page.class);
//        Set<CodeAnnotations> codeAnnotationsList = Mockito.mock(Set.class);
//        List<AnnotationResponse> annotationResponseList = Mockito.mock(List.class);
//        CodeSnippet codeSnippet = Mockito.mock(CodeSnippet.class);
//
//
//        Mockito.when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));
//        Mockito.when(snippetService.getSnippetPage(survey,page)).thenReturn(codeSnippetPage);
//        Mockito.when(survey.getAnnotationList()).thenReturn(codeAnnotationsList);
//        Mockito.when(annotationService.makeListAnnotationResponse(codeAnnotationsList))
//                .thenReturn(annotationResponseList);
//        Mockito.when(codeSnippet.getCodeSnippetId()).thenReturn(snippetId);
//        Mockito.when(codeSnippet.getSnippetText()).thenReturn(bytes);
//        Mockito.when(codeSnippetPage.map(codeSnippet2 -> new StartSurveyResponse(codeSnippet2.getCodeSnippetId(),codeSnippet2.getSnippetText(),annotationService.makeListAnnotationResponse(survey.getAnnotationList())))).thenReturn(startSurveyResponsePage);
//
//
//        Page<StartSurveyResponse> startSurveyResponses = surveyService.startSurvey(surveyId,page);
//
//        Assertions.assertTrue(startSurveyResponsePage.equals(startSurveyResponses));
//
//    }
}
