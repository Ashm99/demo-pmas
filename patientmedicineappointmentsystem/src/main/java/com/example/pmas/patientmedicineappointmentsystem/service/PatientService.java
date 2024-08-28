package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SavePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;

import java.util.List;

public interface PatientService {

    List<PatientDto> getAllPatients();

    PatientDto getPatientById(Long id);

    PatientDto addPatient(SavePatientDto savePatientDto);

    PatientDto updatePatient(Long id, SavePatientDto savePatientDto);

    void deletePatientById(Long id);
}
