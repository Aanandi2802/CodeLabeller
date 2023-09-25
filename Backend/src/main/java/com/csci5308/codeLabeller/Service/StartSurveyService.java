package com.csci5308.codeLabeller.Service;

import com.csci5308.codeLabeller.Models.CodeSnippet;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import com.csci5308.codeLabeller.Models.DTO.StartSurveyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * start the survey pagination
 */
@Service
public class StartSurveyService implements StartSurveyServiceInt {

    @Autowired
    SurveyService surveyService;
    @Autowired
    AnnotationService annotationService;

    /**
     * start the survey pagination.
     * @param surveyID: survey id.
     * @param page: page number
     * @return: page of start survey DTO.
     */
    public Page<StartSurveyResponse> startTheSurvey(Long surveyID, int page){
        return surveyService.startSurvey(surveyID,page);
    }

    /**
     * get the snippet by index for the page.
     * @param snippetSet: snippet collection.
     * @param index: index
     * @return: snippet object.
     */
    public CodeSnippet getIndexSnippet(Set<CodeSnippet> snippetSet, int index){
        List<CodeSnippet> list = new ArrayList<>(snippetSet);
        Collections.sort(list, Comparator.comparingLong(CodeSnippet::getCodeSnippetId));
        CodeSnippet snippet = list.get(index);
        return snippet;
    }
}
