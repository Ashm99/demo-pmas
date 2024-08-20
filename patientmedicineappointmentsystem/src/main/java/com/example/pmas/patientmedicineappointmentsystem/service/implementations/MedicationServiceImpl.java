package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.CreateMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.MedicationMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Medication;
import com.example.pmas.patientmedicineappointmentsystem.repo.MedicationRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.MedicationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class MedicationServiceImpl implements MedicationService {
    private MedicationRepo medicationRepo;
    private PatientRepo patientRepo;

    /**
     * A service method to get all the medication irrespective of the patient
     * @return All the medication list in the database.
     */
    @Override
    public List<MedicationDto> getAllMedication() {
        List<MedicationDto> medicationDtos = new ArrayList<>();
        medicationRepo.findAll().forEach(
                medication -> medicationDtos.add(MedicationMapper.mapToMedicationDto(medication))
        );
        if(medicationDtos.isEmpty())
            throw new NoSuchElementException("No medication exists in the database for any patient.");
        return medicationDtos;
    }

    /**
     * A service method to get a specific medication with medication id irrespective of the patient
     * @param id Medication id
     * @return A MedicationDto object
     */
    @Override
    public MedicationDto getMedicationById(Long id) {
        Medication medication = medicationRepo.findById(id).orElseThrow(
                ()->  new NoSuchElementException("No Medication exists with ID: "+ id + ".")
        );
        return MedicationMapper.mapToMedicationDto(medication);
    }

    /**
     * A service method to create a medication object.
     * @param createMedicationDto Input data for medication creation.
     * @return A Medication Dto object on successful creation of the medication object.
     */
    @Override
    public MedicationDto createMedication(CreateMedicationDto createMedicationDto) {
        Medication medication = MedicationMapper.mapToMedicationFromCreateMedicationDto(createMedicationDto);
        medication.setPatient(patientRepo.findById(createMedicationDto.getPatientId()).orElseThrow(
                ()->new NoSuchElementException("No patient exists as per the patient id given.")
        ));
        Medication savedMedication = medicationRepo.save(medication);
        return MedicationMapper.mapToMedicationDto(savedMedication);
    }

    /**
     * A service method to update a medication object through given object's id.
     * @param medicationDto Input data for medication updation.
     * @return A Medication Dto object on successful update of the medication object.
     */
    @Override
    public MedicationDto updateMedication(MedicationDto medicationDto) {
        if(!medicationRepo.existsById(medicationDto.getId())){
            throw new NoSuchElementException("No medication exists under the given medication's id.");
        }
        Medication medication = MedicationMapper.mapToMedication(medicationDto);
        medication.setPatient(patientRepo.findById(medicationDto.getPatientId()).orElseThrow(
                ()->new NoSuchElementException("No patient exists under given medication's patient id")
        ));
        Medication savedMedication = medicationRepo.save(medication);
        return MedicationMapper.mapToMedicationDto(savedMedication);
    }

    /**
     * A service method to update a medication object through given object's id.
     * @param id The id of the medication to be deleted.
     * @return A response string based on the outcome.
     */
    @Override
    public String deleteMedication(Long id) {
        if(!medicationRepo.existsById(id)){
            throw new NoSuchElementException("No medication exists under the given medication's id.");
        }
        patientRepo.deleteById(id);
        if(medicationRepo.existsById(id)){
            throw new NoSuchElementException("Error occurred. Medication exists even after deletion.");
        }
        return "Successfully deleted medication with id: " + id + ".";
    }
}
