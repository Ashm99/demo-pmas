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

    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be numeric and exactly 10 digits long.")
    private String mobile;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
            message = "Password must contain at least 8characters. " +
                    "At least one capital letter, at least one small, at least one number and at least one special character.")
    private String password;

    @NotBlank(message = "Address of a patient cannot be blank.")
    private String address;

    private int age;

    private String gender;

    private String bloodGroup;

    // Emergency Contact Details
    @NotBlank(message = "Name of the emergency contact cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Name of the emergency contact can only contain letters and spaces")
    private String emergencyContactName;

    @Pattern(regexp = "^\\d{10}$", message = "Emergency contact Mobile number must be numeric and exactly 10 digits long.")
    private String emergencyContactMobile;

    @NotBlank(message = "Emergency contact relationship cannot be blank.")
    @Pattern(regexp = "^[A-Za-z ]*$", message = "Relationship of the emergency contact can only contain letters and spaces")
    private String emergencyContactRelation;

    // Medical History
    @NotBlank(message = "Emergency contact relationship cannot be blank.")
    private String previousDiagnoses;

    @NotBlank(message = "Emergency contact relationship cannot be blank.")
    private String surgeries;

    @NotBlank(message = "Emergency contact relationship cannot be blank.")
    private String allergies;

    @NotBlank(message = "Emergency contact relationship cannot be blank.")
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
