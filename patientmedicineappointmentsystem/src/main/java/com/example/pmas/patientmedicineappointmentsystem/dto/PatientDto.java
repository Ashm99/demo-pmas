package com.example.pmas.patientmedicineappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class PatientDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String address;
}
