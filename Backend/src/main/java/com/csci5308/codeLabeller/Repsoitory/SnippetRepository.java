package com.csci5308.codeLabeller.Repsoitory;

import com.csci5308.codeLabeller.Models.CodeSnippet;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnippetRepository extends CrudRepository<CodeSnippet, Long> {

    Page<CodeSnippet> findBySurvey(CodeSurvey codeSurvey, PageRequest pageRequest, Sort sort);
}
