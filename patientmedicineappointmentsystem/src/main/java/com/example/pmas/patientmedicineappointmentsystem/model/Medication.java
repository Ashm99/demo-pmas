package com.example.pmas.patientmedicineappointmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Since one patient can have multiple appointments
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    // JoinColumn - |||r to constraint query.....foreign key (patient_id) references patient(id)
    private Patient patient;

    private String medicine;

    private String dosage;

    private String frequency;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate prescriptionDate;

    private LocalDate updatedDate;

    private String notes;
}
