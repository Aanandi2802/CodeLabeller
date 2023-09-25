package com.csci5308.codeLabeller.Models;

import com.csci5308.codeLabeller.Enums.SurveyApprovalStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class SurveyApprovalRequest {
    @Id
    private Long id;
    private Long surveyId;
    private String annotatorUsername;
    private SurveyApprovalStatus status;
}
