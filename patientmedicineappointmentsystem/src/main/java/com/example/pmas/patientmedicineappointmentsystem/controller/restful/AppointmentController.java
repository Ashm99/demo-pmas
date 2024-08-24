package com.example.pmas.patientmedicineappointmentsystem.controller.restful;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateAppointmentDto;
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
    public ResponseEntity<?> createAppointment(@Valid @RequestBody CreateAppointmentDto createAppointmentDto){
        AppointmentDto appointmentDto = appointmentService.createAppointment(createAppointmentDto);
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
//
//    /**
//     * A RESTful method to delete all appointments for a patient with the patient's id.
//     * @param patientId The id of the patient.
//     * @return A message string.
//     */
//    @DeleteMapping(value="/deleteAll/{patientId}")
//    public ResponseEntity<?> deleteAppointmentByPatientId(@PathVariable(value = "patientId") Long patientId){
//        if(appointmentService.deleteAllAppointmentByPatientId(patientId)){
//            return new ResponseEntity<>("Deleted all appointments of patient with id: " + patientId + ".", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Could not delete all appointments of patient: " + patientId + ".", HttpStatus.OK);
//    }
}
