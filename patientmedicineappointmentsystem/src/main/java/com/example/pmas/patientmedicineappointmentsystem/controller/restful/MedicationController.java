package com.example.pmas.patientmedicineappointmentsystem.controller.restful;

import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreateMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.service.MedicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/medication")
@AllArgsConstructor
public class MedicationController {
    private MedicationService medicationService;

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllMedications(){
        List<MedicationDto> medicationDtos = medicationService.getAllMedication();
        return new ResponseEntity<>(medicationDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getMedicationById(@PathVariable(value = "id") Long id){
        MedicationDto medicationDto = medicationService.getMedicationById(id);
        return new ResponseEntity<>(medicationDto, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<?> createMedication(@Valid @RequestBody CreateMedicationDto createMedicationDto){
        MedicationDto medicationDto = medicationService.createMedication(createMedicationDto);
        return new ResponseEntity<>(medicationDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateMedication(@Valid @RequestBody MedicationDto medicationDto){
        MedicationDto updatedMedicationDto = medicationService.updateMedication(medicationDto);
        return new ResponseEntity<>(updatedMedicationDto, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteMedication(@PathVariable(value = "id") Long id){
        String message = medicationService.deleteMedication(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
