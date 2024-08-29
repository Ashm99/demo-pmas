package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Will load the Entity and JPA related classes -> The repository layer
public class PatientRepositoryTests {
    @Autowired
    private PatientRepo patientRepo;
    private Patient patient;

    @BeforeEach
    public void setUp(){
        patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("jdoe@gmail.com");
        patient.setAddress("USA");
        patient.setMobile("9876543210");
    }
    @DisplayName(value = "JUnit test for save patient method")
    @Test
    public void givenPatientObject_whenSave_thenReturnSavedPatient(){

        Patient savedPatient = patientRepo.save(patient);

        assertThat(savedPatient).isNotNull();
        assertThat(savedPatient.getId()).isGreaterThan(0);
        assertThat(savedPatient.getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(savedPatient.getLastName()).isEqualTo(patient.getLastName());
        assertThat(savedPatient.getEmail()).isEqualTo(patient.getEmail());
        assertThat(savedPatient.getAddress()).isEqualTo(patient.getAddress());
        assertThat(savedPatient.getMobile()).isEqualTo(patient.getMobile());
    }

    @DisplayName(value = "JUnit test for find all patient method")
    @Test
    public void givenPatientList_whenFind_thenReturnPatientList (){
        Patient patient1 = new Patient();
        patient1.setFirstName("John");
        patient1.setLastName("Smith");
        patient1.setEmail("jsmith@gmail.com");
        patient1.setAddress("UK");
        patient1.setMobile("99876543210");

        patientRepo.save(patient);
        patientRepo.save(patient1);

        List<Patient> patientList = patientRepo.findAll();

        assertThat(patientList).isNotNull();
        assertThat(patientList.size()).isEqualTo(2);

        assertThat(patientList.get(0).getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(patientList.get(0).getLastName()).isEqualTo(patient.getLastName());
        assertThat(patientList.get(0).getEmail()).isEqualTo(patient.getEmail());
        assertThat(patientList.get(0).getAddress()).isEqualTo(patient.getAddress());
        assertThat(patientList.get(0).getMobile()).isEqualTo(patient.getMobile());

        assertThat(patientList.get(1).getFirstName()).isEqualTo(patient1.getFirstName());
        assertThat(patientList.get(1).getLastName()).isEqualTo(patient1.getLastName());
        assertThat(patientList.get(1).getEmail()).isEqualTo(patient1.getEmail());
        assertThat(patientList.get(1).getAddress()).isEqualTo(patient1.getAddress());
        assertThat(patientList.get(1).getMobile()).isEqualTo(patient1.getMobile());
    }

    @DisplayName(value = "JUnit test for find by patient id method")
    @Test
    public void givenPatient_whenFindById_thenReturnPatient (){

        Patient savedPatient = patientRepo.save(patient);
        Long id = savedPatient.getId();

        Patient patient1 = patientRepo.findById(id).get();

        assertThat(patient1).isNotNull();
        assertThat(patient1.getId()).isGreaterThan(0);
        assertThat(patient1.getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(patient1.getLastName()).isEqualTo(patient.getLastName());
        assertThat(patient1.getEmail()).isEqualTo(patient.getEmail());
        assertThat(patient1.getAddress()).isEqualTo(patient.getAddress());
        assertThat(patient1.getMobile()).isEqualTo(patient.getMobile());
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

        Patient savedPatient = patientRepo.save(patient);
        Long id = savedPatient.getId();

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


}
