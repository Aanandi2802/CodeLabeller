package com.csci5308.codeLabeller.Repsoitory;

import com.csci5308.codeLabeller.Models.SurveyApprovalRequest;
import org.springframework.data.repository.CrudRepository;

/**
 * repository for survey approval.
 */
public interface SurveyApprovalRequestRepository extends CrudRepository<SurveyApprovalRequest, Long> {
}
