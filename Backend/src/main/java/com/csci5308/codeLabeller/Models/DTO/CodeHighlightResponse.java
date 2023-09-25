package com.csci5308.codeLabeller.Models.DTO;

import com.csci5308.codeLabeller.Models.CodeAnnotations;
import com.csci5308.codeLabeller.Models.CodeHighlights;
import com.csci5308.codeLabeller.Models.CodeSnippet;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class CodeHighlightResponse extends CodeHighlights {
    private AnnotationResponse annotationResponse;

}
