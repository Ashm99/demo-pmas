package com.example.pmas.patientmedicineappointmentsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePatientDto {

    @NotBlank(message = "First name of a patient cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can only contain letters and spaces")
    private String firstName;

    @NotBlank(message = "Last name of a patient cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can only contain letters and spaces")
    private String lastName;

    @Email(message = "Enter a valid email address.")
    private String email;

//    private String username;

    @Pattern(regexp = "\\d{10}", message = "Mobile number must be numeric and exactly 10 digits long.")
    private String mobile;

    @NotBlank(message = "Address of a patient cannot be blank.")
    private String address;
}
