package com.csci5308.codeLabeller.Controller;

import com.csci5308.codeLabeller.Models.CodeHighlights;
import com.csci5308.codeLabeller.Models.DTO.*;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import com.csci5308.codeLabeller.Service.AnnotationService;
import com.csci5308.codeLabeller.Service.SnippetService;
import com.csci5308.codeLabeller.Service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@EnableMethodSecurity
@RequestMapping("/admin/")
public class AdminController {

    @Autowired
    SurveyService surveyService;
    @Autowired
    AnnotationService annotationService;
    @Autowired
    SnippetService snippetService;

    @Autowired
    UserDetailsManager jdbcUserDetailsManager;

    /**
     * This url returns all the annotations created by Admin
     * It expects admin username and surveyId as path variables for the call
     * @param username
     * @param surveyId
     * @return List of AnnotationResponse
     */
    @GetMapping("{admin_username}/survey/{survey_id}/annotation/all/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AnnotationResponse> getAllAnnotations(@PathVariable("admin_username") String username,
                                                  @PathVariable("survey_id") Long surveyId){
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        return annotationService.getAllAnnotations(username,surveyId);
    }
    /**
     * This url returns annotation based on the Id passed by Admin
     * It expects admin username, annotationId and surveyId as path variables for the call
     * @param username
     * @param surveyId
     * @param id annotationId
     * @return AnnotationResponse
     */
    @GetMapping("{admin_username}/survey/{survey_id}/annotation/{id}/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AnnotationResponse getAnnotation(@PathVariable("admin_username") String username,
                                        @PathVariable("survey_id") Long surveyId,
                                        @PathVariable("id") Long id){
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        return annotationService.getAnnotation(username,surveyId,id);
    }

    /**
     * This url returns all the responses done by different annotators, all annotations by single annotator clubs on single page.
     * It expects admin username, surveyId, snippetId and page number as path variables for the call
     * @param username
     * @param surveyId
     * @param snippetId
     * @param page
     * @return Page with list of CodeHighlightResponse
     */
    @GetMapping("{admin_username}/survey/{survey_id}/snippet/{id}/highlightResponses/start/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<List<CodeHighlightResponse>> startSnippetHighlightResponses(@PathVariable("admin_username") String username,
                                                               @PathVariable("survey_id") Long surveyId,
                                                               @PathVariable("id") Long snippetId,
                                                               @RequestParam(value = "page", defaultValue="0") int page){
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        return snippetService.startSnippetHighlightsPaginationByAnnotator(username,surveyId,snippetId,page);
    }

    /**
     * This url returns all the annotations tagged to a particular snippet
     * It expects admin username and surveyId as path variables for the call
     * @param username
     * @param surveyId
     * @param id snippetId
     * @return List of AnnotationResponse
     */
    @GetMapping("{admin_username}/survey/{survey_id}/snippet/{id}/taggedAnnotations/all/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AnnotationResponse> getSnippetTaggedAnnotations(@PathVariable("admin_username") String username,
                                      @PathVariable("survey_id") Long surveyId,
                                      @PathVariable("id") Long id){
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        return snippetService.getSnippetTaggedAnnotations(username,surveyId,id);
    }

    /**
     * This url returns all the snippets created by Admin
     * It expects admin username and surveyId as path variables for the call
     * @param username
     * @param surveyId
     * @return List of SnippetResponse
     */
    @GetMapping("{admin_username}/survey/{survey_id}/snippet/all/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SnippetResponse> getAllSnippets(@PathVariable("admin_username") String username,
                                                @PathVariable("survey_id") Long surveyId){
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        return snippetService.getAllSnippets(username,surveyId);
    }

    /**
     * This url returns snippet
     * It expects admin username, snippetId and surveyId as path variables for the call
     * @param username
     * @param surveyId
     * @param id snippetId
     * @return SnippetResponse
     */
    @GetMapping("{admin_username}/survey/{survey_id}/snippet/{id}/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SnippetResponse getSnippet(@PathVariable("admin_username") String username,
                                            @PathVariable("survey_id") Long surveyId,
                                            @PathVariable("id") Long id){
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        return snippetService.getSnippet(username,surveyId,id);
    }

    /**
     * This url returns all the Surveys created by Admin
     * It expects admin username as path variables for the call
     * @param username
     * @return List of SurveyResponse
     */
    @GetMapping("{admin_username}/survey/all/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SurveyResponse> getAllSurveys(@PathVariable("admin_username") String username){
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        return surveyService.getAllSurveys(username);
    }

    /**
     * This url returns survey by surveyId
     * It expects admin username and surveyId as path variables for the call
     * @param username
     * @param id surveyId
     * @return SurveyReponse
     */
    @GetMapping("{admin_username}/survey/{id}/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SurveyResponse getSurvey(@PathVariable("admin_username") String username, @PathVariable("id") Long id){
        return surveyService.getSurvey(id);
    }

    /**
     * This url returns status of saving of survey
     * It expects admin username and object of AdminSnippetsAnnotationsDTO as payload for call
     * @param username
     * @param asaDTO
     * @return Status of saving survey
     */
    @PostMapping("{admin_username}/survey/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> saveSurvey(@PathVariable("admin_username") String username, @RequestBody AdminSnippetsAnnotationsDTO asaDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        asaDTO.setUsername(authentication.getName());
        CodeSurvey survey = surveyService.createSurvey(asaDTO);
        annotationService.createAnnotations(asaDTO, survey);
        snippetService.createSnippets(asaDTO, survey);
        return ResponseEntity.ok().build();
    }


}

