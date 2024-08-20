package com.example.pmas.patientmedicineappointmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    private String frequency;

    private Date startDate;

    private Date endDate;

    private String notes;
}
