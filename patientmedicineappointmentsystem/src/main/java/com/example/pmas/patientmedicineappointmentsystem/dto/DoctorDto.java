package com.example.pmas.patientmedicineappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class DoctorDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private String mobile;

    private String email;

    private String speciality;

    private int experienceInYears;

    private String qualifications;

    private String languagesSpoken;

    private String officeAddress;
}
