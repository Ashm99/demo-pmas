package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
}
