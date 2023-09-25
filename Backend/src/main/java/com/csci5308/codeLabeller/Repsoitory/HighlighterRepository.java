package com.csci5308.codeLabeller.Repsoitory;

import com.csci5308.codeLabeller.Models.CodeHighlights;
import com.csci5308.codeLabeller.Models.CodeSnippet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * repository for highliter object.
 */
@Repository
public interface HighlighterRepository extends CrudRepository<CodeHighlights, Long> {

    /**
     * this query helps get the distinct count for highlights
     * @param codeSnippet: snippet object
     * @return: distinct count.
     */
    @Query("SELECT COUNT(DISTINCT ch.annotated_by) FROM CodeHighlights ch WHERE ch.codeSnippet=:cs")
    Long findByCodeSnippetCount(@Param("cs") CodeSnippet codeSnippet);

    /**
     * code highlights according to snippets.
     * @param codeSnippet: snippet object
     * @param pageRequest: page request object
     * @return: highlight objects.
     */
    @Query("SELECT GROUP_CONCAT(ch) FROM CodeHighlights ch WHERE ch.codeSnippet=:cs GROUP BY ch.annotated_by")
    String findByCodeSnippet(@Param("cs") CodeSnippet codeSnippet, PageRequest pageRequest);
}
