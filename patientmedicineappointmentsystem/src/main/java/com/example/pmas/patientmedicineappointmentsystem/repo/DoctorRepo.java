package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    List<Doctor> getAllBySpeciality(String specialty);

    @Query("SELECT DISTINCT d.speciality FROM Doctor d ORDER BY d.speciality ASC")
    List<String> getDistinctSpeciality();
}
