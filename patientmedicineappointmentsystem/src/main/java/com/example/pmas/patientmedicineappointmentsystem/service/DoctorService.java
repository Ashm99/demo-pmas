package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;

import java.util.List;

public interface DoctorService {

    List<DoctorDto> getAllDoctors();

    DoctorDto getDoctorById(Long id);

    DoctorDto addDoctor(CreateDoctorDto doctorDto);

    DoctorDto updateDoctor(DoctorDto doctorDto);

    void deleteDoctorById(Long id);
}
