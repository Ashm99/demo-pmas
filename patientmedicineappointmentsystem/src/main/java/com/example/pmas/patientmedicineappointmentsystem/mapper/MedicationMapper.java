package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateMedicationDto;
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

    public static Medication mapToMedication(Patient patient, MedicationDto medicationDto){
        LocalDate startDate, endDate;
        // Checking if the start date provided is valid or not
        try{
            startDate = LocalDate.parse(medicationDto.getStartDate());
            if(startDate.isBefore(LocalDate.now())){
                throw new Exception();
            }
        } catch (Exception e){
            throw new DateTimeException("Enter valid medication start date.");
        }

        // Checking if the end date provided is valid or not
        try{
            endDate = LocalDate.parse(medicationDto.getEndDate());
            if(endDate.isBefore(LocalDate.now()) || endDate.isBefore(startDate)){
                throw new Exception();
            }
        } catch (Exception e){
            throw new DateTimeException("Enter valid medication end date.");
        }
        return new Medication(
                medicationDto.getId(),
                patient,
                medicationDto.getMedicine(),
                medicationDto.getFrequency(),
                startDate,
                endDate,
                medicationDto.getNotes()
        );
    }

    public static Medication mapToMedicationFromCreateMedicationDto(Patient patient, CreateMedicationDto createMedicationDto){
        LocalDate startDate, endDate;
        // Checking if the start date provided is valid or not
        try{
            startDate = LocalDate.parse(createMedicationDto.getStartDate());
            if(startDate.isBefore(LocalDate.now())){
                throw new Exception();
            }
        } catch (Exception e){
            throw new DateTimeException("Enter valid medication start date.");
        }

        // Checking if the end date provided is valid or not
        try{
            endDate = LocalDate.parse(createMedicationDto.getEndDate());
            if(endDate.isBefore(LocalDate.now()) || endDate.isBefore(startDate)){
                throw new Exception();
            }
        } catch (Exception e){
            throw new DateTimeException("Enter valid medication end date.");
        }
        return new Medication(
                null,
                patient,
                createMedicationDto.getMedicine(),
                createMedicationDto.getFrequency(),
                startDate,
                endDate,
                createMedicationDto.getNotes()
        );
    }
}
