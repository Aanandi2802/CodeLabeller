package com.csci5308.codeLabeller.Models.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class AnnotatorHighlightTagResponse {

    private Long snippetId;

    private List<AnnotationResponse> annotationResponseList;
    private List<CodeHighlightResponse> codeHighlightResponseList;


}
