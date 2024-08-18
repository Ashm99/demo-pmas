package com.example.pmas.patientmedicineappointmentsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Since one patient can have multiple appointments
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    // JoinColumn - |||r to constraint query.....foreign key (patient_id) references patient(id)
    private Patient patient;

    @ManyToOne // Since one doctor can have multiple appointments
    @JoinColumn(name="doctor_id", referencedColumnName = "id", nullable = false)
    // JoinColumn - |||r to constraint query.....foreign key (doctor_id) references doctor(id)
    private Doctor doctor;

    @Column(nullable = false, columnDefinition = "datetime")
    private Instant appointmentDateTime;

    @Column(nullable = false, columnDefinition = "datetime default current_timestamp")
    private Instant createdAt;
}
