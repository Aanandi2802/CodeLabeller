package com.csci5308.codeLabeller.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class CodeAnnotations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long annotationID;

    @ManyToOne
    @JoinColumn(name = "SurveyID")
    private CodeSurvey survey;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "annotation")
    private Set<CodeHighlights> highlightList;

    @ToString.Exclude
    @ManyToMany
    private Set<CodeSnippet> taggedSnippet;

}
