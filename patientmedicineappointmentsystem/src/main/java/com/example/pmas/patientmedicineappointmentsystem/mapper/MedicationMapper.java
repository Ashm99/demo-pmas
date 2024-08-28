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
                medication.getFrequency(),
                medication.getStartDate().toString(),
                medication.getEndDate().toString(),
                medication.getNotes()
        );
    }

    public static Medication mapToMedicationFromSaveMedicationDto(Long id, Patient patient, SaveMedicationDto saveMedicationDto){
        LocalDate startDate, endDate;
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
                saveMedicationDto.getFrequency().trim(),
                startDate,
                endDate,
                saveMedicationDto.getNotes().trim()
        );
    }
}
