package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Medication;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;

public class MedicationMapper {
    public static MedicationDto mapToMedicationDto(Medication medication){
        return new MedicationDto(
                medication.getId(),
                medication.getPatient().getId(),
                medication.getMedicine(),
                medication.getFrequency(),
                medication.getStartDate(),
                medication.getEndDate(),
                medication.getNotes()
        );
    }

    public static Medication mapToMedication(Patient patient, MedicationDto medicationDto){
        return new Medication(
                medicationDto.getId(),
                patient,
                medicationDto.getMedicine(),
                medicationDto.getFrequency(),
                medicationDto.getStartDate(),
                medicationDto.getEndDate(),
                medicationDto.getNotes()
        );
    }

    public static Medication mapToMedicationFromCreateMedicationDto(Patient patient, CreateMedicationDto createMedicationDto){
        return new Medication(
                null,
                patient,
                createMedicationDto.getMedicine(),
                createMedicationDto.getFrequency(),
                createMedicationDto.getStartDate(),
                createMedicationDto.getEndDate(),
                createMedicationDto.getNotes()
        );
    }
}
