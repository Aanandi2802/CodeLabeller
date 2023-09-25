package com.csci5308.codeLabeller.Repsoitory;

import com.csci5308.codeLabeller.Models.Annotator;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * repository for annotator.
 */
@Repository
public interface AnnotatorRepository extends CrudRepository<Annotator, String> {
}
