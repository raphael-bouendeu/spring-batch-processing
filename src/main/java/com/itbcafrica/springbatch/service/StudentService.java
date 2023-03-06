/*
package com.itbcafrica.springbatch.service;

import com.itbcafrica.springbatch.model.StudentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


@Service
public class StudentService {
    List<StudentResponse> responses;

    public List<StudentResponse> restCallToGetStudents() {
        RestTemplate restTemplate = new RestTemplate ();
        StudentResponse[] studentResponses = restTemplate.getForObject ( "http://localhost:8081/api/v1/students", StudentResponse[].class );
        responses =
                stream ( studentResponses ).collect ( Collectors.toList () );
        return responses;
    }

    public StudentResponse getStudent() {
        if (responses == null) {
            restCallToGetStudents ();
        }
        if (responses != null && !responses.isEmpty ()) {
            return responses.remove ( 0 );
        }
        return null;
    }
}
*/
