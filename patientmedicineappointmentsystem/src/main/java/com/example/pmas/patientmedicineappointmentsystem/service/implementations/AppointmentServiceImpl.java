package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveAppointmentDto;
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
        return appointmentDtos;
    }

    /**
     * A service method to get an appointment through its id from the database.
     *
     * @param id Appointment id.
     * @return An AppointmentDto object.
     */
    @Override
    public AppointmentDto getAppointmentById(Long id) {
        return AppointmentMapper.mapToAppointmentDto(appointmentRepo.findById(id).orElseThrow(
                ()->new NoSuchElementException("No appointment exists under given id: " + id + ".")
        ));
    }

    /**
     * A service method to create an appointment.
     *
     * @param saveAppointmentDto Parameter with appointment time, patient and doctor ids.
     * @return An AppointmentDto object.
     */
    @Override
    public AppointmentDto createAppointment(SaveAppointmentDto saveAppointmentDto) {
        Appointment appointment = AppointmentMapper.mapToAppointmentFromSaveAppointmentDto(
                patientRepo.findById(saveAppointmentDto.getPatientId()).orElseThrow(
                        () -> new NoSuchElementException("No patient exists under given patient id: " + saveAppointmentDto.getPatientId() + ".")
                ),
                doctorRepo.findById(saveAppointmentDto.getDoctorId()).orElseThrow(
                        () -> new NoSuchElementException("No doctor exists under given doctor id: " + saveAppointmentDto.getDoctorId() + ".")
                ),
                saveAppointmentDto
        );
        Appointment savedAppointment = appointmentRepo.save(appointment);
        return AppointmentMapper.mapToAppointmentDto(savedAppointment);
    }

    /**
     * A Service method to delete an appointment.
     *
     * @param id The id of the appointment to be deleted.
     * @return A message based on the outcome.
     */
    @Override
    public String deleteAppointment(Long id) {
        if (!appointmentRepo.existsById(id)) {
            throw new NoSuchElementException("No appointment exists under the given id: " + id + ".");
        }
        appointmentRepo.deleteById(id);
        if (appointmentRepo.existsById(id)) {
            throw new RuntimeException("Appointment still exists with the given id: " + id + ".");
        }
        return "Successfully cancelled the appointment.";
    }

    /**
     * A Service method to find if there are appointments of a particular patient.
     * @param patientId The id of the patient whose all appointments are to be checked.
     * @return True if present and vice versa.
     */
    @Override
    public boolean existsByPatientId(Long patientId) {
        return appointmentRepo.existsByPatientId(patientId);
    }

    /**
     * A Service method to delete all the appointments of a particular patient.
     * @param patientId The id of the doctor whose all appointments are to be deleted.
     */
    @Override
    public void deleteAllAppointmentByPatientId(Long patientId) {
        int rows = appointmentRepo.deleteAllByPatientId(patientId);
        System.out.printf("%d rows deleted from the appointment table.%n", rows);
        if(this.existsByPatientId(patientId)){
            throw new RuntimeException("Error while deleting appointments of patient with id: " + patientId + ".");
        }
    }

    /**
     * A Service method to find if there are appointments of a particular doctor.
     * @param doctorId The id of the doctor whose all appointments are to be checked.
     * @return True if present and vice versa.
     */
    @Override
    public boolean existsByDoctorId(Long doctorId) {
        return appointmentRepo.existsByDoctorId(doctorId);
    }

    /**
     * A Service method to delete all the appointments of a particular doctor.
     * @param doctorId The id of the doctor whose all appointments are to be deleted.
     */
    @Override
    public void deleteAllAppointmentByDoctorId(Long doctorId) {
        int rows = appointmentRepo.deleteAllByDoctorId(doctorId);
        System.out.printf("%d rows deleted from the appointment table.%n", rows);
        if(this.existsByDoctorId(doctorId)){
            throw new RuntimeException("Error while deleting appointments of Doctor with id: " + doctorId + ".");
        }
    }
}
