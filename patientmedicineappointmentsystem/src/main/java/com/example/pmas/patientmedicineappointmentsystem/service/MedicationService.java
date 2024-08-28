package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;

import java.util.List;

public interface MedicationService {
    List<MedicationDto> getAllMedication();

    MedicationDto getMedicationById(Long id);

    MedicationDto addMedication(SaveMedicationDto saveMedicationDto);

    MedicationDto updateMedication(Long id, SaveMedicationDto saveMedicationDto);

    String deleteMedication(Long id);

    boolean existsByPatientId(Long id);

    void deleteAllMedicationByPatientId(Long id);
}
