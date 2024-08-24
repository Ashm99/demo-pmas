package com.example.pmas.patientmedicineappointmentsystem.controller.restful;

import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.service.DoctorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/doctors")
@AllArgsConstructor
public class DoctorController {

    private DoctorService doctorService;

    /**
     * A RESTful method to get all the doctors in the database.
     * @return A response entity with the List of doctors as its body.
     */
    @GetMapping(value="/getAll")
    public ResponseEntity<?> getAllDoctors(){
        List<DoctorDto> doctorList = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctorList, HttpStatus.OK);
    }

    /**
     * A RESTful method to get a doctor with its ID.
     * @param id To search for a doctor.
     * @return A response entity with the doctor as its body.
     */
    @GetMapping(value="/get/{id}")
    public ResponseEntity<?> getDoctorsById(@PathVariable(name="id") Long id){
        DoctorDto doctorDto = doctorService.getDoctorById(id);
        return new ResponseEntity<>(doctorDto, HttpStatus.OK);
    }

    /**
     * A RESTful method to create a doctor.
     * @param doctorDto A Dto object of type CreateDoctorDto
     * @return A response entity with the doctor as its body.
     */
    @PostMapping(value="/add")
    public ResponseEntity<?> addDoctor(@Valid @RequestBody CreateDoctorDto doctorDto){
        DoctorDto savedDoctorDto  = doctorService.addDoctor(doctorDto);
        return new ResponseEntity<>(savedDoctorDto, HttpStatus.CREATED);
    }

    /**
     * A RESTful method to update a doctor.
     * @param doctorDto A Dto object.
     * @return A response entity with the doctor as its body.
     */
    @PutMapping(value="/update")
    public ResponseEntity<?> updateDoctor(@Valid @RequestBody DoctorDto doctorDto){
        DoctorDto savedDoctorDto  = doctorService.updateDoctor(doctorDto);
        return new ResponseEntity<>(savedDoctorDto, HttpStatus.OK);
    }

    /**
     * A RESTful method to delete a doctor with its ID.
     * @param id To search and delete for a doctor.wsw
     * @return A response entity with deletion message as its body.
     */
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteDoctorById(@PathVariable(name="id") Long id){
        doctorService.deleteDoctorById(id);
        return new ResponseEntity<>("Doctor with ID - " + id + " removed from the database.", HttpStatus.OK);
    }
}
