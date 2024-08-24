package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;

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

    public static Appointment mapToAppointmentFromCreateAppointmentDto(Patient patient, Doctor doctor, CreateAppointmentDto createAppointmentDto){
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(createAppointmentDto.getAppointmentDateTime());
        return appointment;
    }
}
