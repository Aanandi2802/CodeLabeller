package ServiceTests;

import com.csci5308.codeLabeller.Enums.MiscEnums;
import com.csci5308.codeLabeller.Models.CodeAnnotations;
import com.csci5308.codeLabeller.Models.CodeHighlights;
import com.csci5308.codeLabeller.Models.CodeSnippet;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import com.csci5308.codeLabeller.Models.DTO.AdminSnippetsAnnotationsDTO;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import com.csci5308.codeLabeller.Models.DTO.CodeHighlightResponse;
import com.csci5308.codeLabeller.Models.DTO.SnippetResponse;
import com.csci5308.codeLabeller.Repsoitory.SnippetRepository;
import com.csci5308.codeLabeller.Repsoitory.SurveyRepository;
import com.csci5308.codeLabeller.Service.AnnotationService;
import com.csci5308.codeLabeller.Service.HighlighterService;
import com.csci5308.codeLabeller.Service.SnippetService;
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
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class SnippetServiceTests {

    @Mock
    SnippetRepository snippetRepository;
    @Mock
    HighlighterService highlighterService;

    @Mock
    AnnotationService annotationService;

    @Mock
    SurveyRepository surveyRepository;

    @InjectMocks
    SnippetService snippetService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void makeSnippetResponseTests(){
        CodeAnnotations codeAnnotations = Mockito.mock(CodeAnnotations.class);
        AnnotationResponse annotationResponse = Mockito.mock(AnnotationResponse.class);
        SnippetResponse snippetResponse = Mockito.mock(SnippetResponse.class);
        CodeSnippet codeSnippet = new CodeSnippet();
        codeSnippet.setTags(new HashSet<>(){{
            add(codeAnnotations);
        }});
        CodeHighlights codeHighlights = new CodeHighlights();
        codeSnippet.setHighlightList(new HashSet<>(){{
            add(codeHighlights);
        }});
        CodeHighlightResponse chr = new CodeHighlightResponse();
        Set<CodeHighlightResponse> highlightResponseList = new HashSet<>(){{
            add(chr);
        }};

        Mockito.when(highlighterService.makeAllHighlightResponse(codeSnippet.getHighlightList()))
                .thenReturn(highlightResponseList);

        Set<AnnotationResponse> annotationResponseList = Mockito.mock(Set.class);
        Mockito.when(annotationService.makeAnnotationResponse(codeAnnotations))
                .thenReturn(annotationResponse);
        Mockito.lenient().doNothing().when(snippetResponse).setTaggedAnnotations(annotationResponseList);

        SnippetResponse makeSnippetResponse = snippetService.makeSnippetResponse(codeSnippet);

        Assertions.assertTrue(snippetResponse.getSnippetID()==0);
    }

    @Test
    public void getAllSnippetsTests(){
        String username = "johndoe";
        long surveyId = 1;
        CodeSurvey codeSurvey = Mockito.mock(CodeSurvey.class);
        Mockito.when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(codeSurvey));
        CodeSnippet codeSnippet = Mockito.mock(CodeSnippet.class);
        Set<CodeSnippet> codeSnippetSet = new HashSet<>();
        codeSnippetSet.add(codeSnippet);
        SnippetResponse snippetResponse = Mockito.mock(SnippetResponse.class);

        Mockito.when(codeSurvey.getSnippetList()).thenReturn(codeSnippetSet);

        SnippetService snippetServiceSpy = Mockito.spy(snippetService);

        Mockito.doReturn(snippetResponse).when(snippetServiceSpy)
                .makeSnippetResponse(codeSnippet);

        Assertions.assertTrue(snippetServiceSpy.getAllSnippets(username,surveyId).size()==1);
    }

    @Test
    public void getSnippetTest(){
        long surveyId = 1;
        long id = 1;
        String username = "johndoe";
        CodeSnippet codeSnippet = Mockito.mock(CodeSnippet.class);
        SnippetResponse snippetResponse = Mockito.mock(SnippetResponse.class);

        Mockito.when(snippetRepository.findById(surveyId)).thenReturn(Optional.of(codeSnippet));
        SnippetService snippetService1 = Mockito.spy(snippetService);

        Mockito.doReturn(snippetResponse)
                .when(snippetService1)
                .makeSnippetResponse(codeSnippet);

        SnippetResponse snippetResponse1 = snippetService1.getSnippet(username,surveyId,id);

        Assertions.assertTrue(snippetResponse1.equals(snippetResponse));
    }

    @Test
    public void getCodeSnippetTest(){
        long snippetId = 1;
        CodeSnippet codeSnippet = Mockito.mock(CodeSnippet.class);

        Mockito.when(snippetRepository.findById(snippetId)).thenReturn(Optional.of(codeSnippet));

        CodeSnippet codeSnippet1 = snippetService.getCodeSnippet(snippetId);

        Assertions.assertTrue(codeSnippet1.equals(codeSnippet));
    }

//    @Test
//    public void getSnippetPageTest(){
//        CodeSurvey codeSurvey = new CodeSurvey();
//        int page = 1;
//
//        Sort sort = Mockito.mock(Sort.class);
//
//        Mockito.when(snippetRepository.findBySurvey(codeSurvey, PageRequest.of(page, MiscEnums.NumberOfPages.getValue()), sort))
//                .thenReturn((Page<CodeSnippet>) any(PageRequest.class));
//
//        Page<CodeSnippet> codeSnippetPage = snippetService.getSnippetPage(codeSurvey, page);
//
//        Assertions.assertTrue(codeSnippetPage==null);
//    }
}