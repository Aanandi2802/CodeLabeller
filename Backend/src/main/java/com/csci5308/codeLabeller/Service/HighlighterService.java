package com.csci5308.codeLabeller.Service;

import com.csci5308.codeLabeller.Enums.MiscEnums;
import com.csci5308.codeLabeller.Models.CodeAnnotations;
import com.csci5308.codeLabeller.Models.CodeHighlights;
import com.csci5308.codeLabeller.Models.CodeSnippet;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import com.csci5308.codeLabeller.Models.DTO.CodeHighlightResponse;
import com.csci5308.codeLabeller.Repsoitory.HighlighterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This service helps:
 * fetch all highlights.
 * convert highlihts to DTO and vica-versa.
 * fetches a set of highlights.
 */
@Service
public class HighlighterService {

    @Autowired
    AnnotationService annotationService;

    @Autowired
    HighlighterRepository highlighterRepository;

    /**
     * This method fetches all highlights.
     * @param annotatorUsername: annotator username
     * @param codeSnippet: code snippet object.
     * @param codeHighlightResponseList: highlight DTO.
     * @return: collection of highlight.
     */
    public Set<CodeHighlights> getAllHighlights(String annotatorUsername, CodeSnippet codeSnippet, List<CodeHighlightResponse> codeHighlightResponseList){
        Set<CodeHighlights> codeHighlightsSet = new HashSet<>();

        for (CodeHighlightResponse chr: codeHighlightResponseList){
            CodeHighlights codeHighlights = new CodeHighlights();
            codeHighlights.setAnnotated_by(annotatorUsername);
            codeHighlights.setCodeSnippet(codeSnippet);
            codeHighlights.setSpan_start_id(chr.getSpan_start_id());
            codeHighlights.setSpan_end_id(chr.getSpan_end_id());
            codeHighlights.setAnnotation(annotationService.getCodeAnnotation(chr.getAnnotationResponse()));
            codeHighlights = highlighterRepository.save(codeHighlights);
            codeHighlightsSet.add(codeHighlights);
        }
        return codeHighlightsSet;
    }

    /**
     * This method converts highlight to DTO.
     * @param codeHighlights: highlight object.
     * @return: highlight DTO.
     */
    public CodeHighlightResponse makeHighlightResponse(CodeHighlights codeHighlights){
        CodeHighlightResponse chr = new CodeHighlightResponse();
        chr.setSpan_start_id(codeHighlights.getSpan_start_id());
        chr.setSpan_end_id(codeHighlights.getSpan_end_id());
        chr.setAnnotated_by(codeHighlights.getAnnotated_by());
        chr.setAnnotationResponse(annotationService.makeAnnotationResponse(codeHighlights.getAnnotation()));
        return chr;
    }

    /**
     * convert collection of highlihts to their DTO.
     * @param codeHighlightsSet: collection of highlights.
     * @return: collection of highlight response.
     */
    public Set<CodeHighlightResponse> makeAllHighlightResponse(Set<CodeHighlights> codeHighlightsSet){
        Set<CodeHighlightResponse> chrSet = new HashSet<>();
        for(CodeHighlights codeHighlights: codeHighlightsSet){
            chrSet.add(this.makeHighlightResponse(codeHighlights));
        }

        return chrSet;
    }

    /**
     * fetches collection of highlight page of size 1.
     * @param codeSnippet: snippet object.
     * @param page: page number requested.
     * @return: page of collection of highlight response.
     */
    public Page<List<CodeHighlightResponse>> getHighlightsSetPage(CodeSnippet codeSnippet, int page) {
        PageRequest pageRequest = PageRequest.of(page, MiscEnums.NumberOfPages.getValue());
        Long count = highlighterRepository.findByCodeSnippetCount(codeSnippet);
        String highlightsString = highlighterRepository.findByCodeSnippet(codeSnippet, pageRequest);
        String[] highlightIdArray = new String[]{};
        if(highlightsString!=null){
            highlightIdArray = highlightsString.split(",");
        }
        List<CodeHighlightResponse> codeHighlightResponseList = new ArrayList<>();
        for(String codeHighlightsId: highlightIdArray){
            Long id = Long.valueOf(codeHighlightsId);
            CodeHighlights codeHighlights = highlighterRepository.findById(id).get();
            CodeHighlightResponse codeHighlightResponse = this.makeHighlightResponse(codeHighlights);
            codeHighlightResponseList.add(codeHighlightResponse);
        }
        List<List<CodeHighlightResponse>> codeHighlightResponseListList = new ArrayList<>();
        codeHighlightResponseListList.add(codeHighlightResponseList);
        Page<List<CodeHighlightResponse>> codeHighlightResponseListPage = new PageImpl<>(codeHighlightResponseListList, pageRequest, count);
        return codeHighlightResponseListPage;
    }
}
