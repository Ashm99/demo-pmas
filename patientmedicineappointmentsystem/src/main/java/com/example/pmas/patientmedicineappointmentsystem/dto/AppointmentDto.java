package com.example.pmas.patientmedicineappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDto {

    private Long id;

    private PatientDto patientDto;

    private DoctorDto doctorDto;

    private Instant appointmentDateTime;

    private Instant createdAt;
}
