package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.CreatePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;

import java.util.List;

public interface PatientService {

    List<PatientDto> getAllPatients();

    PatientDto getPatientById(Long id);

    PatientDto addPatient(CreatePatientDto patientDto);

    PatientDto updatePatient(PatientDto patientDto);

    void deletePatientById(Long id);
}
