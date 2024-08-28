package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class AppointmentMapper {
    public static AppointmentDto mapToAppointmentDto(Appointment appointment){
        return new AppointmentDto(
                appointment.getId(),
                PatientMapper.mapToPatientDto(appointment.getPatient()),
                DoctorMapper.mapToDoctorDto(appointment.getDoctor()),
                appointment.getAppointmentDateTime().atZone(ZoneId.systemDefault()),
                appointment.getCreatedAt().atZone(ZoneId.systemDefault())
        );
    }

    public static Appointment mapToAppointmentFromSaveAppointmentDto(Patient patient, Doctor doctor, SaveAppointmentDto saveAppointmentDto){
        Instant appointmentDateTime;
        try{
            LocalDateTime localDateTime = LocalDateTime.parse(saveAppointmentDto.getAppointmentDateTime());
            appointmentDateTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            if(appointmentDateTime.isBefore(Instant.now())){
                throw new DateTimeException("");
            }
        } catch (Exception exception){
            throw new DateTimeException("Enter valid appointment date and time.");
        }
        return new Appointment(
                null,
                patient,
                doctor,
                appointmentDateTime,
                Instant.now()
        );
    }
}
