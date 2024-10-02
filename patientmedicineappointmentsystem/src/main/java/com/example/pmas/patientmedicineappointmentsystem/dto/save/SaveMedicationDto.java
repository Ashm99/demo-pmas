package com.example.pmas.patientmedicineappointmentsystem.dto.save;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class SaveMedicationDto {

    @Pattern(regexp = "^\\d+$", message = "Enter a valid patient id. Expecting a positive number.")
    private String patientId; // To save into the db

    @NotBlank(message = "Medicine should not be blank.")
    private String medicine;

    private String dosage;

    @NotBlank(message = "In frequency field, enter how much intake is required per day.")
    private String frequency;

    @NotBlank(message = "Medicine current status is mandatory.")
    private String status;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Enter medication start date in <yyyy-MM-dd> format.")
    private String startDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Enter medication end date in <yyyy-MM-dd> format.")
    private String endDate;

    private String notes;

    @Override
    public String toString() {
        return "SaveMedicationDto{" +
                "\n\tpatientId='" + patientId + '\'' +
                ", \n\tmedicine='" + medicine + '\'' +
                ", \n\tdosage='" + dosage + '\'' +
                ", \n\tfrequency='" + frequency + '\'' +
                ", \n\tstatus='" + status + '\'' +
                ", \n\tstartDate='" + startDate + '\'' +
                ", \n\tendDate='" + endDate + '\'' +
                ", \n\tnotes='" + notes + '\'' +
                "\n}";
    }
}
