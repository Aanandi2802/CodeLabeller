package com.csci5308.codeLabeller.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @ToString @AllArgsConstructor
public class Annotator {
    @Id
    private String username;
    @OneToMany
    private List<CodeSurvey> approvedCodeSurvey;
    @OneToMany
    private List<CodeSurvey> pendingApprovalsSurveys;
}
