package ServiceTests;

import com.csci5308.codeLabeller.Models.CodeSnippet;
import com.csci5308.codeLabeller.Models.DTO.StartSurveyResponse;
import com.csci5308.codeLabeller.Service.StartSurveyService;
import com.csci5308.codeLabeller.Service.SurveyService;
import org.aspectj.apache.bcel.classfile.Code;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class StartSurveyServiceTests {

    @Mock
    SurveyService surveyService;

    @InjectMocks
    StartSurveyService startSurveyService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void startTheSurveyTest(){
        int page = 1;
        long surveyId = 1;

        startSurveyService.startTheSurvey(surveyId,page);
        StartSurveyResponse startSurveyResponse = new StartSurveyResponse();
        startSurveyResponse.setSnippetId(surveyId);
        List<StartSurveyResponse> startSurveyResponseList = new ArrayList<>(){{
            add(startSurveyResponse);
        }};
        Page<StartSurveyResponse> startSurveyResponsePage = new PageImpl<>(startSurveyResponseList);

        Mockito.lenient().when(surveyService.startSurvey(surveyId,page))
                .thenReturn(startSurveyResponsePage);

        Assertions.assertTrue(startSurveyResponsePage.getContent().size()==1);
    }

    @Test
    public void getIndexSnippetTest(){
        Set<CodeSnippet> snippetSet = new HashSet<>();
        CodeSnippet codeSnippet1 = new CodeSnippet();
        snippetSet.add(codeSnippet1);
        int index = 0;

        CodeSnippet codeSnippet = startSurveyService.getIndexSnippet(snippetSet, index);
        Assertions.assertTrue(codeSnippet instanceof CodeSnippet);
    }

}
