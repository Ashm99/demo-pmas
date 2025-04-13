package com.example.pmas.patientmedicineappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class AppointmentDto {

    private Long id;

    private PatientDto patientDto;

    private DoctorDto doctorDto;

    private String appointmentDateTime;

    private String createdAt;
}
