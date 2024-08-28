package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;

public class DoctorMapper {
    public static DoctorDto mapToDoctorDto(Doctor doctor){
        return new DoctorDto(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpeciality()
        );
    }

    public static Doctor mapToDoctor(DoctorDto doctorDto){
        return new Doctor(
                doctorDto.getId(),
                doctorDto.getFirstName().trim(),
                doctorDto.getLastName().trim(),
                doctorDto.getSpeciality().trim()
        );
    }

    public static Doctor mapToDoctorFromSaveDoctorDto(Long id, SaveDoctorDto saveDoctorDto){
        return new Doctor(
                id,
                saveDoctorDto.getFirstName().trim(),
                saveDoctorDto.getLastName().trim(),
                saveDoctorDto.getSpeciality().trim()
        );
    }
}
