package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientWebController.class)
public class PatientWebControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Mock
    private Authentication authentication;

    //Global variables
    private PatientDto patientDto;

    @BeforeEach
    public void setup() {
//        MockitoAnnotations.openMocks(this);

        patientDto = new PatientDto(
                1L,
                "John",
                "Doe",
                "jdoe@gmail.com",
                "9876543210",
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

    @WithMockUser(roles = "PATIENT")
    @DisplayName("JUnit test method for patient home page")
    @Test
    public void givenAuthenticatedUser_whenGoHome_thenReturnHomePage() throws Exception {
        // Arrange
        String username = patientDto.getMobile();
        String patientName = patientDto.getFirstName();

        // Mocking the Authentication to return a username
        when(authentication.getName()).thenReturn(username);

        // Mocking the service to return the patient's first name based on the username
        when(patientService.getPatientFirstnameByUsername(anyString())).thenReturn(patientName);

        // Act
        ResultActions resultActions = mockMvc.perform(get("/web/patients/home"));

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("patientName"))
                .andExpect(model().attribute("patientName", patientName));
    }

    @WithMockUser
    @DisplayName("JUnit test method for patient home page with no such element exception")
    @Test
    public void givenAuthentication_whenGoHome_noSuchElementException_thenReturnErrorPage() throws Exception {
        // Arrange
        when(authentication.getName()).thenReturn(patientDto.getMobile());
        when(patientService.getPatientFirstnameByUsername(anyString()))
                .thenThrow(new NoSuchElementException("Patient not found"));

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/patients/home")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"));
    }

    @WithMockUser
    @DisplayName("JUnit test method for patient profile page")
    @Test
    public void givenAuthentication_whenGetProfile_thenReturnProfilePage() throws Exception {
        // Arrange
        when(authentication.getName()).thenReturn(patientDto.getMobile());
        when(patientService.getPatientByUsername(anyString())).thenReturn(patientDto);

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/patients/profile")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("patient", patientDto));
    }

    @WithMockUser
    @DisplayName("JUnit test method for confirm logout page")
    @Test
    public void whenConfirmLogout_thenReturnConfirmLogoutPage() throws Exception {
        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/patients/confirm-logout")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("confirm-logout"));
    }
}
