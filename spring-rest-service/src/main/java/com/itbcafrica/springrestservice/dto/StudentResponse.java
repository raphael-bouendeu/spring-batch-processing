package com.itbcafrica.springrestservice.dto;

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
