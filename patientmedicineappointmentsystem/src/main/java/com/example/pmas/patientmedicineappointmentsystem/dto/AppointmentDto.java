package com.example.pmas.patientmedicineappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class AppointmentDto {

    private Long id;

    private PatientDto patientDto;

    private DoctorDto doctorDto;

    private ZonedDateTime appointmentDateTime;

    private ZonedDateTime createdAt;
}
