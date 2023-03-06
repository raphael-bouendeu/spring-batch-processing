package com.itbcafrica.springbatch.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
