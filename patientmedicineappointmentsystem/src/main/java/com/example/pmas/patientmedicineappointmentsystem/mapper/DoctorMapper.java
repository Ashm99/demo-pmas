package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateDoctorDto;
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

    public static Doctor mapToDoctorFromCreateDoctorDto(CreateDoctorDto doctorDto){
        return new Doctor(
                null,
                doctorDto.getFirstName().trim(),
                doctorDto.getLastName().trim(),
                doctorDto.getSpeciality().trim()
        );
    }
}
