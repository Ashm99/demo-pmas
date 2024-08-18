package com.example.pmas.patientmedicineappointmentsystem.controller.restful;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.CreateAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/appointments")
@AllArgsConstructor
public class AppointmentController{

    private AppointmentService appointmentService;

    /**
     * A RESTful method to get all the appointments in the database.
     * @return A response entity with the List of appointments as its body.
     */
    @GetMapping(value="/getAll")
    public ResponseEntity<?> getAllAppointments(){
        List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointments();
        System.out.println(appointmentDtos.isEmpty());
        return new ResponseEntity<>(appointmentDtos, HttpStatus.OK);
    }

    /**
     * A RESTful method to get an appointment by id from the database.
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
    @PostMapping(value="/create")
    public ResponseEntity<?> createAppointment(@Valid @RequestBody CreateAppointmentDto createAppointmentDto){
        AppointmentDto appointmentDto = appointmentService.createAppointment(createAppointmentDto);
        return new ResponseEntity<>(appointmentDto, HttpStatus.CREATED);
    }
}
