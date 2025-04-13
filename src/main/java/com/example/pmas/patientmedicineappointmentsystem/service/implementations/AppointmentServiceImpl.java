package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.SlotDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.AppointmentMapper;
import com.example.pmas.patientmedicineappointmentsystem.mapper.DoctorMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;
import com.example.pmas.patientmedicineappointmentsystem.repo.AppointmentRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.DoctorRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
     *
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
                () -> new NoSuchElementException("No appointment exists under given id: " + id + ".")
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
        Appointment appointment = AppointmentMapper.mapToAppointmentFromSaveAppointmentDto(saveAppointmentDto);
        appointment.setPatient(
                patientRepo.findById(saveAppointmentDto.getPatientId()).orElseThrow(
                        () -> new NoSuchElementException("No patient exists under given patient id: " + saveAppointmentDto.getPatientId() + ".")
                )
        );
        appointment.setDoctor(
                doctorRepo.findById(saveAppointmentDto.getDoctorId()).orElseThrow(
                        () -> new NoSuchElementException("No doctor exists under given doctor id: " + saveAppointmentDto.getDoctorId() + ".")
                )
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
     *
     * @param patientId The id of the patient whose all appointments are to be checked.
     * @return True if present and vice versa.
     */
    @Override
    public boolean existsByPatientId(Long patientId) {
        return appointmentRepo.existsByPatientId(patientId);
    }

    /**
     * A Service method to delete all the appointments of a particular patient.
     *
     * @param patientId The id of the doctor whose all appointments are to be deleted.
     */
    @Override
    public void deleteAllAppointmentByPatientId(Long patientId) {
        int rows = appointmentRepo.deleteAllByPatientId(patientId);
        System.out.printf("%d rows deleted from the appointment table.%n", rows);
        if (this.existsByPatientId(patientId)) {
            throw new RuntimeException("Error while deleting appointments of patient with id: " + patientId + ".");
        }
    }

    /**
     * @param name username
     * @return
     */
    @Override
    public List<List<AppointmentDto>> getAllAppointmentByUsername(String name) {
        Long patientId = patientRepo.findByMobile(name).get().getId();
        List<List<AppointmentDto>> appointmentDtosList = new ArrayList<>();
        List<AppointmentDto> upcomingAppointmentDtos = new ArrayList<>();
        List<AppointmentDto> completedAppointmentDtos = new ArrayList<>();
        appointmentRepo.getAllByPatientId(patientId).forEach(
                (appointment) -> {
                    if(appointment.getAppointmentDateTime().isBefore(LocalDateTime.now())){
                        completedAppointmentDtos.add(AppointmentMapper.mapToAppointmentDto(appointment));
                    } else {
                        upcomingAppointmentDtos.add(AppointmentMapper.mapToAppointmentDto(appointment));
                    }
                }
        );
        appointmentDtosList.add(upcomingAppointmentDtos);
        appointmentDtosList.add(completedAppointmentDtos);
        return appointmentDtosList;
    }

    /**
     * A Service method to find if there are appointments of a particular doctor.
     *
     * @param doctorId The id of the doctor whose all appointments are to be checked.
     * @return True if present and vice versa.
     */
    @Override
    public boolean existsByDoctorId(Long doctorId) {
        return appointmentRepo.existsByDoctorId(doctorId);
    }

    /**
     * A Service method to delete all the appointments of a particular doctor.
     *
     * @param doctorId The id of the doctor whose all appointments are to be deleted.
     */
    @Override
    public void deleteAllAppointmentByDoctorId(Long doctorId) {
        int rows = appointmentRepo.deleteAllByDoctorId(doctorId);
        System.out.printf("%d rows deleted from the appointment table.%n", rows);
        if (this.existsByDoctorId(doctorId)) {
            throw new RuntimeException("Error while deleting appointments of Doctor with id: " + doctorId + ".");
        }
    }

    @Override
    public List<DoctorDto> getDoctorsBySpeciality(String speciality){
        List<DoctorDto> doctorDtos = new ArrayList<>();
        doctorRepo.getAllBySpeciality(speciality).forEach(
               doctor -> doctorDtos.add(DoctorMapper.mapToDoctorDto(doctor))
        );
        return doctorDtos;
    }

    @Override
    public List<String> getAllDoctorSpecialties(){
        List<String> specialities = doctorRepo.getDistinctSpeciality();
        if (specialities.isEmpty()){
            throw new RuntimeException("No specialities found as no doctors are present in the database.");
        }
        return specialities;
    }

    @Override
    public List<SlotDto> getAvailableSlots(Long doctorId, String date) {
        System.out.println("Getting available slots...");
        List<SlotDto> slotDtos = new ArrayList<>();
        LocalDate selectedDate = LocalDate.parse(date);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 00);
        int slotDuration = 30;

        LocalTime time = startTime;

        // adding slots
        for(; time.isBefore(endTime); time = time.plusMinutes(slotDuration)){
            System.out.println("for loop for adding slots started");
            System.out.println(time.format(DateTimeFormatter.ofPattern("hh:mm a")));
            if(time.equals(LocalTime.of(12,30)) || time.equals(LocalTime.of(13,0))){
                System.out.println("lunch time arrived!");
                //do nothing -> lunch break 🤷‍♂️
            } else {
                System.out.println("Preparing to add slot...");
                boolean isBooked = appointmentRepo.existsByDoctorIdAndAppointmentDateTimeBetween(
                        doctorId,
                        selectedDate.atTime(time),
                        selectedDate.atTime(time.plusMinutes(slotDuration - 1))
                );
                String displayString = time.format(DateTimeFormatter.ofPattern("hh:mm a")) + " to " + time.plusMinutes(slotDuration).format(DateTimeFormatter.ofPattern("hh:mm a"));
                slotDtos.add(new SlotDto(time, isBooked, displayString));
                System.out.println("Added slot: " + displayString);
            }
        }
        System.out.println("Added all the slots...");
        System.out.println("Returning the slots.");

//        Dummy dates for testing
//        SlotDto slotDto1 = new SlotDto();
//        slotDto1.setTime(LocalTime.of(9,0));
//        slotDto1.setBooked(false);
//        slotDto1.setDisplayString("9.00 a.m. to 9.30 a.m.");
//        SlotDto slotDto2 = new SlotDto();
//        slotDto2.setTime(LocalTime.of(9,30));
//        slotDto2.setBooked(false);
//        slotDto2.setDisplayString("9.30 a.m. to 10.00 a.m.");
//        slotDtos.add(slotDto1);
//        slotDtos.add(slotDto2);

        return slotDtos;
    }

    /**
     * A service method to get the current patient Id with the username
     * @param username Patient username
     * @return Patient Id as a String
     */
    @Override
    public Long getPatientIdByUsername(String username) {
        return patientRepo.findByMobile(username).get().getId();
    }
}
