package com.example.pmas.patientmedicineappointmentsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String password;

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
