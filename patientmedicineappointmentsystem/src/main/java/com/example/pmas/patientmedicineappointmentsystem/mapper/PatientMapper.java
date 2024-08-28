package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SavePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;

public class PatientMapper {
    public static PatientDto mapToPatientDto(Patient patient){
        return new PatientDto(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getMobile(),
                patient.getAddress()
        );
    }

    public static Patient mapToPatientFromSavePatientDto(Long id, SavePatientDto savePatientDto){
        return new Patient(
                id,
                savePatientDto.getFirstName().trim(),
                savePatientDto.getLastName().trim(),
                savePatientDto.getEmail().trim(),
                savePatientDto.getMobile(),
                savePatientDto.getAddress().trim()
        );
    }
}
