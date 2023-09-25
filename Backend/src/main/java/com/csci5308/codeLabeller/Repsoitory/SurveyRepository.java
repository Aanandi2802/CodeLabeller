package com.csci5308.codeLabeller.Repsoitory;

import com.csci5308.codeLabeller.Models.CodeSurvey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * survey respository.
 */
@Repository
public interface SurveyRepository extends CrudRepository<CodeSurvey, Long> {
    List<CodeSurvey> findAllByUsername(String username);
}
