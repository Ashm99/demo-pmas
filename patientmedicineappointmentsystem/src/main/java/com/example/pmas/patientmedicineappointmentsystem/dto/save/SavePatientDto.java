package com.example.pmas.patientmedicineappointmentsystem.dto.save;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SavePatientDto {

    @NotBlank(message = "First name of a patient cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can only contain letters and spaces")
    private String firstName;

    @NotBlank(message = "Last name of a patient cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name can only contain letters and spaces")
    private String lastName;

    @Email(message = "Enter a valid email address.")
    @NotBlank(message = "Email cannot be blank.")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Mobile number must be numeric and exactly 10 digits long.")
    private String mobile;

    private String password;

    @NotBlank(message = "Address of a patient cannot be blank.")
    private String address;

    private int age;

    private String gender;

    private String bloodGroup;

    // Emergency Contact Details
    private String emergencyContactName;

    private String emergencyContactMobile;

    private String emergencyContactRelation;

    // Medical History
    private String previousDiagnoses;

    private String surgeries;

    private String allergies;

    private String vaccinationHistory;

    // Lifestyle conditions
    private Boolean isSmoker;

    private Boolean consumesAlcohol;

    @Override
    public String toString(){
        return new String ("First name: " + firstName + "\n"
                + "Last name: " + lastName + "\n"
                + "Email: " + email + "\n"
                + "Mobile: " + mobile + "\n"
                + "Password: " + password + "\n"
                + "Address: " + address + "\n"
                + "Age: " + age + "\n"
                + "Gender: " + gender + "\n"
                + "Blood group: " + bloodGroup + "\n"
                + "Emergency Contact Name: " + emergencyContactName + "\n"
                + "Emergency Contact Mobile: " + emergencyContactMobile + "\n"
                + "Emergency Contact Relation: " + emergencyContactRelation + "\n"
                + "Previous Diagnoses: " + previousDiagnoses + "\n"
                + "Surgeries: " + surgeries + "\n"
                + "Allergies: " + allergies + "\n"
                + "Vaccination History: " + vaccinationHistory + "\n"
                + "Smokes: " + isSmoker + "\n"
                + "Consumes alcohol: " + consumesAlcohol + "\n");
    }
}
