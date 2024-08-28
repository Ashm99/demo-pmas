package com.example.pmas.patientmedicineappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class MedicationDto {

    private Long id;

    private Long patientId;

    private String medicine;

    private String frequency;

    private String startDate;

    private String endDate;

    private String notes;
}
