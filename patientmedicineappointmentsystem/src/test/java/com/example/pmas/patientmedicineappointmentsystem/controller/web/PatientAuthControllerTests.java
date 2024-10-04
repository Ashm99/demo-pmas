package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SavePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientAuthController.class)
public class PatientAuthControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PatientService patientService;

    private SavePatientDto savePatientDto;

    @BeforeEach
    public void setup(){
        savePatientDto = new SavePatientDto(
                "John",
                "Doe",
                "jdoe@gmail.com",
                "9876543210",
                "John@2024",
                "USA",
                25,
                "Male",
                "O+",
                "Johanna",
                "1234567890",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true
        );
    }


    @WithMockUser(roles = {"PATIENT"}) // Although here role is unnecessary
    @DisplayName("JUnit test method for patient login page")
    @Test
    public void givenNothing_whenLogin_thenReturnLoginPage() throws Exception {
        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/patients/login")
        ); // api call

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk()) // By default it is 200 OK
                .andExpect(view().name("login"));
                // telling that this view will be returned
    }

    @WithMockUser(roles = {"PATIENT"}) // Although here role is unnecessary
    @DisplayName("JUnit test method for register view")
    @Test
    public void givenNothing_whenRegister_thenReturnRegisterPage() throws Exception {
        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/patients/register")
        );
        // api call

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk()) // By default it is 200 OK
                .andExpect(view().name("register"))
                // telling that this view will be returned
                .andExpect(model().attributeExists("newPatient"));
    }


    @WithMockUser
    @DisplayName("JUnit test method for saving a patient")
    @Test
    public void givenPatientObject_whenSavePatient_thenRedirectWithSuccess() throws Exception {
        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/patients/savePatient")
                        .with(csrf()) // Sending this csrf token is essential because:
                        // Spring Security includes CSRF protection by default for state-changing requests (like POST, PUT, DELETE) to prevent attacks
                        // When the url is accessed by an user, the server generates a token for this.
                        // So, here for the test, we need to do the same ourselves
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", savePatientDto.getFirstName())
                        .param("lastName", savePatientDto.getLastName())
                        .param("email", savePatientDto.getEmail())
                        .param("mobile", savePatientDto.getMobile())
                        .param("password", savePatientDto.getPassword())
                        .param("address", savePatientDto.getAddress())
                        .param("age", String.valueOf(savePatientDto.getAge()))
                        .param("gender", savePatientDto.getGender())
                        .param("bloodGroup", savePatientDto.getBloodGroup())
                        .param("emergencyContactName", savePatientDto.getEmergencyContactName())
                        .param("emergencyContactMobile", savePatientDto.getEmergencyContactMobile())
                        .param("emergencyContactRelation", savePatientDto.getEmergencyContactRelation())
                        .param("previousDiagnoses", savePatientDto.getPreviousDiagnoses())
                        .param("surgeries", savePatientDto.getSurgeries())
                        .param("allergies", savePatientDto.getAllergies())
                        .param("vaccinationHistory", savePatientDto.getVaccinationHistory())
                        .param("isSmoker", String.valueOf(savePatientDto.getIsSmoker()))
                        .param("consumesAlcohol", String.valueOf(savePatientDto.getConsumesAlcohol()))
    );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/patients/register?success"));

        // Verify service method was called
        verify(patientService, times(1)).addPatient(savePatientDto);
    }

    @WithMockUser
    @DisplayName("JUnit test method for saving a patient with existing mobile")
    @Test
    public void givenPatientObject_whenMobileExists_thenRedirectWithError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Mobile already exists")).when(patientService).addPatient(any(SavePatientDto.class));

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/patients/savePatient")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", savePatientDto.getFirstName())
                        .param("lastName", savePatientDto.getLastName())
                        .param("email", savePatientDto.getEmail())
                        .param("mobile", savePatientDto.getMobile())
                        .param("password", savePatientDto.getPassword())
                        .param("address", savePatientDto.getAddress())
                        .param("age", String.valueOf(savePatientDto.getAge()))
                        .param("gender", savePatientDto.getGender())
                        .param("bloodGroup", savePatientDto.getBloodGroup())
                        .param("emergencyContactName", savePatientDto.getEmergencyContactName())
                        .param("emergencyContactMobile", savePatientDto.getEmergencyContactMobile())
                        .param("emergencyContactRelation", savePatientDto.getEmergencyContactRelation())
                        .param("previousDiagnoses", savePatientDto.getPreviousDiagnoses())
                        .param("surgeries", savePatientDto.getSurgeries())
                        .param("allergies", savePatientDto.getAllergies())
                        .param("vaccinationHistory", savePatientDto.getVaccinationHistory())
                        .param("isSmoker", String.valueOf(savePatientDto.getIsSmoker()))
                        .param("consumesAlcohol", String.valueOf(savePatientDto.getConsumesAlcohol()))
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/patients/register?mobileAlreadyExists"));
    }
}
