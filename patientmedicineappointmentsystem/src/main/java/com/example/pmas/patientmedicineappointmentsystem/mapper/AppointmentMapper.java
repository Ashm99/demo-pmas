package com.example.pmas.patientmedicineappointmentsystem.mapper;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentMapper {
    public static AppointmentDto mapToAppointmentDto(Appointment appointment){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        return new AppointmentDto(
                appointment.getId(),
                PatientMapper.mapToPatientDto(appointment.getPatient()),
                DoctorMapper.mapToDoctorDto(appointment.getDoctor()),
                appointment.getAppointmentDateTime().format(formatter),
                appointment.getCreatedAt().format(formatter)
        );
    }

    public static Appointment mapToAppointmentFromSaveAppointmentDto(SaveAppointmentDto saveAppointmentDto){
        LocalDateTime appointmentDateTime;
        try{
            appointmentDateTime = LocalDateTime.parse(saveAppointmentDto.getAppointmentDateTime());
            if(appointmentDateTime.isBefore(LocalDateTime.now())){
                System.err.println("Select a time in the future for the appointment.");
                throw new DateTimeException("");
            }
        } catch (Exception exception){
            throw new DateTimeException("Enter valid appointment date and time.");
        }
        return new Appointment(
                null,
                null,
                null,
                appointmentDateTime,
                LocalDateTime.now()
        );
    }
}
