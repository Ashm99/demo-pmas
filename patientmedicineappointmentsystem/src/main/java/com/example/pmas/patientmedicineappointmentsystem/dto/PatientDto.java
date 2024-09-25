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

    private int age;

    private String gender;

    private String bloodGroup;

    // Emergency Contact Details
    private String emergencyContactName;

    private String emergencyContactMobile;

    private String emergencyContactRelation;

    // Medical History
    private String previousDiagnoses;

    private String surgeries;

    private String allergies;

    private String vaccinationHistory;

    // Lifestyle conditions
    private boolean isSmoker;

    private boolean consumesAlcohol;
}
