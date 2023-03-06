package com.itbcafrica.springbatch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@XmlRootElement(name = "student")
public class StudentXml {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
