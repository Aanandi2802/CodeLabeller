package com.csci5308.codeLabeller.Models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.User;

import java.util.Date;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CodeHighlights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long highlightId;

    @NonNull
    private String span_start_id;

    @NonNull
    private String span_end_id;

    @NonNull
    private String annotated_by;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "codeSnippetId")
    private CodeSnippet codeSnippet;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "annotationID")
    private CodeAnnotations annotation;

}
