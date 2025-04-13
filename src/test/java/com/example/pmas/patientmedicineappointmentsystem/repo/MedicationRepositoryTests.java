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

        patient = patientRepo.save(patient);

        today = LocalDate.now();

        medication = new Medication(
                null,
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

    }

    @DisplayName(value="JUnit test for save medication method")
    @Test
    public void givenMedicationObject_whenSave_thenReturnSavedMedication(){
        Medication savedMedication = medicationRepo.save(medication);

        assertThat(savedMedication).isNotNull();
        assertThat(savedMedication.getId()).isGreaterThan(0);

        assertThat(savedMedication.getPatient()).isEqualTo(patient);

        assertThat(savedMedication).isEqualTo(medication);
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

        Medication medication1 = new Medication(
                null,
                patient,
                "Clear Gel",
                "Slight coat over the face",
                "Once in a day",
                "Complete",
                today,
                today.plusDays(14),
                today,
                today,
                "Before going to bed. Wash the face, let it dry, then apply the gel."
        );

        medication = medicationRepo.save(medication);
        medication1 = medicationRepo.save(medication1);

        List<Medication> medications = medicationRepo.findAll();

        assertThat(medications).isNotEmpty();
        assertThat(medications.size()).isEqualTo(2);

        assertThat(medications.get(0).getId()).isEqualTo(medication.getId());

        assertThat(medications.get(0).getPatient()).isEqualTo(patient);

        assertThat(medications.get(0)).isEqualTo(medication);

        assertThat(medications.get(1).getId()).isEqualTo(medication1.getId());

        assertThat(medications.get(1).getPatient()).isEqualTo(patient);

        assertThat(medications.get(1)).isEqualTo(medication1);
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

        assertThat(optionalMedication.get().getPatient()).isEqualTo(patient);

        assertThat(optionalMedication.get()).isEqualTo(medication);
    }

    @DisplayName(value="JUnit test for existsById repo method without medication object.")
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
        Medication medication1 = new Medication(
                null,
                patient,
                "Clear Gel",
                "Slight coat over the face",
                "Once in a day",
                "Complete",
                today,
                today.plusDays(14),
                today,
                today,
                "Before going to bed. Wash the face, let it dry, then apply the gel."
        );

        medication = medicationRepo.save(medication);
        medication1 = medicationRepo.save(medication1);

        assertThat(medicationRepo.existsByPatientId(patient.getId())).isTrue();

        int noOfRows = medicationRepo.deleteAllByPatientId(patient.getId());

        assertThat(noOfRows).isEqualTo(2);
        assertThat(medicationRepo.existsByPatientId(patient.getId())).isFalse();
    }
    @DisplayName("JUnit test method for findAllByPatientId method")
    @Test
    public void givenPatientId_whenFindAllByPatientId_thenReturnListOfMedications() {
        // Given

        Medication medication1 = new Medication(
                null,
                patient,
                "Clear Gel",
                "Slight coat over the face",
                "Once a day",
                "Complete",
                today,
                today.plusDays(14),
                today,
                today,
                "Before going to bed. Wash the face, let it dry, then apply the gel."
        );

        medicationRepo.save(medication);
        medicationRepo.save(medication1);

        // When
        List<Medication> medications = medicationRepo.findAllByPatientId(patient.getId());

        // Then
        assertThat(medications).isNotEmpty();
        assertThat(medications.size()).isEqualTo(2);
        assertThat(medications).containsExactlyInAnyOrder(medication1, medication);
    }

    @DisplayName("JUnit test method for findAllByPatientId when no medications exist for patient")
    @Test
    public void givenPatientId_whenFindAllByPatientId_thenReturnEmptyList() {
        // When
        List<Medication> medications = medicationRepo.findAllByPatientId(patient.getId());

        // Then
        assertThat(medications).isEmpty();
    }

}
