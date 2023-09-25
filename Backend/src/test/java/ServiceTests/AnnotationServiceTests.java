package ServiceTests;

import com.csci5308.codeLabeller.Models.CodeAnnotations;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import com.csci5308.codeLabeller.Models.DTO.AdminSnippetsAnnotationsDTO;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import com.csci5308.codeLabeller.Repsoitory.AnnotationsRepository;
import com.csci5308.codeLabeller.Repsoitory.SurveyRepository;
import com.csci5308.codeLabeller.Service.AnnotationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class AnnotationServiceTests {
    @Mock
    AnnotationsRepository annotationsRepository;
    @Mock
    SurveyRepository surveyRepository;

    @InjectMocks
    AnnotationService annotationService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createAnnotationsTest(){
        AdminSnippetsAnnotationsDTO asaDTO = new AdminSnippetsAnnotationsDTO();
        List<String> stringList = new ArrayList<>(){{
            add("abcd");
        }};
        asaDTO.setAnnotations(stringList);
        CodeSurvey codeSurvey = Mockito.mock(CodeSurvey.class);

        annotationService.createAnnotations(asaDTO,codeSurvey);
        Mockito.verify(annotationsRepository).saveAll(any(List.class));
    }

    @Test
    public void makeAnnotationResponseTest(){
        String name = "john";
        CodeAnnotations codeAnnotations = new CodeAnnotations();
        codeAnnotations.setName(name);

        AnnotationResponse annotationResponse = annotationService
                .makeAnnotationResponse(codeAnnotations);

        Assertions.assertTrue(annotationResponse.getName().equals(codeAnnotations.getName()));
    }

    @Test
    public void makeListAnnotationResponseTest(){
        Set<CodeAnnotations> codeAnnotationsSet = new HashSet<>();
        String name = "john";
        CodeAnnotations codeAnnotations = new CodeAnnotations();
        codeAnnotations.setName(name);

        codeAnnotationsSet.add(codeAnnotations);

        List<AnnotationResponse> responses = annotationService
                .makeListAnnotationResponse(codeAnnotationsSet);

        Assertions.assertTrue(responses.get(0).getName().equals(codeAnnotations.getName()));
    }

    @Test
    public void getAllAnnotationsTest(){
        String username = "johndoe";
        long surveyId = 1;
        CodeSurvey codeSurvey = new CodeSurvey();
        Mockito.when(surveyRepository.findById(surveyId))
                .thenReturn(Optional.of(codeSurvey));

        Set<CodeAnnotations> codeAnnotationsSet = new HashSet<>();
        codeSurvey.setAnnotationList(codeAnnotationsSet);

        List<AnnotationResponse> annotationResponseList = annotationService
                .getAllAnnotations(username,surveyId);

        Assertions.assertTrue(annotationResponseList.size()==0);
    }

    @Test
    public void getAnnotationTest(){
        String username = "johndoe";
        long surveyId = 1;
        long id = 1;

        CodeAnnotations codeAnnotations = new CodeAnnotations();
        codeAnnotations.setName(username);
        AnnotationResponse annotationResponse = new AnnotationResponse();
        annotationResponse.setName(username);

        Mockito.when(annotationsRepository.findById(id)).thenReturn(Optional.of(codeAnnotations));

        AnnotationService annotationService1 = Mockito.spy(annotationService);

        Mockito.doReturn(annotationResponse).
                when(annotationService1)
                .makeAnnotationResponse(codeAnnotations);

        AnnotationResponse annotationResponse1 = annotationService1.getAnnotation(username,surveyId,id);

        Assertions.assertTrue(annotationResponse1.getName().equals(annotationResponse.getName()));
    }

    @Test
    public void getAllCodeAnnotationsTest(){
        long id = 1;
        List<AnnotationResponse> annotationsTag = new ArrayList<>();
        AnnotationResponse annotationResponse = new AnnotationResponse();
        annotationResponse.setAnnotationID(id);
        annotationsTag.add(annotationResponse);
        CodeAnnotations codeAnnotations = new CodeAnnotations();
        codeAnnotations.setAnnotationID(id);

        Mockito.when(annotationsRepository.findById(id))
                .thenReturn(Optional.of(codeAnnotations));

        Set<CodeAnnotations> codeAnnotationsSet = annotationService
                .getAllCodeAnnotations(annotationsTag);

        Assertions.assertTrue(codeAnnotationsSet.size()==1);
    }

    @Test
    public void getCodeAnnotationTest(){
        long id = 1;
        AnnotationResponse annotationResponse = new AnnotationResponse();
        annotationResponse.setAnnotationID(id);

        CodeAnnotations codeAnnotations = new CodeAnnotations();
        codeAnnotations.setAnnotationID(id);

        Mockito.when(annotationsRepository.findById(id))
                .thenReturn(Optional.of(codeAnnotations));

        CodeAnnotations codeAnnotations1 = annotationService.getCodeAnnotation(annotationResponse);

        Assertions.assertTrue(codeAnnotations1.getAnnotationID().equals(codeAnnotations.getAnnotationID()));

    }
}