package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SavePatientDto;

import java.util.List;

public interface PatientService{

    List<PatientDto> getAllPatients();

    PatientDto getPatientById(Long id);

    PatientDto getPatientByUsername(String username);

    String getPatientFirstnameByUsername(String username);

    PatientDto addPatient(SavePatientDto savePatientDto);

    PatientDto updatePatient(Long id, SavePatientDto savePatientDto);

    void deletePatientById(Long id);
}
