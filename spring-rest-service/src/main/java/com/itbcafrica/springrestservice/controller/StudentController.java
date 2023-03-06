package com.itbcafrica.springrestservice.controller;


import com.itbcafrica.springrestservice.dto.StudentResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    @GetMapping("/students")
    public List<StudentResponse> students() {
        return Arrays.asList (

                new StudentResponse ( 1L, "John", "Bernard", "raphael@yahoo.de" ),
                new StudentResponse ( 2L, "John2", "Bernard2", "raphael2@yahoo.de" ),
                new StudentResponse ( 3L, "John3", "Bernard3", "raphael3@yahoo.de" ),
                new StudentResponse ( 4L, "John4", "Bernard4", "raphael4@yahoo.de" )

        );
    }
}
