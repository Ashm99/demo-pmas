package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.MedicationMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Medication;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import com.example.pmas.patientmedicineappointmentsystem.repo.MedicationRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.implementations.MedicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class MedicationServiceTests {

    @Mock
    private MedicationRepo medicationRepo;
    @Mock
    private PatientRepo patientRepo;
    @InjectMocks
    private MedicationServiceImpl medicationService;
    private Patient patient;
    private Medication medication;
    private MedicationDto medicationDto;
    private SaveMedicationDto saveMedicationDto;

    LocalDate today = LocalDate.now();
    LocalDate dayAfterTomorrow = today.plusDays(2);

    @BeforeEach
    public void setup() {

        patient = new Patient(
                1L,
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

        medication = new Medication(
                1L,
                patient,
                "Dolo",
                "650 mg",
                "Twice a day",
                "Complete",
                today,
                today.plusDays(3),
                today,
                today,
                "After breakfast and dinner."
        );

        medicationDto = new MedicationDto(
                1L,
                patient.getId(),
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
                patient.getId().toString(),
                "Dolo",
                "650 mg",
                "Twice a day",
                "Complete",
                today.toString(),
                today.plusDays(3).toString(),
                "After breakfast and dinner."
        );
    }

    @DisplayName(value = "Junit test method to save a medication")
    @Test
    public void givenMedication_whenSave_thenReturnSavedMedication() {
        // Mock Behaviour
        try (MockedStatic<MedicationMapper> medicationMapperMockedStatic = mockStatic(MedicationMapper.class)) {
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationFromSaveMedicationDto(any(), any(), any(LocalDate.class), any(SaveMedicationDto.class)))
                    .thenReturn(medication);
            given(patientRepo.findById(patient.getId())).willReturn(Optional.of(patient));
            given(medicationRepo.save(medication)).willReturn(medication);
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationDto(medication))
                    .thenReturn(medicationDto);

            // Action
            MedicationDto savedMedication = medicationService.saveMedication(saveMedicationDto);

            // Assertion
            assertThat(savedMedication).isNotNull();

            assertThat(savedMedication).isEqualTo(medicationDto);
        }
    }

    @DisplayName(value = "Junit test method to get all medications when no medication is available")
    @Test
    public void givenNoMedicationList_whenGetAllMedication_thenThrowError() {
        // Mock Behaviour
        given(medicationRepo.findAll()).willReturn(Collections.EMPTY_LIST);

        // Action and assertion
        assertThrows(NoSuchElementException.class, () -> medicationService.getAllMedication());
    }

    @DisplayName(value = "Junit test method to get all medications")
    @Test
    public void givenMedicationList_whenGetAllMedication_thenReturnMedicationList() {
        Medication medication1 = new Medication(
                2L,
                patient,
                "Eritel AM",
                "650 mg",
                "Once a day.",
                "Complete",
                today,
                today.plusDays(3),
                today,
                today,
                "After dinner."
        );

        MedicationDto medicationDto1 = new MedicationDto(
                2L,
                patient.getId(),
                "Eritel AM",
                "650 mg",
                "Once a day.",
                "Complete",
                today.toString(),
                today.plusDays(3).toString(),
                today.toString(),
                today.toString(),
                "After dinner."
        );
        List<Medication> medicationList = List.of(medication, medication1);
        when(medicationRepo.findAll()).thenReturn(medicationList);

        try (MockedStatic<MedicationMapper> medicationMapperMockedStatic = mockStatic(MedicationMapper.class)) {
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationDto(medication))
                    .thenReturn(medicationDto);
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationDto(medication1))
                    .thenReturn(medicationDto1);

            // Action
            List<MedicationDto> savedMedicationDtos = medicationService.getAllMedication();

            // Assertion
            assertThat(savedMedicationDtos).isNotEmpty();
            assertThat(savedMedicationDtos.size()).isEqualTo(2);

            assertThat(savedMedicationDtos).containsExactly(medicationDto, medicationDto1);
        }

    }

    @DisplayName(value = "Junit test method to get a medication by id when it is unavailable")
    @Test
    public void givenNoMedication_whenGetById_thenThrowError() {
        // Mock Behaviour
        given(medicationRepo.findById(any())).willReturn(Optional.empty());

        // Action and assertion
        assertThrows(NoSuchElementException.class, () -> medicationService.getMedicationById(medication.getId()));
    }

    @DisplayName(value = "Junit test method to get a medication by id")
    @Test
    public void givenMedication_whenGetById_thenReturnMedication() {
        // Mock Behaviour
        given(medicationRepo.findById(medication.getId())).willReturn(Optional.of(medication));

        try (MockedStatic<MedicationMapper> medicationMapperMockedStatic = mockStatic(MedicationMapper.class)) {
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationDto(medication))
                    .thenReturn(medicationDto);
            // Action
            MedicationDto savedMedicationDto = medicationService.getMedicationById(medication.getId());

            // Assertion
            assertThat(savedMedicationDto).isNotNull();

            assertThat(savedMedicationDto).isEqualTo(medicationDto);
        }
    }

    @DisplayName(value = "Junit test method to update a medication that is unavailable")
    @Test
    public void givenNoMedication_whenUpdate_thenThrowError() {
        // Mock behaviour
        given(medicationRepo.existsById(any())).willReturn(false);

        // Action and assertion
        assertThrows(NoSuchElementException.class, () -> medicationService.updateMedication(medication.getId(),null, saveMedicationDto));
    }

    @DisplayName(value = "Junit test method to update a medication that is unavailable for the given patient")
    @Test
    public void givenWrongMedication_whenUpdate_thenThrowError() {
        // Mock behaviour
        given(medicationRepo.existsById(any())).willReturn(true);
        given(medicationRepo.findById(medication.getId())).willReturn(Optional.of(medication));
        saveMedicationDto.setPatientId("2");

        // Action and assertion
        assertThrows(NoSuchElementException.class, () -> medicationService.updateMedication(medication.getId(),null,  saveMedicationDto));
    }

    @DisplayName(value = "Junit test method to update a medication successfully")
    @Test
    public void givenMedication_whenUpdate_thenReturnUpdatedMedication() {
        // Mock behaviour
        given(medicationRepo.existsById(any())).willReturn(true);
        given(medicationRepo.findById(medication.getId())).willReturn(Optional.of(medication));

        given(patientRepo.findById(any())).willReturn(Optional.of(patient));
        try (MockedStatic<MedicationMapper> medicationMapperMockedStatic = mockStatic(MedicationMapper.class)) {
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationFromSaveMedicationDto(any(), any(Patient.class), any(LocalDate.class), any(SaveMedicationDto.class)))
                    .thenReturn(medication);
            given(medicationRepo.save(medication)).willReturn(medication);
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationDto(medication))
                    .thenReturn(medicationDto);

            // Action
            MedicationDto updatedMedicationDto = medicationService.updateMedication(medication.getId(), today, saveMedicationDto);

            // Assertion
            assertThat(updatedMedicationDto).isNotNull();

            assertThat(updatedMedicationDto).isEqualTo(medicationDto);
        }
    }

    @DisplayName(value = "Junit test method to delete a medication that is unavailable")
    @Test
    public void givenNoMedication_whenDelete_thenThrowError() {
        // Mock behaviour
        given(medicationRepo.existsById(any())).willReturn(false);

        // Action and assertion
        assertThrows(NoSuchElementException.class, () -> medicationService.deleteMedication(medication.getId()));
        verify(medicationRepo, never()).deleteById(medication.getId());
        verify(medicationRepo, times(1)).existsById(medication.getId());
    }

    @DisplayName(value = "Junit test method to delete a medication and medication existing after deletion")
    @Test
    public void givenMedication_whenDelete_thenThrowError() {
        // Mock behaviour
        given(medicationRepo.existsById(any())).willReturn(true);
        willDoNothing().given(medicationRepo).deleteById(any());

        // Action and assertion
        assertThrows(NoSuchElementException.class, () -> medicationService.deleteMedication(medication.getId()));
        verify(medicationRepo, times(1)).deleteById(medication.getId());
        verify(medicationRepo, times(2)).existsById(medication.getId());
    }

    @DisplayName(value = "Junit test method to delete a medication successfully")
    @Test
    public void givenMedication_whenDelete_thenRemoveMedication() {
        // Mock behaviour
        given(medicationRepo.existsById(any())).willReturn(true).willReturn(false);
        willDoNothing().given(medicationRepo).deleteById(any());

        // Action and assertion
        String result = medicationService.deleteMedication(medication.getId());
        assertThat(result).isEqualTo("Successfully deleted medication with id: " + medication.getId() + ".");
        verify(medicationRepo, times(1)).deleteById(medication.getId());
        verify(medicationRepo, times(2)).existsById(medication.getId());
    }

    @DisplayName(value = "Junit test method to check if medication exists - existing case")
    @Test
    public void givenMedication_whenExistsById_thenReturnTrue() {
        // Mock behaviour
        given(medicationRepo.existsByPatientId(any())).willReturn(true);

        // Action
        boolean result = medicationService.existsByPatientId(patient.getId());

        // Assertion
        assertThat(result).isTrue();
        verify(medicationRepo, times(1)).existsByPatientId(any());
    }

    @DisplayName(value = "Junit test method to check if medication exists - non-existing case")
    @Test
    public void givenNoMedication_whenExistsById_thenReturnFalse() {
        // Mock behaviour
        given(medicationRepo.existsByPatientId(any())).willReturn(false);

        // Action
        boolean result = medicationService.existsByPatientId(patient.getId());

        // Assertion
        assertThat(result).isFalse();
        verify(medicationRepo, times(1)).existsByPatientId(any());
    }

    @DisplayName(value = "Junit test method to delete all medications for a given patient id and medication existing for the patient afterwards")
    @Test
    public void givenMedication_whenDeleteALlByPatientId_thenThrowError() {
        // Mock behaviour
        Long patientId = patient.getId();
        given(medicationRepo.deleteAllByPatientId(patientId)).willReturn(2);
        given(medicationService.existsByPatientId(patientId)).willReturn(true);

        // Action and Assertion
        assertThrows(RuntimeException.class, () -> medicationService.deleteAllMedicationByPatientId(patientId));
    }

    @DisplayName(value = "Junit test method to delete all medications for a given patient id successfully")
    @Test
    public void givenMedication_whenDeleteALlByPatientId_thenRemoveMedication() {
        // Mock behaviour
        Long patientId = patient.getId();
        given(medicationRepo.deleteAllByPatientId(patientId)).willReturn(2);
        given(medicationService.existsByPatientId(patientId)).willReturn(false);

        // Action
        medicationService.deleteAllMedicationByPatientId(patientId);

        // Assertion
        verify(medicationRepo, times(1)).deleteAllByPatientId(patientId);

    }

    @DisplayName("Junit test method to get all medications for a patient by username")
    @Test
    public void givenUsername_whenGetAllMedicationByUsername_thenReturnMedicationList() {
        // Mock Behaviour
        String username = "9876543210"; // patient's mobile number is the username
        given(patientRepo.findByMobile(username)).willReturn(Optional.of(patient));

        Medication medication1 = new Medication(
                2L,
                patient,
                "Eritel AM",
                "650 mg",
                "Once a day.",
                "Complete",
                today,
                today.plusDays(3),
                today,
                today,
                "After dinner."
        );

        MedicationDto medicationDto1 = new MedicationDto(
                2L,
                patient.getId(),
                "Eritel AM",
                "650 mg",
                "Once a day.",
                "Complete",
                today.toString(),
                today.plusDays(3).toString(),
                today.toString(),
                today.toString(),
                "After dinner."
        );

        List<Medication> medicationList = List.of(medication, medication1);
        given(medicationRepo.findAllByPatientId(patient.getId())).willReturn(medicationList);

        try (MockedStatic<MedicationMapper> medicationMapperMockedStatic = mockStatic(MedicationMapper.class)) {
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationDto(medication)).thenReturn(medicationDto);
            medicationMapperMockedStatic
                    .when(() -> MedicationMapper.mapToMedicationDto(medication1)).thenReturn(medicationDto1);

            // Action
            List<MedicationDto> medicationDtos = medicationService.getAllMedicationByUsername(username);

            // Assertion
            assertThat(medicationDtos).isNotEmpty();
            assertThat(medicationDtos.size()).isEqualTo(2);
            assertThat(medicationDtos).containsExactly(medicationDto, medicationDto1);
        }
    }

    @DisplayName("Junit test method to get all medications by username when patient not found")
    @Test
    public void givenInvalidUsername_whenGetAllMedicationByUsername_thenThrowError() {
        // Mock Behaviour
        String username = "9999999999"; // non-existing mobile number
        given(patientRepo.findByMobile(username)).willReturn(Optional.empty());

        // Action and assertion
        assertThrows(NoSuchElementException.class, () -> medicationService.getAllMedicationByUsername(username));
    }

    @DisplayName("Junit test method to get patient ID by username")
    @Test
    public void givenUsername_whenGetPatientIdByUsername_thenReturnPatientId() {
        // Mock Behaviour
        String username = "9876543210"; // Since username is the patient's mobile number
        given(patientRepo.findByMobile(username)).willReturn(Optional.of(patient));

        // Action
        String patientId = medicationService.getPatientIdByUsername(username);

        // Assertion
        assertThat(patientId).isEqualTo(patient.getId().toString());
    }

    @DisplayName("Junit test method to get patient ID by username when patient not found")
    @Test
    public void givenInvalidUsername_whenGetPatientIdByUsername_thenThrowError() {
        // Mock Behaviour
        String username = "9999999999"; // non-existing mobile number
        given(patientRepo.findByMobile(username)).willReturn(Optional.empty());

        // Action and assertion
        assertThrows(NoSuchElementException.class, () -> medicationService.getPatientIdByUsername(username));
    }

}
