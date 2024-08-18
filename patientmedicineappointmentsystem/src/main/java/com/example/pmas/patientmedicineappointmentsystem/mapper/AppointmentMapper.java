package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.CreateAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;

import java.time.Instant;

public class AppointmentMapper {
    public static AppointmentDto mapToAppointmentDto(Appointment appointment){
        return new AppointmentDto(
                appointment.getId(),
                PatientMapper.mapToPatientDto(appointment.getPatient()),
                DoctorMapper.mapToDoctorDto(appointment.getDoctor()),
                appointment.getAppointmentDateTime(),
                appointment.getCreatedAt()
        );
    }

    public static Appointment mapToAppointmentFromCreateAppointmentDto(CreateAppointmentDto createAppointmentDto){
        return new Appointment(
                null,
                null,
                null,
                createAppointmentDto.getAppointmentDateTime(),
                Instant.now()
        );
    }
}
