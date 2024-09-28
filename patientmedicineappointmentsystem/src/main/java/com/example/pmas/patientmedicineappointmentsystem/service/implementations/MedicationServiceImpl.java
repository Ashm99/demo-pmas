package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.MedicationMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Medication;
import com.example.pmas.patientmedicineappointmentsystem.repo.MedicationRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MedicationServiceImpl implements MedicationService {
    private MedicationRepo medicationRepo;
    private PatientRepo patientRepo;

    /**
     * A service method to get all the medication irrespective of the patient
     *
     * @return All the medication list in the database.
     */
    @Override
    public List<MedicationDto> getAllMedication() {
        List<MedicationDto> medicationDtos = new ArrayList<>();
        medicationRepo.findAll().forEach(
                medication -> medicationDtos.add(MedicationMapper.mapToMedicationDto(medication))
        );
        if (medicationDtos.isEmpty())
            throw new NoSuchElementException("No medication exists in the database for any patient.");
        return medicationDtos;
    }

    /**
     * A service method to get a specific medication with medication id irrespective of the patient
     *
     * @param id Medication id
     * @return A MedicationDto object
     */
    @Override
    public MedicationDto getMedicationById(Long id) {
        Medication medication = medicationRepo.findById(id).orElseThrow(
                () -> new NoSuchElementException("No Medication exists under given id: " + id + ".")
        );
        return MedicationMapper.mapToMedicationDto(medication);
    }

    /**
     * A service method to create a medication object.
     *
     * @param saveMedicationDto Input data for medication creation.
     * @return A Medication Dto object on successful creation of the medication object.
     */
    @Override
    public MedicationDto saveMedication(SaveMedicationDto saveMedicationDto) {
        Medication medication = MedicationMapper.mapToMedicationFromSaveMedicationDto(
                null,
                patientRepo.findById(Long.parseLong(saveMedicationDto.getPatientId())).orElseThrow(
                        () -> new NoSuchElementException("No patient exists as per the patient id given.")
                ),
                LocalDate.now(),
                saveMedicationDto
        );
        Medication savedMedication = medicationRepo.save(medication);
        return MedicationMapper.mapToMedicationDto(savedMedication);
    }

    /**
     * A service method to update a medication object through given object's id.
     *
     * @param saveMedicationDto Input data for medication updation.
     * @return A Medication Dto object on successful update of the medication object.
     */
    @Override
    public MedicationDto updateMedication(Long id, LocalDate prescriptionDate, SaveMedicationDto saveMedicationDto) {
        Long updatedPatientId = Long.parseLong(saveMedicationDto.getPatientId().trim());
        if (!medicationRepo.existsById(id)) {
            throw new NoSuchElementException("No medication exists under the given medication's id.");
        }
        if (!Objects.equals(medicationRepo.findById(id).get().getPatient().getId(), updatedPatientId)) {
            System.out.println(medicationRepo.findById(id).get().getPatient().getId());
            System.out.println(updatedPatientId);
            throw new NoSuchElementException("No such medication exists for the mentioned patient's id.");
        }
        Medication medication = MedicationMapper.mapToMedicationFromSaveMedicationDto(
                id,
                patientRepo.findById(updatedPatientId).orElseThrow(
                        () -> new NoSuchElementException("No patient exists under given medication's patient id.")
                ),
                prescriptionDate,
                saveMedicationDto
        );
        Medication savedMedication = medicationRepo.save(medication);
        return MedicationMapper.mapToMedicationDto(savedMedication);
    }

    /**
     * A service method to update a medication object through given object's id.
     *
     * @param id The id of the medication to be deleted.
     * @return A response string based on the outcome.
     */
    @Override
    public String deleteMedication(Long id) {
        if (!medicationRepo.existsById(id)) {
            throw new NoSuchElementException("Deletion not possible as no medication exists under the given medication id: " + id + ".");
        }
        medicationRepo.deleteById(id);
        if (medicationRepo.existsById(id)) {
            throw new NoSuchElementException("Error occurred. Medication exists even after deletion.");
        }
        return "Successfully deleted medication with id: " + id + ".";
    }


    /**
     * A Service method to find if there are medications of a particular patient.
     *
     * @param patientId The id of the patient whose all medications are to be checked.
     * @return True if present and vice versa.
     */
    @Override
    public boolean existsByPatientId(Long patientId) {
        return medicationRepo.existsByPatientId(patientId);
    }

    /**
     * A Service method to delete all the medications of a particular patient.
     *
     * @param patientId The id of the doctor whose all medications are to be deleted.
     */
    @Override
    public void deleteAllMedicationByPatientId(Long patientId) {
        int rows = medicationRepo.deleteAllByPatientId(patientId);
        System.out.printf("%d rows deleted from the medication table.%n", rows);
        if (this.existsByPatientId(patientId)) {
            throw new RuntimeException("Error while deleting appointments of patient with id: " + patientId + ".");
        }
    }

    /**
     * A method to get the medications for a particular patient with their username
     * @param username Username of the patient
     * @return List of medications of the patient
     */
    @Override
    public List<MedicationDto> getAllMedicationByUsername(String username) {
        Long patientId = patientRepo.findByMobile(username).get().getId();
        List<MedicationDto> medicationDtoList = new ArrayList<>();
        medicationRepo.findAllByPatientId(patientId).forEach(
                medication -> medicationDtoList.add(MedicationMapper.mapToMedicationDto(medication))
        );
        return medicationDtoList;
    }

    /**
     * @param username Patient username
     * @return Patient Id as a String
     */
    @Override
    public String getPatientIdByUsername(String username) {
        return patientRepo.findByMobile(username).get().getId().toString();
    }
}
