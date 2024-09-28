package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Medication;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;

import java.time.DateTimeException;
import java.time.LocalDate;

public class MedicationMapper {
    public static MedicationDto mapToMedicationDto(Medication medication){
        return new MedicationDto(
                medication.getId(),
                medication.getPatient().getId(),
                medication.getMedicine(),
                medication.getDosage(),
                medication.getFrequency(),
                medication.getStatus(),
                medication.getStartDate().toString(),
                medication.getEndDate().toString(),
                medication.getPrescriptionDate().toString(),
                medication.getUpdatedDate().toString(),
                medication.getNotes()
        );
    }

    public static Medication mapToMedicationFromSaveMedicationDto(Long id, Patient patient, LocalDate prescriptionDate, SaveMedicationDto saveMedicationDto){
        LocalDate startDate, endDate, today;
        today = LocalDate.now();
        // Checking if the start date provided is valid or not
        try{
            startDate = LocalDate.parse(saveMedicationDto.getStartDate());
            if(startDate.isBefore(LocalDate.now())){
                throw new Exception();
            }
        } catch (Exception e){
            throw new DateTimeException("Enter valid medication start date.");
        }

        // Checking if the end date provided is valid or not
        try{
            endDate = LocalDate.parse(saveMedicationDto.getEndDate());
            if(endDate.isBefore(LocalDate.now()) || endDate.isBefore(startDate)){
                throw new Exception();
            }
        } catch (Exception e){
            throw new DateTimeException("Enter valid medication end date.");
        }
        return new Medication(
                id,
                patient,
                saveMedicationDto.getMedicine().trim(),
                saveMedicationDto.getDosage().trim(),
                saveMedicationDto.getFrequency().trim(),
                saveMedicationDto.getStatus().trim(),
                startDate,
                endDate,
                prescriptionDate,
                today,
                saveMedicationDto.getNotes().trim()
        );
    }
}
