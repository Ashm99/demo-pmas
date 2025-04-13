package com.example.pmas.patientmedicineappointmentsystem.controller;

import com.example.pmas.patientmedicineappointmentsystem.controller.restful.DoctorController;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Doctor doctor;
    private DoctorDto doctorDto;
    private SaveDoctorDto saveDoctorDto;

    @BeforeEach
    public void setup(){
        doctor = new Doctor(
                1L,
                "John",
                "Doe",
                "Male",
                "7777777777",
                "johndoe@gmail.com",
                "General",
                5,
                "MBBS",
                "English",
                "USA"
        );
        doctorDto = new DoctorDto(
                1L,
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
    @DisplayName(value = "JUnit test method for getAll doctors method.")
    @Test
    public void givenDoctorObject_whenGetAllDoctor_thenReturnDoctorList() throws Exception {
        // Arrange
        given(doctorService.getAllDoctors())
                .willReturn(List.of(doctorDto));

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/doctors/getAll") // Api call
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(doctorDto))))
                .andExpect(jsonPath("$.size()", Matchers.is(1))); // Matchers.is() from Hamcrest
    }

    @WithMockUser(roles = {"PATIENT"}) // Although here role is unnecessary
    @DisplayName(value = "JUnit test method for get doctor by id method.")
    @Test
    public void givenDoctorObject_whenGetDoctorById_thenReturnDoctorList() throws Exception {
        // Arrange
        given(doctorService.getDoctorById(doctor.getId()))
                .willReturn(doctorDto);

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/doctors/get/" + doctor.getId()) // Api call
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(doctorDto)));
    }

    @WithMockUser(roles = {"PATIENT"}) // Although here role is unnecessary
    @DisplayName(value = "JUnit test method for save doctor method.")
    @Test
    public void givenDoctorObject_whenAddDoctor_thenReturnSavedDoctorObject() throws Exception {
        // Arrange
        given(doctorService.addDoctor(any(SaveDoctorDto.class)))
//                .willAnswer(invocation -> invocation.getArgument(0));
//                Instead of using willReturn we are using willAnswer because this is a controller method and
                .willReturn(doctorDto);
//                Used willReturn as willlAnswer betrayed :-(

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/doctors/add") // Api call
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDoctorDto))
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(doctorDto)));
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test method for addAllDoctors method.")
    @Test
    public void givenListOfDoctors_whenAddAllDoctors_thenReturnSavedDoctorList() throws Exception {
        List<SaveDoctorDto> saveDoctorDtoList = List.of(saveDoctorDto);
        List<DoctorDto> savedDoctorDtoList = List.of(doctorDto);

        // Arrange
        given(doctorService.addAllDoctors(saveDoctorDtoList)).willReturn(savedDoctorDtoList);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/doctors/addAll")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDoctorDtoList))
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedDoctorDtoList)))
                .andExpect(jsonPath("$.size()", Matchers.is(1))); // Matchers.is() from Hamcrest
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test method for updateDoctor method.")
    @Test
    public void givenDoctorObject_whenUpdateDoctor_thenReturnUpdatedDoctor() throws Exception {
        Long doctorId = doctorDto.getId();

        // Arrange
        given(doctorService.updateDoctor(eq(doctorId), any(SaveDoctorDto.class))).willReturn(doctorDto);

        // Act
        ResultActions resultActions = mockMvc.perform(
                put("/doctors/update/" + doctorId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveDoctorDto))
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(doctorDto)));
    }

    @WithMockUser(roles = {"PATIENT"})
    @DisplayName("JUnit test method for deleteDoctorById method.")
    @Test
    public void givenDoctorId_whenDeleteDoctorById_thenReturnDeletionMessage() throws Exception {
        Long doctorId = doctorDto.getId();

        // Act
        ResultActions resultActions = mockMvc.perform(
                delete("/doctors/delete/" + doctorId)
                        .with(csrf())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor with ID - " + doctorId + " removed from the database."));

        verify(doctorService, times(1)).deleteDoctorById(doctorId);
        // Verifying that the service's delete method was called
    }

}
