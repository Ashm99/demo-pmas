package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;

import java.time.LocalDate;
import java.util.List;

public interface MedicationService {
    List<MedicationDto> getAllMedication();

    MedicationDto getMedicationById(Long id);

    MedicationDto saveMedication(SaveMedicationDto saveMedicationDto);

    MedicationDto updateMedication(Long id, LocalDate prescriptionDate, SaveMedicationDto saveMedicationDto);

    String deleteMedication(Long id);

    boolean existsByPatientId(Long id);

    void deleteAllMedicationByPatientId(Long id);
    List<MedicationDto> getAllMedicationByUsername(String username);

    String getPatientIdByUsername(String username);
}
