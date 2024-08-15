package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.CreatePatientDto;
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

    public static Patient mapToPatient(PatientDto patientDto){
        return new Patient(
                patientDto.getId(),
                patientDto.getFirstName(),
                patientDto.getLastName(),
                patientDto.getEmail(),
                patientDto.getMobile(),
                patientDto.getAddress()
        );
    }

    public static Patient mapToPatientFromCreatePatientDto(CreatePatientDto patientDto){
        return new Patient(
                null,
                patientDto.getFirstName(),
                patientDto.getLastName(),
                patientDto.getEmail(),
                patientDto.getMobile(),
                patientDto.getAddress()
        );
    }
}
