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

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("jdoe@gmail.com");
        patient.setAddress("USA");
        patient.setPassword("John@2024");
        patient.setMobile("9876543210");

        medication = new Medication(
//                1L,
//                patient,
//                "Dolo 650mg",
//                "Twice a day.",
//                today,
//                dayAfterTomorrow,
//                "One each after breakfast and dinner."
        );

        medicationDto = new MedicationDto(
//                1L,
//                patient.getId(),
//                "Dolo 650mg",
//                "Twice a day.",
//                today.toString(),
//                dayAfterTomorrow.toString(),
//                "One each after breakfast and dinner."
        );
        saveMedicationDto = new SaveMedicationDto(
//                patient.getId().toString(),
//                "Dolo 650mg",
//                "Twice a day.",
//                today.toString(),
//                dayAfterTomorrow.toString(),
//                "One each after breakfast and dinner."
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

            assertThat(savedMedication.getId()).isEqualTo(medicationDto.getId());
            assertThat(savedMedication.getPatientId()).isEqualTo(medicationDto.getPatientId());
            assertThat(savedMedication.getMedicine()).isEqualTo(medicationDto.getMedicine());
            assertThat(savedMedication.getFrequency()).isEqualTo(medicationDto.getFrequency());
            assertThat(savedMedication.getStartDate()).isEqualTo(medicationDto.getStartDate());
            assertThat(savedMedication.getEndDate()).isEqualTo(medicationDto.getEndDate());
            assertThat(savedMedication.getNotes()).isEqualTo(medicationDto.getNotes());
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
//                2L,
//                patient,
//                "Eritel AM",
//                "Once a day.",
//                today,
//                dayAfterTomorrow,
//                "After dinner."
        );

        MedicationDto medicationDto1 = new MedicationDto(
//                2L,
//                patient.getId(),
//                "Eritel AM",
//                "Once a day.",
//                today.toString(),
//                dayAfterTomorrow.toString(),
//                "After dinner."
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

            assertThat(savedMedicationDtos.get(0).getId()).isEqualTo(medicationDto.getId());
            assertThat(savedMedicationDtos.get(0).getPatientId()).isEqualTo(medicationDto.getPatientId());
            assertThat(savedMedicationDtos.get(0).getMedicine()).isEqualTo(medicationDto.getMedicine());
            assertThat(savedMedicationDtos.get(0).getFrequency()).isEqualTo(medicationDto.getFrequency());
            assertThat(savedMedicationDtos.get(0).getStartDate()).isEqualTo(medicationDto.getStartDate());
            assertThat(savedMedicationDtos.get(0).getEndDate()).isEqualTo(medicationDto.getEndDate());
            assertThat(savedMedicationDtos.get(0).getNotes()).isEqualTo(medicationDto.getNotes());

            assertThat(savedMedicationDtos.get(1).getId()).isEqualTo(medicationDto1.getId());
            assertThat(savedMedicationDtos.get(1).getPatientId()).isEqualTo(medicationDto1.getPatientId());
            assertThat(savedMedicationDtos.get(1).getMedicine()).isEqualTo(medicationDto1.getMedicine());
            assertThat(savedMedicationDtos.get(1).getFrequency()).isEqualTo(medicationDto1.getFrequency());
            assertThat(savedMedicationDtos.get(1).getStartDate()).isEqualTo(medicationDto1.getStartDate());
            assertThat(savedMedicationDtos.get(1).getEndDate()).isEqualTo(medicationDto1.getEndDate());
            assertThat(savedMedicationDtos.get(1).getNotes()).isEqualTo(medicationDto1.getNotes());
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

            assertThat(savedMedicationDto.getId()).isEqualTo(medicationDto.getId());
            assertThat(savedMedicationDto.getPatientId()).isEqualTo(medicationDto.getPatientId());
            assertThat(savedMedicationDto.getMedicine()).isEqualTo(medicationDto.getMedicine());
            assertThat(savedMedicationDto.getFrequency()).isEqualTo(medicationDto.getFrequency());
            assertThat(savedMedicationDto.getStartDate()).isEqualTo(medicationDto.getStartDate());
            assertThat(savedMedicationDto.getEndDate()).isEqualTo(medicationDto.getEndDate());
            assertThat(savedMedicationDto.getNotes()).isEqualTo(medicationDto.getNotes());
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
            MedicationDto updatedMedicationDto = medicationService.updateMedication(medication.getId(), null, saveMedicationDto);

            // Assertion
            assertThat(updatedMedicationDto).isNotNull();

            assertThat(updatedMedicationDto.getId()).isEqualTo(medicationDto.getId());
            assertThat(updatedMedicationDto.getPatientId()).isEqualTo(medicationDto.getPatientId());
            assertThat(updatedMedicationDto.getMedicine()).isEqualTo(medicationDto.getMedicine());
            assertThat(updatedMedicationDto.getFrequency()).isEqualTo(medicationDto.getFrequency());
            assertThat(updatedMedicationDto.getStartDate()).isEqualTo(medicationDto.getStartDate());
            assertThat(updatedMedicationDto.getEndDate()).isEqualTo(medicationDto.getEndDate());
            assertThat(updatedMedicationDto.getNotes()).isEqualTo(medicationDto.getNotes());
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
    public void givenMedication_whenDeleteALlByPatiendId_thenThrowError() {
        // Mock behaviour
        Long patientId = patient.getId();
        given(medicationRepo.deleteAllByPatientId(patientId)).willReturn(2);
        given(medicationService.existsByPatientId(patientId)).willReturn(true);

        // Action and Assertion
        assertThrows(RuntimeException.class, () -> medicationService.deleteAllMedicationByPatientId(patientId));
    }

    @DisplayName(value = "Junit test method to delete all medications for a given patient id successfully")
    @Test
    public void givenMedication_whenDeleteALlByPatiendId_thenRemoveMedication() {
        // Mock behaviour
        Long patientId = patient.getId();
        given(medicationRepo.deleteAllByPatientId(patientId)).willReturn(2);
        given(medicationService.existsByPatientId(patientId)).willReturn(false);

        // Action
        medicationService.deleteAllMedicationByPatientId(patientId);

        // Assertion
        verify(medicationRepo, times(1)).deleteAllByPatientId(patientId);

    }
}
