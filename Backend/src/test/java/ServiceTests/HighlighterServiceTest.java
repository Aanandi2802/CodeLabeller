package ServiceTests;

import com.csci5308.codeLabeller.Enums.MiscEnums;
import com.csci5308.codeLabeller.Models.CodeAnnotations;
import com.csci5308.codeLabeller.Models.CodeHighlights;
import com.csci5308.codeLabeller.Models.CodeSnippet;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import com.csci5308.codeLabeller.Models.DTO.CodeHighlightResponse;
import com.csci5308.codeLabeller.Repsoitory.HighlighterRepository;
import com.csci5308.codeLabeller.Service.AnnotationService;
import com.csci5308.codeLabeller.Service.HighlighterService;
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
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class HighlighterServiceTest {

    @Mock
    AnnotationService annotationService;

    @Mock
    HighlighterRepository highlighterRepository;

    @InjectMocks
    HighlighterService highlighterService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllHighlightsTest(){
        CodeSnippet codeSnippet = new CodeSnippet();
        CodeAnnotations codeAnnotations = new CodeAnnotations();
        CodeHighlights codeHighlights = new CodeHighlights();
        String username = "johndoe";
        CodeHighlightResponse chr = new CodeHighlightResponse();
        chr.setSpan_start_id("1");
        chr.setSpan_end_id("5");
        chr.setAnnotated_by(username);
        codeHighlights.setSpan_start_id("1");
        codeHighlights.setSpan_end_id("5");
        codeHighlights.setAnnotated_by(username);
        AnnotationResponse annotationResponse = Mockito.mock(AnnotationResponse.class);
        chr.setAnnotationResponse(annotationResponse);
        List<CodeHighlightResponse> chrl = new ArrayList<>();
        chrl.add(chr);

        Mockito.when(annotationService.getCodeAnnotation(annotationResponse)).thenReturn(codeAnnotations);
        Mockito.doReturn(codeHighlights).when(highlighterRepository).save(any(CodeHighlights.class));

        Set<CodeHighlights> chs = highlighterService.getAllHighlights(username, codeSnippet, chrl);

        for(CodeHighlights ch: chs){
            Assertions.assertTrue(ch.getAnnotated_by().equals(chr.getAnnotated_by()));
        }
    }

    @Test
    public void makeHighlightResponseTest(){
        CodeAnnotations codeAnnotations = new CodeAnnotations();
        CodeHighlights codeHighlights = new CodeHighlights();
        codeHighlights.setAnnotation(codeAnnotations);
        codeHighlights.setSpan_start_id("1");
        codeHighlights.setSpan_end_id("5");
        codeHighlights.setAnnotated_by("johndoe");

        AnnotationResponse annotationResponse = new AnnotationResponse();
        Mockito.when(annotationService.makeAnnotationResponse(codeHighlights.getAnnotation()))
                .thenReturn(annotationResponse);

        CodeHighlightResponse chr = highlighterService.makeHighlightResponse(codeHighlights);

        Assertions.assertTrue(chr.getAnnotated_by().equals(codeHighlights.getAnnotated_by()));
    }

    @Test
    public void makeAllHighlightResponseTest(){
        CodeAnnotations codeAnnotations = new CodeAnnotations();
        CodeHighlights codeHighlights = new CodeHighlights();
        codeHighlights.setAnnotation(codeAnnotations);
        codeHighlights.setSpan_start_id("1");
        codeHighlights.setSpan_end_id("5");
        codeHighlights.setAnnotated_by("johndoe");

        Set<CodeHighlights> set = new HashSet<>(){{
            add(codeHighlights);
        }};

        CodeHighlightResponse chr = new CodeHighlightResponse();

        HighlighterService highlighterServiceSpy = Mockito.spy(highlighterService);
        Mockito.doReturn(chr)
                .when(highlighterServiceSpy)
                .makeHighlightResponse(codeHighlights);

        Set<CodeHighlightResponse> responses = highlighterServiceSpy.makeAllHighlightResponse(set);
        Assertions.assertTrue(responses.size()==1);
    }

    @Test
    public void getHighlightsSetPageTest(){
        CodeSnippet codeSnippet = new CodeSnippet();
        int page = 1;
        Long count = 1L;
        String hstring = "1234";
        long id = 1234L;
        PageRequest pageRequest = PageRequest.of(page, MiscEnums.NumberOfPages.getValue());
        Mockito.doReturn(count).when(highlighterRepository).findByCodeSnippetCount(codeSnippet);

        Mockito.doReturn(hstring).when(highlighterRepository)
                .findByCodeSnippet(codeSnippet, pageRequest);

        CodeHighlights codeHighlights = new CodeHighlights();
        Mockito.doReturn(Optional.of(codeHighlights))
                .when(highlighterRepository)
                .findById(id);

        CodeHighlightResponse chr = new CodeHighlightResponse();
        HighlighterService highlighterServiceSpy = Mockito.spy(highlighterService);
        Mockito.doReturn(chr)
                .when(highlighterServiceSpy)
                .makeHighlightResponse(codeHighlights);

        Page<List<CodeHighlightResponse>> response = highlighterServiceSpy.getHighlightsSetPage(codeSnippet,page);

        Assertions.assertTrue(response instanceof PageImpl<List<CodeHighlightResponse>>);
    }

}
