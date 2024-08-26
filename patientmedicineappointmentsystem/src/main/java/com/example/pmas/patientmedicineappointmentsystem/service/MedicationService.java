package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;

import java.util.List;

public interface MedicationService {
    List<MedicationDto> getAllMedication();

    MedicationDto getMedicationById(Long id);

    MedicationDto createMedication(CreateMedicationDto createMedicationDto);

    MedicationDto updateMedication(MedicationDto medicationDto);

    String deleteMedication(Long id);

    boolean existsByPatientId(Long id);

    void deleteAllMedicationByPatientId(Long id);
}
