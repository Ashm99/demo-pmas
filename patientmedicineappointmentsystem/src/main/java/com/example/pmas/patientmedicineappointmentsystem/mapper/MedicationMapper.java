package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.CreateMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Medication;

public class MedicationMapper {
    public static MedicationDto mapToMedicationDto(Medication medication){
        return new MedicationDto(
                medication.getId(),
//                PatientMapper.mapToPatientDto(medication.getPatient()),
                medication.getPatient().getId(),
                medication.getMedicine(),
                medication.getFrequency(),
                medication.getStartDate(),
                medication.getEndDate(),
                medication.getNotes()
        );
    }

    public static Medication mapToMedication(MedicationDto medicationDto){
        return new Medication(
                medicationDto.getId(),
                null,
                medicationDto.getMedicine(),
                medicationDto.getFrequency(),
                medicationDto.getStartDate(),
                medicationDto.getEndDate(),
                medicationDto.getNotes()
        );
    }

    public static Medication mapToMedicationFromCreateMedicationDto(CreateMedicationDto createMedicationDto){
        return new Medication(
                null,
                null,
                createMedicationDto.getMedicine(),
                createMedicationDto.getFrequency(),
                createMedicationDto.getStartDate(),
                createMedicationDto.getEndDate(),
                createMedicationDto.getNotes()
        );
    }
}
