package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SavePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.PatientMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.implementations.PatientServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTests {
    @Mock
    private PatientRepo patientRepo;
    @Mock
    private AppointmentService appointmentService;
    @Mock
    private MedicationService medicationService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private SavePatientDto savePatientDto;
    private PatientDto patientDto;
    @BeforeEach
    public void setup() {
//        MockitoAnnotations.initMocks(this);
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

    @DisplayName(value = "JUnit test for save patient method")
    @Test
    public void givenSavePatientDtoObject_whenSave_thenReturnSavedPatientDTO() {
        // Mock behaviour or Arrange
        try (MockedStatic<PatientMapper> patientMapperMockedStatic = mockStatic(PatientMapper.class)) {
            patientMapperMockedStatic
                    .when(() -> PatientMapper.mapToPatientFromSavePatientDto(any(), any(SavePatientDto.class)))
                    .thenReturn(patient);
            given(patientRepo.save(patient)).willReturn(patient);
            patientMapperMockedStatic
                    .when(() -> PatientMapper.mapToPatientDto(any(Patient.class)))
                    .thenReturn(patientDto);

            // Action
            PatientDto patientDto1 = patientService.addPatient(savePatientDto);

            // Assertion
            assertThat(patientDto1).isNotNull();
            assertThat(patientDto1.getId()).isGreaterThan(0);

            assertThat(patientDto1).isEqualTo(patientDto);
        }
    }

    @DisplayName(value = "JUnit test for getAll patient method without patient data")
    @Test
    public void givenNoPatientList_whenFindAll_thenThrowError() {
        // Mock behaviour or Arrange

        given(patientRepo.findAll()).willReturn(new ArrayList<>());

        // Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> patientService.getAllPatients());
    }

    @DisplayName(value = "JUnit test for getAll patient method")
    @Test
    public void givenPatientList_whenFindAll_thenReturnPatientDTOList() {
        // Mock behaviour or Arrange
        Patient patient1 = new Patient(
                2L,
                "Vidharan",
                "M",
                "vidhu@gmail.com",
                "9753186420",
                "Vidharan@2024",
                "India",
                25,
                "Male",
                "O+",
                "Sakthi",
                "1234567890",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true
        );

        PatientDto patientDto1 = new PatientDto(
                2L,
                "Vidharan",
                "M",
                "vidhu@gmail.com",
                "9753186420",
                "India",
                25,
                "Male",
                "O+",
                "Sakthi",
                "1234567890",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true
        );

        List<Patient> patientList = List.of(patient, patient1);

        given(patientRepo.findAll()).willReturn(patientList);

        try (MockedStatic<PatientMapper> patientMapperMockedStatic = mockStatic(PatientMapper.class)) {
            patientMapperMockedStatic
                    .when(() -> PatientMapper.mapToPatientDto(patient))
                    .thenReturn(patientDto);
            patientMapperMockedStatic
                    .when(() -> PatientMapper.mapToPatientDto(patient1))
                    .thenReturn(patientDto1);

            // Act
            List<PatientDto> patientDtoList = patientService.getAllPatients();

            // Assert
            assertThat(patientDtoList).isNotEmpty();
            assertThat(patientDtoList.size()).isEqualTo(2);

            assertThat(patientDtoList).containsExactlyInAnyOrder(patientDto, patientDto1);
        }
    }

    @DisplayName(value = "JUnit test for get patient by id method without patient data")
    @Test
    public void givenNoPatient_whenFindById_thenThrowError() {
        // Arrange
        given(patientRepo.findById(patient.getId())).willReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, ()->patientService.getPatientById(patient.getId()));
    }

    @DisplayName(value = "JUnit test for get patient by id method")
    @Test
    public void givenPatient_whenFindById_thenReturnPatientDTO() {
        // Arrange
        given(patientRepo.findById(patient.getId())).willReturn(Optional.of(patient));
        try (MockedStatic<PatientMapper> patientMapperMockedStatic = mockStatic(PatientMapper.class)) {
            patientMapperMockedStatic.when(()->PatientMapper.mapToPatientDto(patient))
                    .thenReturn(patientDto);

            // Act
            PatientDto fetchedPatientDto = patientService.getPatientById(patient.getId());

            // Assert
            assertThat(fetchedPatientDto).isNotNull();
            assertThat(fetchedPatientDto).isEqualTo(patientDto);
        }
    }

    @DisplayName(value = "JUnit test for update patient method without patient data")
    @Test
    public void givenNoPatientDtoObject_whenUpdated_thenThrowError() {
        given(patientRepo.existsById(patient.getId())).willReturn(false);

        Assertions.assertThrows(NoSuchElementException.class, ()->patientService.updatePatient(patient.getId(), savePatientDto));
    }

    @DisplayName(value = "JUnit test for update patient method")
    @Test
    public void givenSavePatientDtoObject_whenUpdated_thenReturnUpdatedPatientDTO() {
        given(patientRepo.existsById(patient.getId())).willReturn(true);
        given(patientRepo.save(patient)).willReturn(patient);

        // Mock behaviour or Arrange
        try (MockedStatic<PatientMapper> patientMapperMockedStatic = mockStatic(PatientMapper.class)) {
            patientMapperMockedStatic
                    .when(() -> PatientMapper.mapToPatientFromSavePatientDto(patient.getId(), savePatientDto))
                    .thenReturn(patient);
            patientMapperMockedStatic
                    .when(() -> PatientMapper.mapToPatientDto(patient))
                    .thenReturn(patientDto);

            // Action
            PatientDto patientDto1 = patientService.updatePatient(patient.getId(), savePatientDto);

            // Assertion
            assertThat(patientDto1).isNotNull();
            assertThat(patientDto1.getId()).isGreaterThan(0);

            assertThat(patientDto1).isEqualTo(patientDto);
        }
    }

    @DisplayName(value = "JUnit test for delete patient method when patient with given id is unavailable.")
    @Test
    public void givenNoPatientObject_whenDelete_thenThrowError(){
        // Mock behaviour
        Long id = patient.getId();
        given(patientRepo.existsById(id)).willReturn(false);

        // Action and Assertion
        Assertions.assertThrows(NoSuchElementException.class, ()->patientService.deletePatientById(id));
        verify(patientRepo).existsById(id);
        verify(patientRepo, times(1)).existsById(id);
        verify(patientRepo, never()).deleteById(id);
        verify(medicationService, never()).existsByPatientId(id);
        verify(appointmentService, never()).existsByPatientId(id);
    }

    @DisplayName(value = "JUnit test for delete patient method when patient is not deleted and throws error.")
    @Test
    public void givenPatientObject_whenDelete_thenThrowError(){
        // Mock behaviour
        Long id = patient.getId();
        given(patientRepo.existsById(id)).willReturn(true);
        given(appointmentService.existsByPatientId(id)).willReturn(false);
        given(medicationService.existsByPatientId(id)).willReturn(false);
        willDoNothing().given(patientRepo).deleteById(id);

        // Action and Assertion
        Assertions.assertThrows(RuntimeException.class, ()->patientService.deletePatientById(id));
        verify(patientRepo, times(1)).deleteById(id);
        verify(patientRepo, times(2)).existsById(id);
        verify(medicationService, never()).deleteAllMedicationByPatientId(id);
        verify(appointmentService, never()).deleteAllAppointmentByPatientId(id);
    }

    @DisplayName(value = "JUnit test for delete patient method when patient is deleted successfully.")
    @Test
    public void givenPatientObject_whenDelete_thenRemoveObject(){
        // Mock behaviour
        Long id = patient.getId();
        given(patientRepo.existsById(id)).willReturn(true).willReturn(false);
        given(appointmentService.existsByPatientId(id)).willReturn(false);
        given(medicationService.existsByPatientId(id)).willReturn(false);
        willDoNothing().given(patientRepo).deleteById(id);

        // Action
        patientService.deletePatientById(id);

        // Assertion
        verify(patientRepo, times(1)).deleteById(id);
        verify(patientRepo, times(2)).existsById(id);
        verify(medicationService, never()).deleteAllMedicationByPatientId(id);
        verify(appointmentService, never()).deleteAllAppointmentByPatientId(id);
    }

    @DisplayName(value = "JUnit test for delete patient method when no appointment but with medication and patient is deleted successfully.")
    @Test
    public void givenPatientAndMedicationNoAppointment_whenDelete_thenRemoveObject(){
        // Mock behaviour
        Long id = patient.getId();
        given(patientRepo.existsById(id)).willReturn(true).willReturn(false);
        given(appointmentService.existsByPatientId(id)).willReturn(false);
        given(medicationService.existsByPatientId(id)).willReturn(true);
        willDoNothing().given(patientRepo).deleteById(id);
        willDoNothing().given(medicationService).deleteAllMedicationByPatientId(id);

        // Action
        patientService.deletePatientById(id);

        // Assertion
        verify(patientRepo, times(2)).existsById(id);
        verify(appointmentService, never()).deleteAllAppointmentByPatientId(id);
        verify(medicationService, times(1)).deleteAllMedicationByPatientId(id);
        verify(patientRepo, times(1)).deleteById(id);
    }

    @DisplayName(value = "JUnit test for delete patient method with appointment and no medication and when patient is deleted successfully.")
    @Test
    public void givenPatientAndAppointmentNoMedication_whenDelete_thenRemoveObject(){
        // Mock behaviour
        Long id = patient.getId();
        given(patientRepo.existsById(id)).willReturn(true).willReturn(false);
        given(appointmentService.existsByPatientId(id)).willReturn(true);
        given(medicationService.existsByPatientId(id)).willReturn(false);
        willDoNothing().given(patientRepo).deleteById(id);
        willDoNothing().given(appointmentService).deleteAllAppointmentByPatientId(id);

        // Action
        patientService.deletePatientById(id);

        // Assertion
        verify(patientRepo, times(2)).existsById(id);
        verify(appointmentService, times(1)).deleteAllAppointmentByPatientId(id);
        verify(medicationService, never()).deleteAllMedicationByPatientId(id);
        verify(patientRepo, times(1)).deleteById(id);
    }

    @DisplayName(value = "JUnit test for delete patient method with both appointment and medication and patient is deleted successfully.")
    @Test
    public void givenPatientAndAppointmentAndMedication_whenDelete_thenRemoveObject(){
        // Mock behaviour
        Long id = patient.getId();
        given(patientRepo.existsById(id)).willReturn(true).willReturn(false);
        given(appointmentService.existsByPatientId(id)).willReturn(true);
        given(medicationService.existsByPatientId(id)).willReturn(true);
        willDoNothing().given(patientRepo).deleteById(id);
        willDoNothing().given(appointmentService).deleteAllAppointmentByPatientId(id);
        willDoNothing().given(medicationService).deleteAllMedicationByPatientId(id);

        // Action
        patientService.deletePatientById(id);

        // Assertion
        verify(patientRepo, times(2)).existsById(id);
        verify(appointmentService, times(1)).deleteAllAppointmentByPatientId(id);
        verify(medicationService, times(1)).deleteAllMedicationByPatientId(id);
        verify(patientRepo, times(1)).deleteById(id);
    }

    @DisplayName(value = "JUnit test for get patient by mobile number method when patient does not exist")
    @Test
    public void givenInvalidUsername_whenGetPatientByUsername_thenThrowError() {
        // Arrange
        String mobileNo = "9876543210";
        given(patientRepo.findByMobile(mobileNo)).willReturn(Optional.empty());

        // Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> patientService.getPatientByUsername(mobileNo));
    }

    @DisplayName(value = "JUnit test for get patient by mobile number method")
    @Test
    public void givenValidUsername_whenGetPatientByUsername_thenReturnPatientDTO() {
        // Arrange
        String mobileNo = "9876543210";
        given(patientRepo.findByMobile(mobileNo)).willReturn(Optional.of(patient));

        try (MockedStatic<PatientMapper> patientMapperMockedStatic = mockStatic(PatientMapper.class)) {
            patientMapperMockedStatic.when(() -> PatientMapper.mapToPatientDto(patient))
                    .thenReturn(patientDto);

            // Act
            PatientDto fetchedPatientDto = patientService.getPatientByUsername(mobileNo);

            // Assert
            assertThat(fetchedPatientDto).isNotNull();
            assertThat(fetchedPatientDto).isEqualTo(patientDto);
        }
    }

    @DisplayName(value = "JUnit test for get patient's first name by mobile number method when patient does not exist")
    @Test
    public void givenInvalidUsername_whenGetPatientFirstnameByUsername_thenThrowError() {
        // Arrange
        String mobileNo = "9876543210";
        given(patientRepo.findByMobile(mobileNo)).willReturn(Optional.empty());

        // Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> patientService.getPatientFirstnameByUsername(mobileNo));
    }

    @DisplayName(value = "JUnit test for get patient's first name by mobile number method")
    @Test
    public void givenValidUsername_whenGetPatientFirstnameByUsername_thenReturnFirstName() {
        // Arrange
        String mobileNo = "9876543210";
        given(patientRepo.findByMobile(mobileNo)).willReturn(Optional.of(patient));

        // Act
        String firstName = patientService.getPatientFirstnameByUsername(mobileNo);

        // Assert
        assertThat(firstName).isNotNull();
        assertThat(firstName).isEqualTo(patient.getFirstName());
    }

}
