package com.example.pmas.patientmedicineappointmentsystem.controller.restful;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveDoctorDto;
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
    // http://localhost:8082/doctors/getAll
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
    // http://localhost:8082/doctors/get/{id}
    @GetMapping(value="/get/{id}")
    public ResponseEntity<?> getDoctorsById(@PathVariable(name="id") Long id){
        DoctorDto doctorDto = doctorService.getDoctorById(id);
        return new ResponseEntity<>(doctorDto, HttpStatus.OK);
    }

    /**
     * A RESTful method to create a doctor.
     * @param saveDoctorDto A Dto object of type SaveDoctorDto
     * @return A response entity with the doctor as its body.
     */
    // http://localhost:8082/doctors/add
    @PostMapping(value="/add")
    public ResponseEntity<?> addDoctor(@Valid @RequestBody SaveDoctorDto saveDoctorDto){
        DoctorDto savedDoctorDto  = doctorService.addDoctor(saveDoctorDto);
        return new ResponseEntity<>(savedDoctorDto, HttpStatus.CREATED);
    }

    /**
     * A RESTful method to create a doctor.
     * @param saveDoctorDtoList A Dto object of type SaveDoctorDto
     * @return A response entity with the doctor as its body.
     */
    // http://localhost:8082/doctors/addAll
    @PostMapping(value="/addAll")
    public ResponseEntity<?> addAllDoctors(@Valid @RequestBody List<SaveDoctorDto> saveDoctorDtoList){
        List<DoctorDto> savedDoctorDtoList  = doctorService.addAllDoctors(saveDoctorDtoList);
        return new ResponseEntity<>(savedDoctorDtoList, HttpStatus.CREATED);
    }

    /**
     * A RESTful method to update a doctor.
     * @param saveDoctorDto A Dto object.
     * @return A response entity with the doctor as its body.
     */
    // http://localhost:8082/doctors/update/{id}
    @PutMapping(value="/update/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable(name = "id") Long id, @Valid @RequestBody SaveDoctorDto saveDoctorDto){
        DoctorDto savedDoctorDto  = doctorService.updateDoctor(id, saveDoctorDto);
        return new ResponseEntity<>(savedDoctorDto, HttpStatus.OK);
    }

    /**
     * A RESTful method to delete a doctor with its ID.
     * @param id To search and delete for a doctor.wsw
     * @return A response entity with deletion message as its body.
     */
    // http://localhost:8082/doctors/delete/{id}
    @DeleteMapping(value="/delete/{id}")
    public ResponseEntity<?> deleteDoctorById(@PathVariable(name="id") Long id){
        doctorService.deleteDoctorById(id);
        return new ResponseEntity<>("Doctor with ID - " + id + " removed from the database.", HttpStatus.OK);
    }
}
