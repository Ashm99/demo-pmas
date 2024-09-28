package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepo extends JpaRepository<Medication, Long> {
    boolean existsByPatientId(Long patientId);
    int deleteAllByPatientId(Long patientId);
    List<Medication> findAllByPatientId(Long patientId);
}
