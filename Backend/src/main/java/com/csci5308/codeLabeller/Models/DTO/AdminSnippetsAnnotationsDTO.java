package com.csci5308.codeLabeller.Models.DTO;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class AdminSnippetsAnnotationsDTO {
    private String username;
    private String surveyName;
    private String surveyLanguage;
    private Long surveyThreshold = (long)0;

    private List<byte[]> snippets;
    private List<String> annotations;
}
