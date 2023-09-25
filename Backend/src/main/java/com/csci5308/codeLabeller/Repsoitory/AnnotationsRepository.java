package com.csci5308.codeLabeller.Repsoitory;

import com.csci5308.codeLabeller.Models.CodeAnnotations;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * repository for annotations.
 */
@Repository
public interface AnnotationsRepository extends CrudRepository<CodeAnnotations, Long> {

}
