package com.itbcafrica.springbatch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentJson {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
