package com.csci5308.codeLabeller.Service;

import com.csci5308.codeLabeller.Models.DTO.AdminSnippetsAnnotationsDTO;
import com.csci5308.codeLabeller.Models.CodeAnnotations;
import com.csci5308.codeLabeller.Models.CodeSurvey;
import com.csci5308.codeLabeller.Models.DTO.AnnotationResponse;
import com.csci5308.codeLabeller.Models.DTO.SurveyResponse;
import com.csci5308.codeLabeller.Repsoitory.AnnotationsRepository;
import com.csci5308.codeLabeller.Repsoitory.SurveyRepository;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Annotation service helps:
 * create annotations
 * fetch annotations by ID or all
 * convert annotion model to DTO and vice-versa
 */
@Service
public class AnnotationService {

    @Autowired
    AnnotationsRepository annotationsRepository;
    @Autowired
    SurveyRepository surveyRepository;

    /**
     * This method helps create annotations and save it against survey
     * @param asaDTO: snippet + annotations info DTO
     * @param survey: survey requested here
     */
    public void createAnnotations(AdminSnippetsAnnotationsDTO asaDTO, CodeSurvey survey) {
        List<CodeAnnotations> annotationsList = new ArrayList<>();
        for(String name: asaDTO.getAnnotations()){
            CodeAnnotations codeAnnotation = new CodeAnnotations();
            codeAnnotation.setName(name);
            codeAnnotation.setSurvey(survey);
            annotationsList.add(codeAnnotation);
        }
        annotationsRepository.saveAll(annotationsList);
    }

    /**
     * This method converts code annotation to annotation response DTO.
     * @param ca  is an CodeAnnotations object
     * @return DTO of code annotations
     */
    public AnnotationResponse makeAnnotationResponse(CodeAnnotations ca) {
        AnnotationResponse annotationResponse = new AnnotationResponse();
        annotationResponse.setAnnotationID(ca.getAnnotationID());
        annotationResponse.setName(ca.getName());
        return annotationResponse;
    }

    /**
     * This method converts collection of annotations to collection of annotation response.
     * @param annotationSet: set of annotations
     * @return: returns the collection of DTO of annotations
     */
    public List<AnnotationResponse> makeListAnnotationResponse(Set<CodeAnnotations> annotationSet){
        List<AnnotationResponse> surveyAnnotationList = new ArrayList<>();
        for(CodeAnnotations annotations: annotationSet){
            surveyAnnotationList.add(makeAnnotationResponse(annotations));
        }

        return surveyAnnotationList;
    }

    /**
     * This method fetches collection of annotation DTO.
     * @param username: username fetched
     * @param surveyId: surveyId fetched
     * @return: list of annotations DTO.
     */
    public List<AnnotationResponse> getAllAnnotations(String username, Long surveyId) {
        CodeSurvey codeSurvey =  surveyRepository.findById(surveyId).get();
        List<AnnotationResponse> annotationResponseList = new ArrayList<>();

        for(CodeAnnotations ca: codeSurvey.getAnnotationList()){
            annotationResponseList.add(this.makeAnnotationResponse(ca));
        }
        return annotationResponseList;
    }

    /**
     * This method helps fetch the annotation DTO.
     * @param username: username fetched.
     * @param surveyId: survey ID fetched.
     * @param id: annotation ID .
     * @return: annotation DTO.
     */
    public AnnotationResponse getAnnotation(String username, Long surveyId, Long id) {
        CodeAnnotations codeAnnotation = annotationsRepository.findById(id).get();
        return this.makeAnnotationResponse(codeAnnotation);
    }

    /**
     * This method helps convert collection of annotation DTO to collection of annotation.
     * @param annotationsTag: collection of annotation DTO.
     * @return: collection of annotation.
     */
    public Set<CodeAnnotations> getAllCodeAnnotations(List<AnnotationResponse> annotationsTag){
        Set<CodeAnnotations> codeAnnotationsSet = new HashSet<>();
        for(AnnotationResponse ar: annotationsTag){
            CodeAnnotations codeAnnotation = annotationsRepository.findById(ar.getAnnotationID()).get();
            codeAnnotationsSet.add(codeAnnotation);
        }
        return codeAnnotationsSet;
    }

    /**
     * This method helps convert annotation DTO to annotation.
     * @param annotationResponse: annotation DTO
     * @return: annotation object.
     */
    public CodeAnnotations getCodeAnnotation(AnnotationResponse annotationResponse) {
        CodeAnnotations codeAnnotation = annotationsRepository.findById(annotationResponse.getAnnotationID()).get();
        return codeAnnotation;
    }


}
