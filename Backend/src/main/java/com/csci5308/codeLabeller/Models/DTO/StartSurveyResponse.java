package com.csci5308.codeLabeller.Models.DTO;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class StartSurveyResponse {
    private Long snippetId;
    private byte[] snippetText;
    private List<AnnotationResponse> surveyAnnotationList;
}
