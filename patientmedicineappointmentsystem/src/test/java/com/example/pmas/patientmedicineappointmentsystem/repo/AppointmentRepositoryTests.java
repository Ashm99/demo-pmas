package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("jdoe@gmail.com");
        patient.setAddress("USA");
        patient.setPassword("John@2024");
        patient.setMobile("9876543210");

        doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpeciality("General");

        patient = patientRepo.save(patient);
        doctor = doctorRepo.save(doctor);

        appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        Instant appointmentDateTime = Instant.parse("2025-01-01T06:30:00Z");
        appointment.setAppointmentDateTime(appointmentDateTime);
        Instant appointmentCreatedTime = Instant.now();
        appointment.setCreatedAt(appointmentCreatedTime);

    }

    @DisplayName(value = "JUnit test method for save appointment method")
    @Test
    public void givenAppointmentObject_whenSave_thenSaveAndReturnAppointmentObject() {
        Appointment savedAppointment = appointmentRepo.save(appointment);

        assertThat(savedAppointment).isNotNull();

        assertThat(savedAppointment.getPatient().getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(savedAppointment.getPatient().getLastName()).isEqualTo(patient.getLastName());
        assertThat(savedAppointment.getPatient().getEmail()).isEqualTo(patient.getEmail());
        assertThat(savedAppointment.getPatient().getMobile()).isEqualTo(patient.getMobile());
        assertThat(savedAppointment.getPatient().getPassword()).isEqualTo(patient.getPassword());
        assertThat(savedAppointment.getPatient().getAddress()).isEqualTo(patient.getAddress());

        assertThat(savedAppointment.getDoctor().getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(savedAppointment.getDoctor().getLastName()).isEqualTo(doctor.getLastName());
        assertThat(savedAppointment.getDoctor().getSpeciality()).isEqualTo(doctor.getSpeciality());

        assertThat(savedAppointment.getAppointmentDateTime().toString()).isEqualTo(appointment.getAppointmentDateTime().toString());
        assertThat(savedAppointment.getCreatedAt().toString()).isEqualTo(appointment.getCreatedAt().toString());

    }

    @DisplayName("JUnit test method for find all appointment repo method")
    @Test
    public void givenAppointmentList_whenFindAll_thenReturnAppointmentList() {

        Doctor doctor1 = new Doctor();
        doctor1.setFirstName("Rebecca");
        doctor1.setLastName("Johnson");
        doctor1.setSpeciality("Orthopedics");
        Instant appointmentDateTime1 = Instant.parse("2025-01-02T06:30:00Z");
        Instant appointmentCreatedTime1 = Instant.now();

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

        assertThat(appointments.get(0).getPatient().getId()).isEqualTo(patient.getId());
        assertThat(appointments.get(0).getPatient().getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(appointments.get(0).getPatient().getLastName()).isEqualTo(patient.getLastName());
        assertThat(appointments.get(0).getPatient().getEmail()).isEqualTo(patient.getEmail());
        assertThat(appointments.get(0).getPatient().getMobile()).isEqualTo(patient.getMobile());
        assertThat(appointments.get(0).getPatient().getPassword()).isEqualTo(patient.getPassword());
        assertThat(appointments.get(0).getPatient().getAddress()).isEqualTo(patient.getAddress());

        assertThat(appointments.get(0).getDoctor().getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(appointments.get(0).getDoctor().getLastName()).isEqualTo(doctor.getLastName());
        assertThat(appointments.get(0).getDoctor().getSpeciality()).isEqualTo(doctor.getSpeciality());

        assertThat(appointments.get(0).getAppointmentDateTime().toString()).isEqualTo(appointment.getAppointmentDateTime().toString());
        assertThat(appointments.get(0).getCreatedAt().toString()).isEqualTo(appointment.getCreatedAt().toString());

        assertThat(appointments.get(1).getPatient().getId()).isEqualTo(patient.getId());
        assertThat(appointments.get(1).getPatient().getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(appointments.get(1).getPatient().getLastName()).isEqualTo(patient.getLastName());
        assertThat(appointments.get(1).getPatient().getEmail()).isEqualTo(patient.getEmail());
        assertThat(appointments.get(1).getPatient().getMobile()).isEqualTo(patient.getMobile());
        assertThat(appointments.get(1).getPatient().getPassword()).isEqualTo(patient.getPassword());
        assertThat(appointments.get(1).getPatient().getAddress()).isEqualTo(patient.getAddress());

        assertThat(appointments.get(1).getDoctor().getFirstName()).isEqualTo(doctor1.getFirstName());
        assertThat(appointments.get(1).getDoctor().getLastName()).isEqualTo(doctor1.getLastName());
        assertThat(appointments.get(1).getDoctor().getSpeciality()).isEqualTo(doctor1.getSpeciality());

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

        assertThat(optionalAppointment.get().getPatient().getId()).isEqualTo(patient.getId());
        assertThat(optionalAppointment.get().getPatient().getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(optionalAppointment.get().getPatient().getLastName()).isEqualTo(patient.getLastName());
        assertThat(optionalAppointment.get().getPatient().getEmail()).isEqualTo(patient.getEmail());
        assertThat(optionalAppointment.get().getPatient().getMobile()).isEqualTo(patient.getMobile());
        assertThat(optionalAppointment.get().getPatient().getPassword()).isEqualTo(patient.getPassword());
        assertThat(optionalAppointment.get().getPatient().getAddress()).isEqualTo(patient.getAddress());

        assertThat(optionalAppointment.get().getDoctor().getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(optionalAppointment.get().getDoctor().getLastName()).isEqualTo(doctor.getLastName());
        assertThat(optionalAppointment.get().getDoctor().getSpeciality()).isEqualTo(doctor.getSpeciality());

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

        Doctor doctor1 = new Doctor();
        doctor1.setFirstName("Rebecca");
        doctor1.setLastName("Johnson");
        doctor1.setSpeciality("Orthopedics");
        Instant appointmentDateTime1 = Instant.parse("2025-01-02T06:30:00Z");
        Instant appointmentCreatedTime1 = Instant.now();

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

        Patient patient1 = new Patient();
        patient1.setFirstName("Vidharan");
        patient1.setLastName("M");
        patient1.setEmail("vidhu@gmail.com");
        patient1.setMobile("9753186420");
        patient1.setPassword("Vidharan@2024 ");
        patient1.setAddress("India");
        Instant appointmentDateTime1 = Instant.parse("2025-01-02T06:30:00Z");
        Instant appointmentCreatedTime1 = Instant.now();

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
}
