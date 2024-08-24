package com.example.pmas.patientmedicineappointmentsystem.dto.creation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMedicationDto {

    @Positive(message="Patient id should be a valid number.")
    private Long patientId;

    @NotBlank(message = "Medicine should not be blank.")
    private String medicine;

    @NotBlank(message = "In frequency field, enter how much intake is required per day.")
    private String frequency;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @NotBlank(message = "Enter further notes or comments in the notes field. Eg. If the medicine is to be taken after food, or before sleep, etc")
    private String notes;
}
