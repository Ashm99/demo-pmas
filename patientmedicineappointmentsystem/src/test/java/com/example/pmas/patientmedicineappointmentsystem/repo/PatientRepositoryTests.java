package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest // Will load the Entity and JPA related classes -> The repository layer
public class PatientRepositoryTests {
    @Autowired
    private PatientRepo patientRepo;
    private Patient patient;

    @BeforeEach
    public void setUp(){
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
    }
    @DisplayName(value = "JUnit test for save patient method")
    @Test
    public void givenPatientObject_whenSave_thenReturnSavedPatient(){

        Patient savedPatient = patientRepo.save(patient);

        assertThat(savedPatient).isNotNull();
        assertThat(savedPatient.getId()).isGreaterThan(0);
        assertThat(savedPatient).isEqualTo(patient);
    }

    @DisplayName(value = "JUnit test for find all patient method")
    @Test
    public void givenPatientList_whenFind_thenReturnPatientList (){
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

        patientRepo.save(patient);
        patientRepo.save(patient1);

        List<Patient> patientList = patientRepo.findAll();

        assertThat(patientList).isNotNull();
        assertThat(patientList.size()).isEqualTo(2);

        assertThat(patientList.get(0).getId()).isGreaterThan(0);
        assertThat(patientList.get(0)).isEqualTo(patient);

        assertThat(patientList.get(1).getId()).isGreaterThan(0);
        assertThat(patientList.get(1)).isEqualTo(patient1);
    }

    @DisplayName(value = "JUnit test for find by patient id method")
    @Test
    public void givenPatient_whenFindById_thenReturnPatient (){

        Patient savedPatient = patientRepo.save(patient);
        Long id = savedPatient.getId();

        Patient patient1 = patientRepo.findById(id).get();

        assertThat(patient1).isNotNull();
        assertThat(patient1.getId()).isGreaterThan(0);
        assertThat(patient1).isEqualTo(patient);
    }

    @DisplayName(value = "JUnit test for find by patient id method without patient")
    @Test
    public void givenNoPatient_whenFindById_thenReturnEmptyOptional(){

        Optional<Patient> savedPatient = patientRepo.findById(1L);

        assertThat(savedPatient).isEmpty();
    }

    @DisplayName(value = "JUnit test for exists by patient id method")
    @Test
    public void givenPatient_whenExistsById_thenReturnTrue(){

        Long id  = patientRepo.save(patient).getId();

        boolean result = patientRepo.existsById(id);

        assertThat(result).isTrue();
    }

    @DisplayName(value = "JUnit test for exists by patient id method without patient")
    @Test
    public void givenNoPatient_whenExistsById_thenReturnFalse(){

        boolean result = patientRepo.existsById(1L);

        assertThat(result).isFalse();
    }

    @DisplayName(value = "JUnit test for delete by patient id method")
    @Test
    public void givenPatient_whenDeleteById_thenRemovePatient(){

        Patient savedPatient = patientRepo.save(patient);
        Long id = savedPatient.getId();
        patientRepo.deleteById(id);
        Optional<Patient> optionalPatient = patientRepo.findById(id);

        assertThat(optionalPatient).isEmpty();

    }

    @DisplayName("JUnit test method for findByMobile method - Positive case")
    @Test
    public void givenMobile_whenFindByMobile_thenReturnDoctor() {
        // Given
        patientRepo.save(patient);

        // When
        Optional<Patient> fetchedPatient = patientRepo.findByMobile("9876543210");

        // Then
        assertThat(fetchedPatient).isPresent();
        assertThat(fetchedPatient.get()).isEqualTo(patient);
    }

    @DisplayName("JUnit test method for findByMobile method when no doctor found")
    @Test
    public void givenInvalidMobile_whenFindByMobile_thenReturnEmptyOptional() {
        // Given nothing

        // When
        Optional<Patient> fetchedPatient = patientRepo.findByMobile("9876543210");

        // Then
        assertThat(fetchedPatient).isEmpty();
    }
}
