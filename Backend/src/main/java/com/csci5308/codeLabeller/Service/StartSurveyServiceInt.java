package com.csci5308.codeLabeller.Service;

import com.csci5308.codeLabeller.Models.CodeSnippet;
import com.csci5308.codeLabeller.Models.DTO.StartSurveyResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface StartSurveyServiceInt {

    Page<StartSurveyResponse> startTheSurvey(Long surveyID, int page);

}
