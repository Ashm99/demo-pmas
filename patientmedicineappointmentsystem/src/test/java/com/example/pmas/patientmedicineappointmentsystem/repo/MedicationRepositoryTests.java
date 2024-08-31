package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Medication;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
public class MedicationRepositoryTests {
    @Autowired
    private MedicationRepo medicationRepo;
    @Autowired
    private PatientRepo patientRepo;
    private Patient patient;
    private Medication medication;
    private LocalDate today;

    @BeforeEach
    public void setup() {
        patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("jdoe@gmail.com");
        patient.setAddress("USA");
        patient.setMobile("9876543210");

        patient = patientRepo.save(patient);

        medication = new Medication();
        medication.setPatient(patient);
        medication.setMedicine("Dolo 650mg");
        medication.setFrequency("Twice a day");
        medication.setNotes("After breakfast and dinner.");

        today = LocalDate.now();
        medication.setStartDate(today);
        medication.setEndDate(today.plusDays(3));
    }

    @DisplayName(value="JUnit test for save medication method")
    @Test
    public void givenMedicationObject_whenSave_thenReturnSavedMedication(){
        Medication savedMedication = medicationRepo.save(medication);

        assertThat(savedMedication).isNotNull();
        assertThat(savedMedication.getId()).isGreaterThan(0);

        assertThat(savedMedication.getPatient().getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(savedMedication.getPatient().getLastName()).isEqualTo(patient.getLastName());
        assertThat(savedMedication.getPatient().getEmail()).isEqualTo(patient.getEmail());
        assertThat(savedMedication.getPatient().getMobile()).isEqualTo(patient.getMobile());
        assertThat(savedMedication.getPatient().getAddress()).isEqualTo(patient.getAddress());

        assertThat(savedMedication.getMedicine()).isEqualTo(medication.getMedicine());
        assertThat(savedMedication.getFrequency()).isEqualTo(medication.getFrequency());
        assertThat(savedMedication.getNotes()).isEqualTo(medication.getNotes());
        assertThat(savedMedication.getStartDate().toString()).isEqualTo(today.toString());
        assertThat(savedMedication.getEndDate().toString()).isEqualTo(today.plusDays(3).toString());
    }

    @DisplayName(value="JUnit test for find all medication method without medication object.")
    @Test
    public void givenNoMedicationList_whenFindAll_thenReturnEmptyList(){
        List<Medication> medications = medicationRepo.findAll();

        assertThat(medications).isEmpty();
    }

    @DisplayName(value="JUnit test for find all medication method.")
    @Test
    public void givenMedicationList_whenFindAll_thenReturnMedicationList(){

        Medication medication1 = new Medication();
        medication1.setPatient(patient);
        medication1.setMedicine("Clear Gel");
        medication1.setFrequency("Once in a day");
        medication1.setNotes("Before going to bed. Wash the face, let it dry, then apply the gel.");

        medication.setStartDate(today);
        medication.setEndDate(today.plusDays(14));

        medication = medicationRepo.save(medication);
        medication1 = medicationRepo.save(medication1);

        List<Medication> medications = medicationRepo.findAll();

        assertThat(medications).isNotEmpty();
        assertThat(medications.size()).isEqualTo(2);

        assertThat(medications.get(0).getId()).isEqualTo(medication.getId());

        assertThat(medications.get(0).getPatient().getId()).isEqualTo(patient.getId());
        assertThat(medications.get(0).getPatient().getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(medications.get(0).getPatient().getLastName()).isEqualTo(patient.getLastName());
        assertThat(medications.get(0).getPatient().getEmail()).isEqualTo(patient.getEmail());
        assertThat(medications.get(0).getPatient().getMobile()).isEqualTo(patient.getMobile());
        assertThat(medications.get(0).getPatient().getAddress()).isEqualTo(patient.getAddress());

        assertThat(medications.get(0).getMedicine()).isEqualTo(medication.getMedicine());
        assertThat(medications.get(0).getFrequency()).isEqualTo(medication.getFrequency());
        assertThat(medications.get(0).getNotes()).isEqualTo(medication.getNotes());
        assertThat(medications.get(0).getStartDate()).isEqualTo(medication.getStartDate());
        assertThat(medications.get(0).getEndDate()).isEqualTo(medication.getEndDate());

        assertThat(medications.get(1).getId()).isEqualTo(medication1.getId());

        assertThat(medications.get(1).getPatient().getId()).isEqualTo(patient.getId());
        assertThat(medications.get(1).getPatient().getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(medications.get(1).getPatient().getLastName()).isEqualTo(patient.getLastName());
        assertThat(medications.get(1).getPatient().getEmail()).isEqualTo(patient.getEmail());
        assertThat(medications.get(1).getPatient().getMobile()).isEqualTo(patient.getMobile());
        assertThat(medications.get(1).getPatient().getAddress()).isEqualTo(patient.getAddress());

        assertThat(medications.get(1).getMedicine()).isEqualTo(medication1.getMedicine());
        assertThat(medications.get(1).getFrequency()).isEqualTo(medication1.getFrequency());
        assertThat(medications.get(1).getNotes()).isEqualTo(medication1.getNotes());
        assertThat(medications.get(1).getStartDate()).isEqualTo(medication1.getStartDate());
        assertThat(medications.get(1).getEndDate()).isEqualTo(medication1.getEndDate());
    }

    @DisplayName(value="JUnit test for find medication by id without medication object.")
    @Test
    public void givenNoMedicationObject_whenFindById_thenReturnEmptyOptionalObject(){
        Optional<Medication> optionalMedication = medicationRepo.findById(1L);

        assertThat(optionalMedication).isEmpty();
    }

    @DisplayName(value="JUnit test for find medication by id.")
    @Test
    public void givenMedicationObject_whenFindById_thenReturnMedicationObject(){
        Long id = medicationRepo.save(medication).getId();
        Optional<Medication> optionalMedication = medicationRepo.findById(id);

        assertThat(optionalMedication).isNotEmpty();

        assertThat(optionalMedication.get().getId()).isEqualTo(medication.getId());

        assertThat(optionalMedication.get().getPatient().getId()).isEqualTo(patient.getId());
        assertThat(optionalMedication.get().getPatient().getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(optionalMedication.get().getPatient().getLastName()).isEqualTo(patient.getLastName());
        assertThat(optionalMedication.get().getPatient().getEmail()).isEqualTo(patient.getEmail());
        assertThat(optionalMedication.get().getPatient().getMobile()).isEqualTo(patient.getMobile());
        assertThat(optionalMedication.get().getPatient().getAddress()).isEqualTo(patient.getAddress());

        assertThat(optionalMedication.get().getMedicine()).isEqualTo(medication.getMedicine());
        assertThat(optionalMedication.get().getFrequency()).isEqualTo(medication.getFrequency());
        assertThat(optionalMedication.get().getNotes()).isEqualTo(medication.getNotes());
        assertThat(optionalMedication.get().getStartDate()).isEqualTo(medication.getStartDate());
        assertThat(optionalMedication.get().getEndDate()).isEqualTo(medication.getEndDate());
    }

    @DisplayName(value="JUnit test for exxistsById repo method without medication object.")
    @Test
    public void givenNoMedicationObject_whenExistsById_thenReturnFalse(){
        boolean medicationIsPresent = medicationRepo.existsById(1L);

        assertThat(medicationIsPresent).isFalse();
    }

    @DisplayName(value="JUnit test for existsById repo method.")
    @Test
    public void givenMedicationObject_whenExistsById_thenReturnTrue(){
        Long id = medicationRepo.save(medication).getId();
        boolean medicationIsPresent = medicationRepo.existsById(id);

        assertThat(medicationIsPresent).isTrue();
    }

    @DisplayName(value="JUnit test for deleteById repo method.")
    @Test
    public void givenMedicationObject_whenDeleteById_thenRemoveMedication(){
        Long id = medicationRepo.save(medication).getId();

        assertThat(medicationRepo.existsById(id)).isTrue();

        medicationRepo.deleteById(id);

        assertThat(medicationRepo.existsById(id)).isFalse();
    }

    @DisplayName(value="JUnit test for existsByPatientId repo method without medication.")
    @Test
    public void givenNoMedicationObject_whenExistsByPatientId_thenReturnTrue(){
        boolean medicationIsPresent = medicationRepo.existsByPatientId(patient.getId());

        assertThat(medicationIsPresent).isFalse();
    }

    @DisplayName(value="JUnit test for existsByPatientId repo method.")
    @Test
    public void givenMedicationObject_whenExistsByPatientId_thenReturnTrue(){
        medicationRepo.save(medication);
        boolean medicationIsPresent = medicationRepo.existsByPatientId(patient.getId());

        assertThat(medicationIsPresent).isTrue();
    }

    @DisplayName(value="JUnit test for deleteAllByPatientId repo method.")
    @Test
    public void givenMedicationObject_whenDeleteAllByPatientId_thenRemoveAllMedicationForPatient(){
        Medication medication1 = new Medication();
        medication1.setPatient(patient);
        medication1.setMedicine("Clear Gel");
        medication1.setFrequency("Once in a day");
        medication1.setNotes("Before going to bed. Wash the face, let it dry, then apply the gel.");

        medication.setStartDate(today);
        medication.setEndDate(today.plusDays(14));

        medication = medicationRepo.save(medication);
        medication1 = medicationRepo.save(medication1);

        assertThat(medicationRepo.existsByPatientId(patient.getId())).isTrue();

        int noOfRows = medicationRepo.deleteAllByPatientId(patient.getId());

        assertThat(noOfRows).isEqualTo(2);
        assertThat(medicationRepo.existsByPatientId(patient.getId())).isFalse();
    }
}
