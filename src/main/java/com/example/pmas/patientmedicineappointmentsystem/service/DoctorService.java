package com.example.pmas.patientmedicineappointmentsystem.service;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;

import java.util.List;

public interface DoctorService {

    List<DoctorDto> getAllDoctors();

    DoctorDto getDoctorById(Long id);

    DoctorDto addDoctor(SaveDoctorDto saveDoctorDto);

    List<DoctorDto> addAllDoctors(List<SaveDoctorDto> saveDoctorDtoList);

    DoctorDto updateDoctor(Long id, SaveDoctorDto saveDoctorDto);

    void deleteDoctorById(Long id);
}
