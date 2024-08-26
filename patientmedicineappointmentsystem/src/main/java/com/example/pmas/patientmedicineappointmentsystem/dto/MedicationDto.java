package com.example.pmas.patientmedicineappointmentsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicationDto {

    @NotNull(message = "Medication id cannot be null. Enter valid value.")
    private Long id;

    @Positive(message = "Patient id should be a valid number.")
    private Long patientId;

    @NotBlank(message = "Medicine should not be blank.")
    private String medicine;

    @NotBlank(message = "In frequency field, enter how much intake is required per day.")
    private String frequency;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Enter medication start date in <yyyy-MM-dd> format.")
    private String startDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Enter medication end date in <yyyy-MM-dd> format.")
    private String endDate;

    @NotBlank(message = "Enter further notes or comments in the notes field. Eg. If the medicine is to be taken after food, or before sleep, etc")
    private String notes;
}
