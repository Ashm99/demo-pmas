package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.service.DoctorService;
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

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorWebController.class)
public class DoctorWebControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    private SaveDoctorDto saveDoctorDto;

    @BeforeEach
    public void setup(){
        saveDoctorDto = new SaveDoctorDto(
                "John",
                "Smith",
                "Male",
                "9988776655",
                "jsmith@gmail.com",
                "General",
                5,
                "MBBS",
                "English",
                "USA"
        );
    }

    @WithMockUser(roles = {"PATIENT"}) // Although here role is unnecessary
    @DisplayName("JUnit test method for register view")
    @Test
    public void givenNothing_whenRegister_thenReturnRegisterPage() throws Exception {
        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/doctors/register")
        );
        // api call

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk()) // By default it is 200 OK
                .andExpect(view().name("doctor-register"))
                // telling that this view will be returned
                .andExpect(model().attributeExists("newDoctor"));
    }


    @WithMockUser
    @DisplayName("JUnit test method for saving a doctor")
    @Test
    public void givenDoctorObject_whenSaveDoctor_thenRedirectWithSuccess() throws Exception {
        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/doctors/saveDoctor")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", saveDoctorDto.getFirstName())
                .param("lastName", saveDoctorDto.getLastName())
                .param("gender", saveDoctorDto.getGender())
                .param("mobile", saveDoctorDto.getMobile())
                .param("email", saveDoctorDto.getEmail())
                .param("speciality", saveDoctorDto.getSpeciality())
                .param("experienceInYears", String.valueOf(saveDoctorDto.getExperienceInYears()))
                .param("qualifications", saveDoctorDto.getQualifications())
                .param("languagesSpoken", saveDoctorDto.getLanguagesSpoken())
                .param("officeAddress", saveDoctorDto.getOfficeAddress()));

        // Assert
        resultActions.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/doctors/register?success"));

        // Verify service method was called
        verify(doctorService, times(1)).addDoctor(saveDoctorDto);
    }

    @WithMockUser
    @DisplayName("JUnit test method for saving a doctor with existing mobile")
    @Test
    public void givenDoctorObject_whenMobileExists_thenRedirectWithError() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Mobile already exists")).when(doctorService).addDoctor(any(SaveDoctorDto.class));

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/doctors/saveDoctor")
                        .with(csrf()) // Sending this csrf token is essential because:
                        // Spring Security includes CSRF protection by default for state-changing requests (like POST, PUT, DELETE) to prevent attacks
                        // When the url is accessed by an user, the server generates a token for this.
                        // So, here for the test, we need to do the same ourselves
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", saveDoctorDto.getFirstName())
                .param("lastName", saveDoctorDto.getLastName())
                .param("gender", saveDoctorDto.getGender())
                .param("mobile", saveDoctorDto.getMobile())
                .param("email", saveDoctorDto.getEmail())
                .param("speciality", saveDoctorDto.getSpeciality())
                .param("experienceInYears", String.valueOf(saveDoctorDto.getExperienceInYears()))
                .param("qualifications", saveDoctorDto.getQualifications())
                .param("languagesSpoken", saveDoctorDto.getLanguagesSpoken())
                .param("officeAddress", saveDoctorDto.getOfficeAddress()));

        // Assert
        resultActions.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/doctors/register?mobileAlreadyExists"));
    }
}
