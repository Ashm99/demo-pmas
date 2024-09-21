package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.service.MedicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/web/medication")
@AllArgsConstructor
public class MedicationWebController {
    private MedicationService medicationService;

    /**
     * A RESTful method to get all the medications in the database.
     * @return A response entity with the List of medications as its body.
     */
    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllMedications(){
        List<MedicationDto> medicationDtos = medicationService.getAllMedication();
        return new ResponseEntity<>(medicationDtos, HttpStatus.OK);
    }

    /**
     * A RESTful method to get a medication with its ID.
     * @param id To search for a medication.
     * @return A response entity with the medication as its body.
     */
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getMedicationById(@PathVariable(value = "id") Long id){
        MedicationDto medicationDto = medicationService.getMedicationById(id);
        return new ResponseEntity<>(medicationDto, HttpStatus.OK);
    }

    /**
     * A RESTful method to create a medication.
     * @param saveMedicationDto A Dto object of type SaveMedicationDto
     * @return A response entity with the medication as its body.
     */
    @PostMapping(value = "/add")
    public ResponseEntity<?> addMedication(@Valid @RequestBody SaveMedicationDto saveMedicationDto){
        MedicationDto medicationDto = medicationService.saveMedication(saveMedicationDto);
        return new ResponseEntity<>(medicationDto, HttpStatus.CREATED);
    }

    /**
     * A RESTful method to update a medication.
     * @param saveMedicationDto A Dto object.
     * @return A response entity with the medication as its body.
     */
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateMedication(@PathVariable(name = "id")Long id, @Valid @RequestBody SaveMedicationDto saveMedicationDto){
        MedicationDto updatedMedicationDto = medicationService.updateMedication(id, saveMedicationDto);
        return new ResponseEntity<>(updatedMedicationDto, HttpStatus.CREATED);
    }

    /**
     * A RESTful method to delete a medication with its ID.
     * @param id To search and delete for a medication.
     * @return A response entity with deletion message as its body.
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteMedication(@PathVariable(value = "id") Long id){
        String message = medicationService.deleteMedication(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
