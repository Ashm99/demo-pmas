package com.example.pmas.patientmedicineappointmentsystem.controller.restful;

import com.example.pmas.patientmedicineappointmentsystem.dto.CreatePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/patients")
@AllArgsConstructor
public class PatientController {

    private PatientService patientService;

    /**
     * A RESTful method to get all the patients in the database.
     * @return A response entity with the List of patients as its body.
     */
    @GetMapping(value="/getAll")
    public ResponseEntity<?> getAllPatients(){
        List<PatientDto> patientList = patientService.getAllPatients();
        return new ResponseEntity(patientList, HttpStatus.OK);
    }

    /**
     * A RESTful method to get a patient with its ID.
     * @param id To search for a patient.
     * @return A response entity with the patient as its body.
     */
    @GetMapping(value="/get/{id}")
    public ResponseEntity<?> getPatientsById(@PathVariable(name="id") Long id){
        PatientDto patientDto = patientService.getPatientById(id);
        return new ResponseEntity(patientDto, HttpStatus.OK);
    }

    /**
     * A RESTful method to create a patient.
     * @param patientDto A Dto object of type CreatePatientDto
     * @return A response entity with the patient as its body.
     */
    @PostMapping(value="/add")
    public ResponseEntity<?> addPatient(@Valid @RequestBody CreatePatientDto patientDto){
        PatientDto savedPatientDto  = patientService.addPatient(patientDto);
        return new ResponseEntity(savedPatientDto, HttpStatus.CREATED);
    }

    /**
     * A RESTful method to update a patient.
     * @param patientDto A Dto object.
     * @return A response entity with the patient as its body.
     */
    @PutMapping(value="/update")
    public ResponseEntity<?> updatePatient(@Valid @RequestBody PatientDto patientDto){
        PatientDto savedPatientDto  = patientService.updatePatient(patientDto);
        return new ResponseEntity(savedPatientDto, HttpStatus.OK);
    }

    /**
     * A RESTful method to delete a patient with its ID.
     * @param id To search and delete for a patient.
     * @return A response entity with deletion message as its body.
     */
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deletePatientsById(@PathVariable(name="id") Long id){
        patientService.deletePatientById(id);
        return new ResponseEntity("Patient with ID - " + id + " removed from the database.", HttpStatus.OK);
    }
}
