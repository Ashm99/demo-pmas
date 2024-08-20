package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.CreateAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.AppointmentMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;
import com.example.pmas.patientmedicineappointmentsystem.repo.AppointmentRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.DoctorRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private AppointmentRepo appointmentRepo;
    private PatientRepo patientRepo;
    private DoctorRepo doctorRepo;

    /**
     * A service method to get all the appointments in the database.
     * @return A list of appointments.
     */
    @Override
    public List<AppointmentDto> getAllAppointments() {
        List<AppointmentDto> appointmentDtos = new ArrayList<>();
        appointmentRepo.findAll().forEach(
                appointment -> appointmentDtos.add(AppointmentMapper.mapToAppointmentDto(appointment))
        );
        return appointmentDtos  ;
    }

    /**
     * A service method to get an appointment through its id from the database.
     * @param id Appointment id.
     * @return An AppointmentDto object.
     */
    @Override
    public AppointmentDto getAppointmentById(Long id) {
        AppointmentDto appointmentDto = AppointmentMapper.mapToAppointmentDto(appointmentRepo.findById(id).orElseThrow());
        return appointmentDto;
    }

    /**
     * A service method to create an appointment.
     * @param createAppointmentDto Parameter with appointment time, patient and doctor ids.
     * @return An AppointmentDto object.
     */
    @Override
    public AppointmentDto createAppointment(CreateAppointmentDto createAppointmentDto) {
        Appointment appointment = AppointmentMapper.mapToAppointmentFromCreateAppointmentDto(createAppointmentDto);
        appointment.setPatient(patientRepo.findById(createAppointmentDto.getPatientId()).orElseThrow(
                () -> new NoSuchElementException("No patient exists under given patient id.")
        ));
        appointment.setDoctor(doctorRepo.findById(createAppointmentDto.getDoctorId()).orElseThrow(
                () -> new NoSuchElementException("No doctor exists under given doctor id.")
        ));
        Appointment savedAppointment = appointmentRepo.save(appointment);
        return AppointmentMapper.mapToAppointmentDto(savedAppointment);
    }

    /**
     * A Service method to delete an appointment.
     * @param id The id of the appointment to be deleted.
     * @return A message based on the outcome.
     */
    @Override
    public String deleteAppointment(Long id) {
        if(!appointmentRepo.existsById(id)){
            throw new NoSuchElementException("No appointment exists under the given id");
        }
        appointmentRepo.deleteById(id);
        if(appointmentRepo.existsById(id)){
            throw new NoSuchElementException("Appointment still exists with the given id.");
        }
        return "Successfully cancelled the appointment.";
    }
}
