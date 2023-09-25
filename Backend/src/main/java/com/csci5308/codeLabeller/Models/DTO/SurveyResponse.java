package com.csci5308.codeLabeller.Models.DTO;

import com.csci5308.codeLabeller.Models.CodeAnnotations;
import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class SurveyResponse {
    private Long surveyID;
    private String surveyName;
    private String surveyLanguage;
    private Long surveyThreshold;
    private Set<AnnotationResponse> annotationResponseSet;
    private Set<SnippetResponse> snippetResponseSet;
}
