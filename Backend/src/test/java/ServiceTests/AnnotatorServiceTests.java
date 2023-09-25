package ServiceTests;

import com.csci5308.codeLabeller.Models.*;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import com.csci5308.codeLabeller.Models.DTO.AnnotatorHighlightTagResponse;
import com.csci5308.codeLabeller.Models.DTO.CodeHighlightResponse;
import com.csci5308.codeLabeller.Models.DTO.SurveyResponse;
import com.csci5308.codeLabeller.Repsoitory.AnnotatorRepository;
import com.csci5308.codeLabeller.Service.*;
import org.aspectj.apache.bcel.classfile.Code;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AnnotatorServiceTests {

    @Mock
    SurveyService surveyService;
    @Mock
    AnnotatorRepository annotatorRepository;
    @Mock
    AnnotationService annotationService;
    @Mock
    SnippetService snippetService;
    @Mock
    HighlighterService highlighterService;

    @InjectMocks
    AnnotatorService annotatorService;

    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllSurveysTest(){
        List<SurveyResponse> list = new ArrayList<>();
        SurveyResponse surveyResponse = new SurveyResponse();
        list.add(surveyResponse);
        Mockito.when(surveyService.getAllSurveys()).thenReturn(list);
        List<SurveyResponse> responseList = annotatorService.getAllSurveys();
        Assertions.assertTrue(responseList.size()==1);
    }

    @Test
    public void getAllApprovedSurveysTest(){
        String username = "johndoe";
        String surveyName = "topic";
        CodeSurvey codeSurvey = Mockito.mock(CodeSurvey.class);
        codeSurvey.setSurveyName(surveyName);
        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setSurveyName(surveyName);
        List<CodeSurvey> list = new ArrayList<>(){{
            add(codeSurvey);
        }};
        Annotator annotator = new Annotator();
        annotator.setApprovedCodeSurvey(list);

        SurveyApprovalRequest surveyApprovalRequest = new SurveyApprovalRequest();
        surveyApprovalRequest.setAnnotatorUsername(username);

        Mockito.when(annotatorRepository.findById(username)).thenReturn(Optional.of(annotator));
        Mockito.when(surveyService.makeSurveyResponse(codeSurvey)).thenReturn(surveyResponse);

        List<SurveyResponse> responses = annotatorService.getAllApprovedSurveys(username);

        Assertions.assertTrue(responses.get(0).getSurveyName().equals(surveyName));
    }

    @Test
    public void getAllPendingSurveys(){
        String username = "johndoe";
        String surveyName = "topic";
        CodeSurvey codeSurvey = Mockito.mock(CodeSurvey.class);
        codeSurvey.setSurveyName(surveyName);
        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setSurveyName(surveyName);
        List<CodeSurvey> list = new ArrayList<>(){{
            add(codeSurvey);
        }};
        Annotator annotator = new Annotator();
        annotator.setPendingApprovalsSurveys(list);

        Mockito.when(annotatorRepository.findById(username)).thenReturn(Optional.of(annotator));
        Mockito.when(surveyService.makeSurveyResponse(codeSurvey)).thenReturn(surveyResponse);

        List<SurveyResponse> responses = annotatorService.getAllPendingSurveys(username);

        Assertions.assertTrue(responses.get(0).getSurveyName().equals(surveyName));
    }

    @Test
    public void tagSnippetWithAnnotationsTest(){
        String username = "johndoe";
        long surveyId = 1;
        long snippetId = 1;
        AnnotatorHighlightTagResponse ahtr = new AnnotatorHighlightTagResponse();
        ahtr.setSnippetId(snippetId);
        ahtr.setAnnotationResponseList(new ArrayList<AnnotationResponse>());
        List<CodeHighlightResponse> chl = new ArrayList<>();
        ahtr.setCodeHighlightResponseList(chl);

        CodeAnnotations codeAnnotations = new CodeAnnotations();
        Set<CodeAnnotations> codeAnnotationsSet = new HashSet<>();
        codeAnnotationsSet.add(codeAnnotations);

        Mockito.when(annotationService.getAllCodeAnnotations(ahtr.getAnnotationResponseList())).thenReturn(codeAnnotationsSet);
        CodeSnippet codeSnippet = new CodeSnippet();
        codeSnippet.setTags(codeAnnotationsSet);
        Mockito.when(snippetService.getCodeSnippet(snippetId)).thenReturn(codeSnippet);
        Set<CodeHighlights> set = new HashSet<>(){{
            add(new CodeHighlights());
        }};
        codeSnippet.setHighlightList(set);
        Mockito.when(highlighterService.getAllHighlights(username,codeSnippet,ahtr.getCodeHighlightResponseList())).thenReturn(set);

        annotatorService.tagSnippetWithAnnotations(username,surveyId,snippetId,ahtr);
        Mockito.verify(snippetService).updateSnippet(any(CodeSnippet.class));
    }
}
