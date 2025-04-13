package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest // Will load the Entity and JPA related classes -> The repository layer
public class AppointmentRepositoryTests {
    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorRepo doctorRepo;
    private Patient patient;
    private Doctor doctor;

    private Appointment appointment;

    @BeforeEach
    public void setup() {
        patient = new Patient(
                null,
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

        doctor = new Doctor(
                null,
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

        patient = patientRepo.save(patient);
        doctor = doctorRepo.save(doctor);

        appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        LocalDateTime appointmentDateTime = LocalDateTime.parse("2025-01-01T06:30:00");
        appointment.setAppointmentDateTime(appointmentDateTime);
        LocalDateTime appointmentCreatedTime = LocalDateTime.now();
        appointment.setCreatedAt(appointmentCreatedTime);

    }

    @DisplayName(value = "JUnit test method for save appointment method")
    @Test
    public void givenAppointmentObject_whenSave_thenSaveAndReturnAppointmentObject() {
        Appointment savedAppointment = appointmentRepo.save(appointment);

        assertThat(savedAppointment).isNotNull();

        assertThat(savedAppointment.getPatient()).isEqualTo(patient);

        assertThat(savedAppointment.getDoctor()).isEqualTo(doctor);

        assertThat(savedAppointment.getAppointmentDateTime().toString()).isEqualTo(appointment.getAppointmentDateTime().toString());
        assertThat(savedAppointment.getCreatedAt().toString()).isEqualTo(appointment.getCreatedAt().toString());

    }

    @DisplayName("JUnit test method for find all appointment repo method")
    @Test
    public void givenAppointmentList_whenFindAll_thenReturnAppointmentList() {

        Doctor doctor1 = new Doctor(
                null,
                "Rebecca",
                "Johnson",
                "Female",
                "7777777778",
                "rjohn@gmail.com",
                "Orthopedics",
                5,
                "MBBS",
                "English",
                "USA"
        );

        LocalDateTime appointmentDateTime1 = LocalDateTime.parse("2025-01-02T06:30:00");
        LocalDateTime appointmentCreatedTime1 = LocalDateTime.now();

        doctor1 = doctorRepo.save(doctor1);

        Appointment appointment1 = new Appointment();
        appointment1.setPatient(patient);
        appointment1.setDoctor(doctor1);
        appointment1.setAppointmentDateTime(appointmentDateTime1);
        appointment1.setCreatedAt(appointmentCreatedTime1);

        appointmentRepo.save(appointment);
        appointmentRepo.save(appointment1);

        List<Appointment> appointments = appointmentRepo.findAll();

        assertThat(appointments).isNotEmpty();
        assertThat(appointments.size()).isEqualTo(2);

        assertThat(appointments.get(0).getPatient()).isEqualTo(patient);

        assertThat(appointments.get(0).getDoctor()).isEqualTo(doctor);

        assertThat(appointments.get(0).getAppointmentDateTime().toString()).isEqualTo(appointment.getAppointmentDateTime().toString());
        assertThat(appointments.get(0).getCreatedAt().toString()).isEqualTo(appointment.getCreatedAt().toString());

        assertThat(appointments.get(1).getPatient()).isEqualTo(patient);

        assertThat(appointments.get(1).getDoctor()).isEqualTo(doctor1);

        assertThat(appointments.get(1).getAppointmentDateTime().toString()).isEqualTo(appointmentDateTime1.toString());
        assertThat(appointments.get(1).getCreatedAt().toString()).isEqualTo(appointmentCreatedTime1.toString());
    }

    @DisplayName("JUnit test method for find all appointment method without objects")
    @Test
    public void givenNoAppointmentList_whenFindAll_thenReturnEmptyList() {

        List<Appointment> appointments = appointmentRepo.findAll();

        assertThat(appointments).isEmpty();
    }

    @DisplayName(value = "JUnit test method for find by appointment id method without having appointment")
    @Test
    public void givenNoAppointmentObject_whenFindById_thenReturnEmptyOptional() {
        Optional<Appointment> optionalAppointment = appointmentRepo.findById(1L);

        assertThat(optionalAppointment).isEmpty();
    }

    @DisplayName(value = "JUnit test method for find by appointment id method")
    @Test
    public void givenAppointmentObject_whenFindById_thenReturnAppointmentObject() {
        Long id = appointmentRepo.save(appointment).getId();

        Optional<Appointment> optionalAppointment = appointmentRepo.findById(id);

        assertThat(optionalAppointment).isNotEmpty();

        assertThat(optionalAppointment.get().getPatient()).isEqualTo(patient);

        assertThat(optionalAppointment.get().getDoctor()).isEqualTo(doctor);

        assertThat(optionalAppointment.get().getAppointmentDateTime().toString()).isEqualTo(appointment.getAppointmentDateTime().toString());
        assertThat(optionalAppointment.get().getCreatedAt().toString()).isEqualTo(appointment.getCreatedAt().toString());
    }

    @DisplayName(value = "JUnit test method for existsById method without appointment")
    @Test
    public void givenNoAppointmentObject_whenExistsById_thenReturnFalse() {
        boolean appointmentIsPresent = appointmentRepo.existsById(1L);

        assertThat(appointmentIsPresent).isFalse();
    }

    @DisplayName(value = "JUnit test method for existsById method with appointment")
    @Test
    public void givenAppointmentObject_whenExistsById_thenReturnTrue() {
        Long id = appointmentRepo.save(appointment).getId();

        boolean appointmentIsPresent = appointmentRepo.existsById(id);

        assertThat(appointmentIsPresent).isTrue();
    }

    @DisplayName(value = "JUnit test method for deleteById method.")
    @Test
    public void givenAppointmentObject_whenDeleteById_thenRemoveAppointment() {
        Long id = appointmentRepo.save(appointment).getId();

        assertThat(appointmentRepo.existsById(id)).isTrue();

        appointmentRepo.deleteById(id);

        assertThat(appointmentRepo.existsById(id)).isFalse();
    }

    @DisplayName(value = "JUnit test method for existsByPatientId repo method for an unsaved patient")
    @Test
    public void givenUnsavedPatientId_whenExistsByPatientId_thenReturnFalse() {
        boolean patientIsPresent = appointmentRepo.existsByPatientId(1L);

        assertThat(patientIsPresent).isFalse();
    }

    @DisplayName(value = "JUnit test method for existsByPatientId repo method.")
    @Test
    public void givenSavedPatientId_whenExistsByPatientId_thenReturnTrue() {

        appointmentRepo.save(appointment);

        boolean patientIsPresent = appointmentRepo.existsByPatientId(patient.getId());

        assertThat(patientIsPresent).isTrue();
    }

    @DisplayName(value = "JUnit test method for deleteAllByPatientId repo method.")
    @Test
    public void givenSavedPatientId_whenDeleteAllByPatientId_thenReturnNoOfRowsDeleted() {

        Doctor doctor1 = new Doctor(
                null,
                "Rebecca",
                "Johnson",
                "Female",
                "7777777778",
                "rjohn@gmail.com",
                "Orthopedics",
                5,
                "MBBS",
                "English",
                "USA"
        );

        LocalDateTime appointmentDateTime1 = LocalDateTime.parse("2025-01-02T06:30:00");
        LocalDateTime appointmentCreatedTime1 = LocalDateTime.now();

        doctor1 = doctorRepo.save(doctor1);

        Appointment appointment1 = new Appointment();
        appointment1.setPatient(patient);
        appointment1.setDoctor(doctor1);
        appointment1.setAppointmentDateTime(appointmentDateTime1);
        appointment1.setCreatedAt(appointmentCreatedTime1);

        appointment = appointmentRepo.save(appointment);
        appointment1 = appointmentRepo.save(appointment1);

        assertThat(appointmentRepo.existsByPatientId(patient.getId())).isTrue();

        int noOfRows = appointmentRepo.deleteAllByPatientId(patient.getId());

        assertThat(appointmentRepo.existsByPatientId(patient.getId())).isFalse();

        assertThat(noOfRows).isEqualTo(2);
    }

    @DisplayName(value = "JUnit test method for existsByDoctorId for an unsaved doctor")
    @Test
    public void givenUnsavedDoctorId_whenExistsByDoctorId_thenReturnFalse() {
        boolean doctorIsPresent = appointmentRepo.existsByDoctorId(1L);

        assertThat(doctorIsPresent).isFalse();
    }

    @DisplayName(value = "JUnit test method for existsByDoctorId method.")
    @Test
    public void givenSavedDoctorId_whenExistsByDoctorId_thenReturnTrue() {

        appointmentRepo.save(appointment);

        boolean doctorIsPresent = appointmentRepo.existsByDoctorId(doctor.getId());

        assertThat(doctorIsPresent).isTrue();
    }

    @DisplayName(value = "JUnit test method for deleteAllByDoctorId repo method.")
    @Test
    public void givenSavedDoctorId_whenDeleteAllByDoctorId_thenReturnNoOfRowsDeleted() {

        Patient patient1 = new Patient(
                null,
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
        LocalDateTime appointmentDateTime1 = LocalDateTime.parse("2025-01-02T06:30:00");
        LocalDateTime appointmentCreatedTime1 = LocalDateTime.now();

        patient1 = patientRepo.save(patient1);

        Appointment appointment1 = new Appointment();
        appointment1.setPatient(patient1);
        appointment1.setDoctor(doctor);
        appointment1.setAppointmentDateTime(appointmentDateTime1);
        appointment1.setCreatedAt(appointmentCreatedTime1);

        appointment = appointmentRepo.save(appointment);
        appointment1 = appointmentRepo.save(appointment1);

        assertThat(appointmentRepo.existsByDoctorId(doctor.getId())).isTrue();

        int noOfRows = appointmentRepo.deleteAllByDoctorId(doctor.getId());

        assertThat(appointmentRepo.existsByDoctorId(doctor.getId())).isFalse();

        assertThat(noOfRows).isEqualTo(2);
    }
    @DisplayName("JUnit test method for getAllByPatientId method")
    @Test
    public void givenPatientId_whenGetAllByPatientId_thenReturnListOfAppointments() {
        // Given
        LocalDateTime appointmentDateTime1 = LocalDateTime.parse("2025-01-03T06:30:00");
        LocalDateTime appointmentCreatedTime1 = LocalDateTime.now();

        Appointment appointment1 = new Appointment();
        appointment1.setPatient(patient);
        appointment1.setDoctor(doctor);
        appointment1.setAppointmentDateTime(appointmentDateTime1);
        appointment1.setCreatedAt(appointmentCreatedTime1);

        appointmentRepo.save(appointment);  // Save the first appointment
        appointmentRepo.save(appointment1); // Save a second appointment for the same patient

        // When
        List<Appointment> appointments = appointmentRepo.getAllByPatientId(patient.getId());

        // Then
        assertThat(appointments).isNotEmpty();
        assertThat(appointments.size()).isEqualTo(2);
        assertThat(appointments.get(0).getPatient()).isEqualTo(patient);
        assertThat(appointments.get(1).getPatient()).isEqualTo(patient);
    }

    @DisplayName("JUnit test method for existsByDoctorIdAndAppointmentDateTimeBetween method")
    @Test
    public void givenDoctorIdAndTimeRange_whenExistsByDoctorIdAndAppointmentDateTimeBetween_thenReturnTrue() {
        // Given
        LocalDateTime startTime = LocalDateTime.parse("2025-01-01T06:30:00");
        LocalDateTime endTime = LocalDateTime.parse("2025-01-01T06:59:00");

        appointmentRepo.save(appointment); // Saving an appointment within the time range

        // When
        boolean exists = appointmentRepo.existsByDoctorIdAndAppointmentDateTimeBetween(doctor.getId(), startTime, endTime);

        // Then
        assertThat(exists).isTrue();
    }

    @DisplayName("JUnit test method for existsByDoctorIdAndAppointmentDateTimeBetween method with no appointments")
    @Test
    public void givenDoctorIdAndTimeRange_whenExistsByDoctorIdAndAppointmentDateTimeBetween_thenReturnFalse() {
        // Given
        LocalDateTime startTime = LocalDateTime.parse("2025-01-01T00:00:00");
        LocalDateTime endTime = LocalDateTime.parse("2025-01-01T23:59:59");

        // When
        boolean exists = appointmentRepo.existsByDoctorIdAndAppointmentDateTimeBetween(doctor.getId(), startTime, endTime);

        // Then
        assertThat(exists).isFalse();
    }

}
