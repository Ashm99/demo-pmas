package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.service.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicationWebController.class)
public class MedicationWebControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MedicationService medicationService;
    @Mock
    private Authentication authentication;

    // Global variables
    private MedicationDto medicationDto;
    private SaveMedicationDto saveMedicationDto;
    LocalDate today = LocalDate.now();
    LocalDate dayAfterTomorrow = today.plusDays(2);

    @BeforeEach
    public void setup() {

        medicationDto = new MedicationDto(
                1L,
                1L,
                "Dolo",
                "650 mg",
                "Twice a day",
                "Complete",
                today.toString(),
                today.plusDays(3).toString(),
                today.toString(),
                today.toString(),
                "After breakfast and dinner."
        );
        saveMedicationDto = new SaveMedicationDto(
                "1",
                "Dolo",
                "650 mg",
                "Twice a day",
                "Complete",
                today.toString(),
                today.plusDays(3).toString(),
                "After breakfast and dinner."
        );
    }

    @WithMockUser
    @DisplayName("JUnit test method for the show medication list api")
    @Test
    public void givenMedications_whenGetMedicationsForUser_thenReturnUserMedicationList() throws Exception {
        // Arrange
        String username = "9999999999";
        when(authentication.getName()).thenReturn(username);
        when(medicationService.getAllMedicationByUsername(anyString())).thenReturn(List.of(medicationDto));

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/medications/list")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("medications/medication-list"))
                .andExpect(model().attributeExists("medicineList"))
                .andExpect(model().attributeExists("isEmpty"))
                .andExpect(model().attribute("medicineList", List.of(medicationDto)))
                .andExpect(model().attribute("isEmpty", false));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the show medication list api when no medications are present")
    @Test
    public void givenNoMedications_whenGetMedicationsForUser_thenReturnUserMedicationList() throws Exception {
        // Arrange
        String username = "9999999999";
        when(authentication.getName()).thenReturn(username);
        when(medicationService.getAllMedicationByUsername(anyString())).thenReturn(Collections.EMPTY_LIST);

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/medications/list")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("medications/medication-list"))
                .andExpect(model().attributeExists("isEmpty"))
                .andExpect(model().attribute("isEmpty", true));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the show medication by id api")
    @Test
    public void givenMedication_whenGetMedicationById_thenReturnMedication() throws Exception {
        // Arrange
        when(medicationService.getMedicationById(anyLong())).thenReturn(medicationDto);

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/medications/view/" + medicationDto.getId())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("medications/view-medication"))
                .andExpect(model().attributeExists("medicineDto"))
                .andExpect(model().attribute("medicineDto", medicationDto));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the add-medication api")
    @Test
    public void givenEmptyMedication_whenAddMedication_thenLoadAddMedicationPage() throws Exception {
        // Nothing to arrange

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/medications/add-medication")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("medications/add-medication"))
                .andExpect(model().attributeExists("medicineDto"))
                .andExpect(model().attribute("medicineDto", new SaveMedicationDto()));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the add api for adding a medication got from form")
    @Test
    public void givenMedication_whenAdd_thenReturnSavedMedication() throws Exception {
        // Arrange
        String username = "9999999999";
        when(authentication.getName()).thenReturn(username);
        when(medicationService.getPatientIdByUsername(anyString())).thenReturn("1");
        when(medicationService.saveMedication(any(SaveMedicationDto.class))).thenReturn(medicationDto);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/medications/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("patientId", saveMedicationDto.getPatientId())
                        .param("medicine", saveMedicationDto.getMedicine())
                        .param("dosage", saveMedicationDto.getDosage())
                        .param("frequency", saveMedicationDto.getFrequency())
                        .param("status", saveMedicationDto.getStatus())
                        .param("startDate", saveMedicationDto.getStartDate())
                        .param("endDate", saveMedicationDto.getEndDate())
                        .param("notes", saveMedicationDto.getNotes())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Medication added with medication ID: " + medicationDto.getId() + "."));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the add api for adding a medication got from form - negative case")
    @Test
    public void givenMedication_whenAdd_thenThrowError() throws Exception {
        // Arrange
        String username = "9999999999";
        when(authentication.getName()).thenReturn(username);
        when(medicationService.getPatientIdByUsername(anyString())).thenReturn("1");
        when(medicationService.saveMedication(any(SaveMedicationDto.class))).thenThrow(RuntimeException.class);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/medications/add")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("patientId", saveMedicationDto.getPatientId())
                        .param("medicine", saveMedicationDto.getMedicine())
                        .param("dosage", saveMedicationDto.getDosage())
                        .param("frequency", saveMedicationDto.getFrequency())
                        .param("status", saveMedicationDto.getStatus())
                        .param("startDate", saveMedicationDto.getStartDate())
                        .param("endDate", saveMedicationDto.getEndDate())
                        .param("notes", saveMedicationDto.getNotes())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Error while adding new medication.null"));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the update-medication api")
    @Test
    public void givenMedication_whenUpdateMedicationById_thenLoadUpdateMedicationPage() throws Exception {
        // Arrange
        when(medicationService.getMedicationById(anyLong())).thenReturn(medicationDto);

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/medications/update-medication/" + medicationDto.getId())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("medications/update-medication"))
                .andExpect(model().attributeExists("medicineDto"))
                .andExpect(model().attribute("medicineDto", medicationDto));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the update api for updating a medication got from form")
    @Test
    public void givenMedication_whenUpdate_thenReturnUpdatedMedication() throws Exception {
        // Arrange
        String username = "9999999999";
        when(authentication.getName()).thenReturn(username);
        when(medicationService.getPatientIdByUsername(anyString())).thenReturn("1");
        when(medicationService.updateMedication(anyLong(), any(LocalDate.class), any(SaveMedicationDto.class))).thenReturn(medicationDto);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/medications/update/" + medicationDto.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("prescriptionDate", LocalDate.now().toString())
                        .param("patientId", saveMedicationDto.getPatientId())
                        .param("medicine", saveMedicationDto.getMedicine())
                        .param("dosage", saveMedicationDto.getDosage())
                        .param("frequency", saveMedicationDto.getFrequency())
                        .param("status", saveMedicationDto.getStatus())
                        .param("startDate", saveMedicationDto.getStartDate())
                        .param("endDate", saveMedicationDto.getEndDate())
                        .param("notes", saveMedicationDto.getNotes())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Medication updated with medication ID: " + medicationDto.getId() + "."));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the update api for adding a medication got from form - negative case")
    @Test
    public void givenMedication_whenUpdate_thenThrowError() throws Exception {
        // Arrange
        String username = "9999999999";
        when(authentication.getName()).thenReturn(username);
        when(medicationService.getPatientIdByUsername(anyString())).thenReturn("1");
        when(medicationService.updateMedication(anyLong(), any(LocalDate.class), any(SaveMedicationDto.class))).thenThrow(RuntimeException.class);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/medications/update/" + medicationDto.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("prescriptionDate", LocalDate.now().toString())
                        .param("patientId", saveMedicationDto.getPatientId())
                        .param("medicine", saveMedicationDto.getMedicine())
                        .param("dosage", saveMedicationDto.getDosage())
                        .param("frequency", saveMedicationDto.getFrequency())
                        .param("status", saveMedicationDto.getStatus())
                        .param("startDate", saveMedicationDto.getStartDate())
                        .param("endDate", saveMedicationDto.getEndDate())
                        .param("notes", saveMedicationDto.getNotes())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Error while updating the medication-null"));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the delete medication api")
    @Test
    public void givenMedication_whenDelete_thenDeleteMedication() throws Exception {
        // Arrange
        when(medicationService.deleteMedication(anyLong())).thenReturn("");
        when(medicationService.getMedicationById(anyLong())).thenThrow(NoSuchElementException.class);

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/medications/delete/" + medicationDto.getId())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Successfully deleted medication from the database."));
    }

    @WithMockUser
    @DisplayName("JUnit test method for the delete medication api - negative case")
    @Test
    public void givenMedication_whenDelete_thenShowError() throws Exception {
        // Arrange
        when(medicationService.deleteMedication(anyLong())).thenReturn("");
        when(medicationService.getMedicationById(anyLong())).thenReturn(medicationDto);

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/medications/delete/" + medicationDto.getId())
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Error deleting the Medication."));
    }
}
