package com.example.pmas.patientmedicineappointmentsystem.dto.save;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveDoctorDto {

    @NotBlank(message = "First name of a doctor cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can only contain letters and spaces")
    private String firstName;

    @NotBlank(message = "Last name of a doctor cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can only contain letters and spaces")
    private String lastName;

    @NotBlank(message = "Gender cannot be blank.")
    private String gender;

    @NotBlank(message = "Mobile number is a mandatory field")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number should be 10 digits long.")
    private String mobile;

    private String email;

    @NotBlank(message = "Doctor speciality cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can only contain letters and spaces")
    private String speciality;

    private int experienceInYears;

    private String qualifications;

    private String languagesSpoken;

    private String officeAddress;
}
