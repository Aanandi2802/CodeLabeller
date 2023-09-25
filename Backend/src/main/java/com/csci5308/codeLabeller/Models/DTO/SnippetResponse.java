package com.csci5308.codeLabeller.Models.DTO;

import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class SnippetResponse {
    private Long snippetID;
    private byte[] snippetText;
    private Set<AnnotationResponse> taggedAnnotations;

    private Set<CodeHighlightResponse> highlightResponses;
}
