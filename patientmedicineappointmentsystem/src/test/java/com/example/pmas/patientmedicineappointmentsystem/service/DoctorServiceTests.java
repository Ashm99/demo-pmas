package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.DoctorMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.repo.DoctorRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.implementations.DoctorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTests {

    @Mock
    private DoctorRepo doctorRepo;
    @Mock
    private AppointmentService appointmentService;
    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorDto doctorDto;
    private SaveDoctorDto saveDoctorDto;

    @BeforeEach
    public void setup() {
        doctor = new Doctor(
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

    @DisplayName("JUnit test method to add multiple new doctors.")
    @Test
    public void givenListOfSaveDoctorDto_whenAddAllDoctors_thenReturnListOfDoctorDto() {
        // Arrange
        SaveDoctorDto saveDoctorDto1 = new SaveDoctorDto(
                "Alice",
                "Smith",
                "Female",
                "8888888888",
                "alice@example.com",
                "Cardiology",
                10,
                "MBBS",
                "English",
                "USA"
        );
        SaveDoctorDto saveDoctorDto2 = new SaveDoctorDto(
                "Bob",
                "Johnson",
                "Male",
                "9999999999",
                "bob@example.com",
                "Dermatology",
                8,
                "MBBS",
                "English",
                "USA"
        );

        List<SaveDoctorDto> saveDoctorDtoList = List.of(saveDoctorDto1, saveDoctorDto2);

        Doctor doctor1 = new Doctor(1L,
                "Alice",
                "Smith",
                "Female",
                "8888888888",
                "alice@example.com",
                "Cardiology",
                10,
                "MBBS",
                "English",
                "USA"
        );
        Doctor doctor2 = new Doctor(2L,
                "Bob",
                "Johnson",
                "Male",
                "9999999999",
                "bob@example.com",
                "Dermatology",
                8,
                "MBBS",
                "English",
                "USA"
        );

        DoctorDto doctorDto1 = new DoctorDto(1L,
                "Alice",
                "Smith",
                "Female",
                "8888888888",
                "alice@example.com",
                "Cardiology",
                10,
                "MBBS",
                "English",
                "USA"
        );
        DoctorDto doctorDto2 = new DoctorDto(2L,
                "Bob",
                "Johnson",
                "Male",
                "9999999999",
                "bob@example.com",
                "Dermatology",
                8,
                "MBBS",
                "English",
                "USA"
        );

        List<Doctor> savedDoctors = List.of(doctor1, doctor2);

        // Mock behavior
        try (MockedStatic<DoctorMapper> doctorMapperMockedStatic = mockStatic(DoctorMapper.class)) {
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorFromSaveDoctorDto(any(), any(SaveDoctorDto.class)))
                    .thenReturn(doctor1).thenReturn(doctor2);
            given(doctorRepo.saveAll(any())).willReturn(savedDoctors);
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorDto(any()))
                    .thenReturn(doctorDto1, doctorDto2);

            // Action
            List<DoctorDto> savedDoctorDtoList = doctorService.addAllDoctors(saveDoctorDtoList);

            // Assertion
            assertThat(savedDoctorDtoList).isNotNull();
            assertThat(savedDoctorDtoList.size()).isEqualTo(2);
            assertThat(savedDoctorDtoList).containsExactlyInAnyOrder(doctorDto1, doctorDto2);
        }
    }

    @DisplayName(value = "JUnit test method to save a doctor.")
    @Test
    public void givenDoctor_whenSave_thenReturnDoctorDto() {
        // Mock behaviour
        try (MockedStatic<DoctorMapper> doctorMapperMockedStatic = mockStatic(DoctorMapper.class)) {
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorFromSaveDoctorDto(any(), any(SaveDoctorDto.class)))
                    .thenReturn(doctor);
            given(doctorRepo.save(doctor)).willReturn(doctor);
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorDto(doctor))
                    .thenReturn(doctorDto);
            // Action
            DoctorDto savedDoctorDto = doctorService.addDoctor(saveDoctorDto);

            // Assertion
            assertThat(savedDoctorDto).isNotNull();

            assertThat(savedDoctorDto).isEqualTo(doctorDto);
        }
    }

    @DisplayName(value = "JUnit test method for getting all doctors in database.")
    @Test
    public void givenDoctorList_whenGetAllDoctors_thenReturnDoctorDtoList() {
        // Mock behaviour
        Doctor doctor1 = new Doctor(
                2L,
                "Alice",
                "Smith",
                "Female",
                "8888888888",
                "alice@example.com",
                "Cardiology",
                10,
                "MBBS",
                "English",
                "USA"
        );
        DoctorDto doctorDto1 = new DoctorDto(
                2L,
                "Alice",
                "Smith",
                "Female",
                "8888888888",
                "alice@example.com",
                "Cardiology",
                10,
                "MBBS",
                "English",
                "USA"
        );
        List<Doctor> doctors = List.of(doctor, doctor1);
        given(doctorRepo.findAll()).willReturn(doctors);
        try (MockedStatic<DoctorMapper> doctorMapperMockedStatic = mockStatic(DoctorMapper.class)) {
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorDto(doctor))
                    .thenReturn(doctorDto);
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorDto(doctor1))
                    .thenReturn(doctorDto1);

            // Action
            List<DoctorDto> doctorDtoList = doctorService.getAllDoctors();

            // Assertion
            assertThat(doctorDtoList).isNotEmpty();
            assertThat(doctorDtoList.size()).isGreaterThan(0);
            assertThat(doctorDtoList.size()).isEqualTo(2);

            assertThat(doctorDtoList).containsExactlyInAnyOrder(doctorDto, doctorDto1);
        }
    }

    @DisplayName(value = "JUnit test method for getting all doctors when database is empty.")
    @Test
    public void givenNoDoctorList_whenGetAllDoctors_thenThrowError() {
        // Mock behaviour
        given(doctorRepo.findAll()).willReturn(new ArrayList<>());
        // Action and assertion
        Assertions.assertThrows(NoSuchElementException.class, () -> doctorService.getAllDoctors());
    }

    @DisplayName(value = "JUnit test method for get doctor by id.")
    @Test
    public void givenDoctor_whenGetDoctorById_thenReturnDoctorDto() {
        // Mock behaviour
        given(doctorRepo.findById(doctor.getId())).willReturn(Optional.of(doctor));
        try (MockedStatic<DoctorMapper> doctorMapperMockedStatic = mockStatic(DoctorMapper.class)) {
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorDto(doctor))
                    .thenReturn(doctorDto);
        }
        // Action
        DoctorDto fetchedDoctorDto = doctorService.getDoctorById(doctor.getId());

        // Assertion
        assertThat(fetchedDoctorDto).isNotNull();

        assertThat(fetchedDoctorDto).isEqualTo(doctorDto);
    }

    @DisplayName(value = "JUnit test method for get doctor by id for an unavailable doctor.")
    @Test
    public void givenNoDoctor_whenGetDoctorById_thenReturnDoctorDto() {
        // Mock behaviour
        given(doctorRepo.findById(doctor.getId())).willReturn(Optional.empty());
        // Action and assertion
        Assertions.assertThrows(NoSuchElementException.class, () -> doctorService.getDoctorById(doctor.getId()));
    }

    @DisplayName(value = "JUnit test method to update a doctor.")
    @Test
    public void givenDoctor_whenUpdate_thenReturnUpdatedDoctor() {
        // Mock behaviour
        given(doctorRepo.existsById(any())).willReturn(true);
        try (MockedStatic<DoctorMapper> doctorMapperMockedStatic = mockStatic(DoctorMapper.class)) {
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorFromSaveDoctorDto(any(), any(SaveDoctorDto.class)))
                    .thenReturn(doctor);
            given(doctorRepo.save(doctor)).willReturn(doctor);
            doctorMapperMockedStatic
                    .when(() -> DoctorMapper.mapToDoctorDto(doctor))
                    .thenReturn(doctorDto);

            // Action
            DoctorDto updatedDoctorDto = doctorService.updateDoctor(doctor.getId(), saveDoctorDto);

            // Assertion
            assertThat(updatedDoctorDto).isNotNull();

            assertThat(updatedDoctorDto).isEqualTo(doctorDto);
        }
    }

    @DisplayName(value = "JUnit test method to update a doctor that is unavailable.")
    @Test
    public void givenNoDoctor_whenUpdate_thenThrowError() {
        // Mock behaviour
        given(doctorRepo.existsById(any())).willReturn(false);

        // Action and Assertion
        Assertions.assertThrows(NoSuchElementException.class, () -> doctorService.updateDoctor(doctor.getId(), saveDoctorDto));
    }

    @DisplayName(value = "JUnit test method to delete a doctor that is unavailable.")
    @Test
    public void givenNoDoctor_whenDelete_thenThrowError() {
        // Mock behaviour
        given(doctorRepo.existsById(any())).willReturn(false);
        // Action and Assertion
        Assertions.assertThrows(NoSuchElementException.class, () -> doctorService.deleteDoctorById(doctor.getId()));
    }

    @DisplayName(value = "JUnit test method to delete a doctor having an appointment.")
    @Test
    public void givenDoctorWithAppointment_whenDelete_thenRemoveAppointmentAndDoctor() {
        // Mock behaviour
        Long id = doctor.getId();
        given(doctorRepo.existsById(id)).willReturn(true).willReturn(false);

        given(appointmentService.existsByDoctorId(id)).willReturn(true);

        willDoNothing().given(appointmentService).deleteAllAppointmentByDoctorId(id);

        willDoNothing().given(doctorRepo).deleteById(id);

        // Action
        doctorService.deleteDoctorById(id);

        // Assertion
        verify(appointmentService, times(1)).deleteAllAppointmentByDoctorId(id);

        verify(doctorRepo, times(1)).deleteById(id);

        verify(doctorRepo, times(2)).existsById(id);
    }

    @DisplayName(value = "JUnit test method to delete a doctor without an appointment.")
    @Test
    public void givenDoctorWithoutAppointment_whenDelete_thenRemoveDoctor() {
        // Mock behaviour
        Long id = doctor.getId();
        given(doctorRepo.existsById(any())).willReturn(true).willReturn(false);
        given(appointmentService.existsByDoctorId(any())).willReturn(false);

        willDoNothing().given(doctorRepo).deleteById(id);

        // Action
        doctorService.deleteDoctorById(id);

        // Assertion
        verify(appointmentService, never()).deleteAllAppointmentByDoctorId(id);
        verify(doctorRepo, times(1)).deleteById(id);

    }

    @DisplayName(value = "JUnit test method to delete a doctor but doctor existing after deletion.")
    @Test
    public void givenDoctor_whenDelete_thenThrowError() {
        // Mock behaviour
        Long id = doctor.getId();
        given(doctorRepo.existsById(any())).willReturn(true);
        given(appointmentService.existsByDoctorId(any())).willReturn(false);

        willDoNothing().given(doctorRepo).deleteById(id);

        // Action and Assertion
        Assertions.assertThrows(RuntimeException.class, () -> doctorService.deleteDoctorById(id));

        verify(appointmentService, never()).deleteAllAppointmentByDoctorId(id);
        verify(doctorRepo, times(1)).deleteById(id);
    }
}
