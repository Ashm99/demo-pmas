package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/web/appointments")
@AllArgsConstructor
public class AppointmentWebController {

    private AppointmentService appointmentService;

    /**
     * A RESTful method to get all the appointments in the database.
     * @return A response entity with the List of appointments as its body.
     */
    @GetMapping(value="/getAll")
    public ResponseEntity<?> getAllAppointments(){
        List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointments();
        return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
    }

    /**
     * A RESTful method to get an appointment by id from the database.
     * @param id The id of the appointment to be searched.
     * @return A response entity with the appointment as its body.
     */
    @GetMapping(value="/get/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable(value = "id") Long id){
        AppointmentDto appointmentDto = appointmentService.getAppointmentById(id);
        return new ResponseEntity<>(appointmentDto, HttpStatus.OK);
    }

    /**
     * A RESTful method to create an appointment.
     * @return A response entity with the appointment as its body.
     */
    @PostMapping(value="/add")
    public ResponseEntity<?> saveAppointment(@Valid @RequestBody SaveAppointmentDto saveAppointmentDto){
        AppointmentDto appointmentDto = appointmentService.createAppointment(saveAppointmentDto);
        return new ResponseEntity<>(appointmentDto, HttpStatus.CREATED);
    }

    /**
     * A RESTful method to delete an appointment with its id.
     * @param id The id of the appointment to be deleted.
     * @return A message string.
     */
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable(value = "id") Long id){
        String message = appointmentService.deleteAppointment(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
