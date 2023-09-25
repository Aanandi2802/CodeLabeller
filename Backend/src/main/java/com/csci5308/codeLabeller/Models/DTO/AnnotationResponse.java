package com.csci5308.codeLabeller.Models.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class AnnotationResponse {
    private Long annotationID;
    private String name;
}
