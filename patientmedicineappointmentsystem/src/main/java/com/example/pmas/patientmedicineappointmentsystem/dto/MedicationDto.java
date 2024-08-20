package com.example.pmas.patientmedicineappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicationDto {

    private Long id;

    // Ensure patient data is removed in the mvc application.
//    private PatientDto patientDto;

    // Ensure patient data is removed in the mvc application.
    private Long patientId;

    private String medicine;

    private String frequency;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date startDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date endDate;

    private String notes;
}
