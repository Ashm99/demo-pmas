package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.AppointmentMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import com.example.pmas.patientmedicineappointmentsystem.repo.AppointmentRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.DoctorRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.implementations.AppointmentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTests {
    @Mock
    private AppointmentRepo appointmentRepo;
    @Mock
    private PatientRepo patientRepo;
    @Mock
    private DoctorRepo doctorRepo;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;
    private Appointment appointment;
    private AppointmentDto appointmentDto;
    private SaveAppointmentDto saveAppointmentDto;
    private Patient patient;
    private PatientDto patientDto;
    private Doctor doctor;
    private DoctorDto doctorDto;
    private LocalDateTime dayAfterTomorrowNoon;
    private LocalDateTime now;

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
                24,
                "Male",
                "A1+",
                "Johanna",
                "9876543211",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true
        );

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
        patientDto = new PatientDto(
                1L,
                "John",
                "Doe",
                "jdoe@gmail.com",
                "9876543210",
                "USA",
                24,
                "Male",
                "A1+",
                "Johanna",
                "9876543211",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true

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

        dayAfterTomorrowNoon = LocalDateTime
                .of(LocalDate.now().plusDays(2), LocalTime.NOON);
        now = LocalDateTime.now();

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(dayAfterTomorrowNoon);
        appointment.setCreatedAt(now);

        appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);
        appointmentDto.setPatientDto(patientDto);
        appointmentDto.setDoctorDto(doctorDto);
        appointmentDto.setAppointmentDateTime(dayAfterTomorrowNoon.toString());
        appointmentDto.setCreatedAt(now.toString());

        saveAppointmentDto = new SaveAppointmentDto();
        saveAppointmentDto.setPatientId(patient.getId());
        saveAppointmentDto.setDoctorId(doctor.getId());
        saveAppointmentDto.setAppointmentDateTime(dayAfterTomorrowNoon.atZone(ZoneId.systemDefault()).toString());
    }

    @DisplayName(value = "JUnit test method to create an appointment successfully")
    @Test
    public void givenCorrectAppointment_whenCreate_thenReturnSavedAppointment() {
        // Mock behaviour
        try (MockedStatic<AppointmentMapper> appointmentMapperMockedStatic = mockStatic(AppointmentMapper.class)) {
            appointmentMapperMockedStatic
                    .when(() -> AppointmentMapper.mapToAppointmentFromSaveAppointmentDto(any(SaveAppointmentDto.class)))
                    .thenReturn(appointment);
            given(patientRepo.findById(any())).willReturn(Optional.of(patient));
            given(doctorRepo.findById(any())).willReturn(Optional.of(doctor));
            given(appointmentRepo.save(any(Appointment.class))).willReturn(appointment);
            appointmentMapperMockedStatic
                    .when(() -> AppointmentMapper.mapToAppointmentDto(appointment))
                    .thenReturn(appointmentDto);

            // Action
            AppointmentDto savedAppointmentDto = appointmentService.createAppointment(saveAppointmentDto);

            // Assertion
            assertThat(savedAppointmentDto).isNotNull();

            assertThat(savedAppointmentDto.getId()).isEqualTo(appointmentDto.getId());
            assertThat(savedAppointmentDto.getPatientDto()).isEqualTo(appointmentDto.getPatientDto());
            assertThat(savedAppointmentDto.getDoctorDto()).isEqualTo(appointmentDto.getDoctorDto());
            assertThat(savedAppointmentDto.getAppointmentDateTime()).isEqualTo(appointmentDto.getAppointmentDateTime());
            assertThat(savedAppointmentDto.getCreatedAt()).isEqualTo(appointmentDto.getCreatedAt());
        }
    }

    @DisplayName(value = "JUnit test method to create an appointment with an unavailable patient")
    @Test
    public void givenAppointmentAndUnavailablePatient_whenCreate_thenThrowError() {
        // Mock behaviour
        try (MockedStatic<AppointmentMapper> appointmentMapperMockedStatic = mockStatic(AppointmentMapper.class)) {
            appointmentMapperMockedStatic
                    .when(() -> AppointmentMapper.mapToAppointmentFromSaveAppointmentDto(
                            any(SaveAppointmentDto.class)
                    ))
                    .thenReturn(appointment);
            given(patientRepo.findById(any())).willReturn(Optional.empty());

            // Action and assertion
            Assertions.assertThrows(NoSuchElementException.class, () -> appointmentService.createAppointment(saveAppointmentDto));

            verify(patientRepo, times(1)).findById(any());
            verify(doctorRepo, never()).findById(any());
            verify(appointmentRepo, never()).save(any(Appointment.class));
        }
    }

    @DisplayName(value = "JUnit test method to create an appointment with an unavailable doctor")
    @Test
    public void givenAppointmentAndUnavailableDoctor_whenCreate_thenThrowError() {
        // Mock behaviour
        try (MockedStatic<AppointmentMapper> appointmentMapperMockedStatic = mockStatic(AppointmentMapper.class)) {
            appointmentMapperMockedStatic
                    .when(() -> AppointmentMapper.mapToAppointmentFromSaveAppointmentDto(
                            any(SaveAppointmentDto.class)
                    ))
                    .thenReturn(appointment);
            given(patientRepo.findById(any())).willReturn(Optional.of(patient));
            given(doctorRepo.findById(any())).willReturn(Optional.empty());

            // Action and assertion
            Assertions.assertThrows(NoSuchElementException.class, () -> appointmentService.createAppointment(saveAppointmentDto));

            verify(patientRepo, times(1)).findById(any());
            verify(doctorRepo, times(1)).findById(any());
            verify(appointmentRepo, never()).save(any(Appointment.class));
        }
    }

    @DisplayName(value = "JUnit test method to get all appointments")
    @Test
    public void givenAppointmentList_whenGetAll_thenReturnAppointmentList() {
        // Arrangements
        Patient patient1 = new Patient(
                1L,
                "John",
                "Doe",
                "jdoe@gmail.com",
                "9876543210",
                "John@2024",
                "USA",
                24,
                "Male",
                "A1+",
                "Johanna",
                "9876543211",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true
        );
        Doctor doctor1 = new Doctor(
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

        PatientDto patientDto1 = new PatientDto();
        patientDto1.setId(patient1.getId());
        patientDto1.setFirstName(patient1.getFirstName());
        patientDto1.setLastName(patient1.getLastName());
        patientDto1.setEmail(patient1.getEmail());
        patientDto1.setAddress(patient1.getAddress());
        patientDto1.setMobile(patient1.getMobile());

        DoctorDto doctorDto1 = new DoctorDto();
        doctorDto1.setId(doctor1.getId());
        doctorDto1.setFirstName(doctor1.getFirstName());
        doctorDto1.setLastName(doctor1.getLastName());
        doctorDto1.setSpeciality(doctor1.getSpeciality());

        Appointment appointment1 = new Appointment();
        appointment1.setId(2L);
        appointment1.setPatient(patient1);
        appointment1.setDoctor(doctor1);
        appointment1.setAppointmentDateTime(dayAfterTomorrowNoon);
        appointment1.setCreatedAt(now);

        AppointmentDto appointmentDto1 = new AppointmentDto();
        appointmentDto1.setId(appointment1.getId());
        appointmentDto1.setPatientDto(patientDto1);
        appointmentDto1.setDoctorDto(doctorDto1);
        appointmentDto1.setAppointmentDateTime(dayAfterTomorrowNoon.toString());
        appointmentDto1.setCreatedAt(now.toString());

        // Mock behaviour
        given(appointmentRepo.findAll()).willReturn(List.of(appointment, appointment1));
        try (MockedStatic<AppointmentMapper> appointmentMapperMockedStatic = mockStatic(AppointmentMapper.class)) {
            appointmentMapperMockedStatic
                    .when(() -> AppointmentMapper.mapToAppointmentDto(appointment))
                    .thenReturn(appointmentDto);
            appointmentMapperMockedStatic
                    .when(() -> AppointmentMapper.mapToAppointmentDto(appointment1))
                    .thenReturn(appointmentDto1);

            // Action
            List<AppointmentDto> appointmentDtoList = appointmentService.getAllAppointments();

            // Assertion
            assertThat(appointmentDtoList).isNotEmpty();
            assertThat(appointmentDtoList.size()).isEqualTo(2);

            assertThat(appointmentDtoList.get(0).getId()).isEqualTo(appointmentDto.getId());
            assertThat(appointmentDtoList.get(0).getPatientDto()).isEqualTo(appointmentDto.getPatientDto());
            assertThat(appointmentDtoList.get(0).getDoctorDto()).isEqualTo(appointmentDto.getDoctorDto());
            assertThat(appointmentDtoList.get(0).getAppointmentDateTime()).isEqualTo(appointmentDto.getAppointmentDateTime());
            assertThat(appointmentDtoList.get(0).getCreatedAt()).isEqualTo(appointmentDto.getCreatedAt());

            assertThat(appointmentDtoList.get(1).getId()).isEqualTo(appointmentDto1.getId());
            assertThat(appointmentDtoList.get(1).getPatientDto()).isEqualTo(appointmentDto1.getPatientDto());
            assertThat(appointmentDtoList.get(1).getDoctorDto()).isEqualTo(appointmentDto1.getDoctorDto());
            assertThat(appointmentDtoList.get(1).getAppointmentDateTime()).isEqualTo(appointmentDto1.getAppointmentDateTime());
            assertThat(appointmentDtoList.get(1).getCreatedAt()).isEqualTo(appointmentDto1.getCreatedAt());
        }
    }

    @DisplayName(value = "JUnit test method to get all appointments without having any appointments")
    @Test
    public void givenNoAppointment_whenGetAll_thenReturnEmptyList() {
        // Mock behaviour
        given(appointmentRepo.findAll()).willReturn(Collections.EMPTY_LIST);

        // Action
        List<AppointmentDto> appointmentDtoList = appointmentService.getAllAppointments();

        // Assertion
        assertThat(appointmentDtoList).isEmpty();
    }

    @DisplayName(value = "JUnit test method to get an appointment by id")
    @Test
    public void givenAppointmentObject_whenGetById_thenReturnAppointmentObject() {
        // Mock behaviour
        given(appointmentRepo.findById(any())).willReturn(Optional.of(appointment));
        try (MockedStatic<AppointmentMapper> appointmentMapperMockedStatic = mockStatic(AppointmentMapper.class)) {
            appointmentMapperMockedStatic
                    .when(() -> AppointmentMapper.mapToAppointmentDto(appointment))
                    .thenReturn(appointmentDto);

            // Action
            AppointmentDto fetchedAppointmentDto = appointmentService.getAppointmentById(appointment.getId());

            assertThat(fetchedAppointmentDto.getId()).isEqualTo(appointmentDto.getId());
            assertThat(fetchedAppointmentDto.getPatientDto()).isEqualTo(appointmentDto.getPatientDto());
            assertThat(fetchedAppointmentDto.getDoctorDto()).isEqualTo(appointmentDto.getDoctorDto());
            assertThat(fetchedAppointmentDto.getAppointmentDateTime()).isEqualTo(appointmentDto.getAppointmentDateTime());
            assertThat(fetchedAppointmentDto.getCreatedAt()).isEqualTo(appointmentDto.getCreatedAt());

        }
    }

    @DisplayName(value = "JUnit test method to get an appointment by id that is unavailable")
    @Test
    public void givenNoAppointmentObject_whenGetById_thenThrowError() {
        // Mock behaviour
        given(appointmentRepo.findById(any())).willReturn(Optional.empty());

        // Action and assertion
        Assertions.assertThrows(NoSuchElementException.class, () -> appointmentService.getAppointmentById(appointment.getId()));
    }

    @DisplayName(value = "JUnit test method to delete an appointment by id that is unavailable")
    @Test
    public void givenNoAppointment_whenDeleteById_thenThrowError() {
        // Mock behaviour
        given(appointmentRepo.existsById(any())).willReturn(false);

        // Action and assertion
        Assertions.assertThrows(NoSuchElementException.class, () -> appointmentService.deleteAppointment(appointment.getId()));
    }

    @DisplayName(value = "JUnit test method to delete an appointment by id and appointment existing after deletion")
    @Test
    public void givenAppointment_whenDeleteById_thenThrowError() {
        // Mock behaviour
        given(appointmentRepo.existsById(any())).willReturn(true);
        willDoNothing().given(appointmentRepo).deleteById(any());

        // Action and assertion
        Assertions.assertThrows(RuntimeException.class, () -> appointmentService.deleteAppointment(appointment.getId()));

        verify(appointmentRepo, times(1)).deleteById(appointment.getId());
        verify(appointmentRepo, times(2)).existsById(appointment.getId());
    }

    @DisplayName(value = "JUnit test method to delete an appointment by id successfully")
    @Test
    public void givenAppointment_whenDeleteById_thenRemoveAppointment() {
        // Mock behaviour
        given(appointmentRepo.existsById(any())).willReturn(true).willReturn(false);
        willDoNothing().given(appointmentRepo).deleteById(any());

        // Action
        String result = appointmentService.deleteAppointment(appointment.getId());

        // Assertion
        verify(appointmentRepo, times(1)).deleteById(appointment.getId());
        verify(appointmentRepo, times(2)).existsById(appointment.getId());
        assertThat(result).isEqualTo("Successfully cancelled the appointment.");
    }

    @DisplayName(value = "JUnit test method to check if any appointment exists for a patient - existing case")
    @Test
    public void givenAppointment_whenExistsByPatientId_thenReturnTrue() {
        // Mock behaviour
        given(appointmentRepo.existsByPatientId(any())).willReturn(true);

        // Action and Assertion
        assertThat(appointmentService.existsByPatientId(patient.getId())).isTrue();
    }

    @DisplayName(value = "JUnit test method to check if any appointment exists for a patient - non-existing case")
    @Test
    public void givenNoAppointment_whenExistsByPatientId_thenReturnFalse() {
        // Mock behaviour
        given(appointmentRepo.existsByPatientId(any())).willReturn(false);

        // Action and Assertion
        assertThat(appointmentService.existsByPatientId(patient.getId())).isFalse();
    }

    @DisplayName(value = "JUnit test method to delete all appointments of a patient and appointments exists after deletion")
    @Test
    public void givenAppointment_whenDeleteByPatientIdAndAppointmentsNotDeleted_thenThrowError() {
        // Mock behaviour
        given(appointmentRepo.deleteAllByPatientId(any())).willReturn(2);
        given(appointmentService.existsByPatientId(any())).willReturn(true);

        // Action and asssertion
        Assertions.assertThrows(RuntimeException.class, () -> appointmentService.deleteAllAppointmentByPatientId(patient.getId()));
    }

    @DisplayName(value = "JUnit test method to delete all appointments of a patient and appointments deleted successfully")
    @Test
    public void givenAppointment_whenDeleteByPatientIdAndAppointmentsDeleted_thenReturnNothing(){
        // Mock behaviour
        given(appointmentRepo.deleteAllByPatientId(any())).willReturn(2);
        given(appointmentService.existsByPatientId(any())).willReturn(false);

        // Action and assertion
        appointmentService.deleteAllAppointmentByPatientId(patient.getId());
    }

    @DisplayName(value = "JUnit test method to check if any appointment exists for a doctor - existing case")
    @Test
    public void givenAppointment_whenExistsByDoctorId_thenReturnTrue(){
        // Mock behaviour
        given(appointmentRepo.existsByDoctorId(any())).willReturn(true);

        // Action and Assertion
        assertThat(appointmentService.existsByDoctorId(doctor.getId())).isTrue();
    }

    @DisplayName(value = "JUnit test method to check if any appointment exists for a doctor - non-existing case")
    @Test
    public void givenNoAppointment_whenExistsByDoctorId_thenReturnFalse(){
        // Mock behaviour
        given(appointmentRepo.existsByDoctorId(any())).willReturn(false);

        // Action and Assertion
        assertThat(appointmentService.existsByDoctorId(doctor.getId())).isFalse();
    }

    @DisplayName(value = "JUnit test method to delete all appointments of a doctor and appointments exists after deletion")
    @Test
    public void givenAppointment_whenDeleteByDoctorIdAndAppointmentsNotDeleted_thenThrowError(){
        // Mock behaviour
        given(appointmentRepo.deleteAllByDoctorId(any())).willReturn(2);
        given(appointmentService.existsByDoctorId(any())).willReturn(true);

        // Action and asssertion
        Assertions.assertThrows(RuntimeException.class, () -> appointmentService.deleteAllAppointmentByDoctorId(doctor.getId()));
    }

    @DisplayName(value = "JUnit test method to delete all appointments of a doctor and appointments deleted successfully")
    @Test
    public void givenAppointment_whenDeleteByDoctorIdAndAppointmentsDeleted_thenReturnNothing(){
        // Mock behaviour
        given(appointmentRepo.deleteAllByDoctorId(any())).willReturn(2);
        given(appointmentService.existsByDoctorId(any())).willReturn(false);

        // Action and assertion
        appointmentService.deleteAllAppointmentByDoctorId(doctor.getId());
    }
}
