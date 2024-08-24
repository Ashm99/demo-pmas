package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.creation.CreatePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.PatientMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import com.example.pmas.patientmedicineappointmentsystem.repo.AppointmentRepo;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {
    private PatientRepo patientRepo;
    private AppointmentRepo appointmentRepo;

    /**
     * A service method to get all the patients in the database.
     * @return A list of patients
     */
    @Override
    public List<PatientDto> getAllPatients() {
        List<PatientDto> patientDtoList = new ArrayList<>();
        patientRepo.findAll().forEach(
                (patient) -> {
                    patientDtoList.add(PatientMapper.mapToPatientDto(patient));
                }
        );
        if(patientDtoList.isEmpty()){
            throw new NoSuchElementException("No Patients present in the database.");
        }
        return patientDtoList;
    }

    /**
     * A Service method to find a specific patient with its ID.
     * @param id
     * @throws NoSuchElementException If no element exists with given id.
     * @return The patient data.
     */
    @Override
    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(
                ()-> new NoSuchElementException("No patient present in our database under given id: " + id + ".")
        );
        return PatientMapper.mapToPatientDto(patient);
    }

    /**
     * A Service method to add a new patient into the database.
     * @param patientDto of type CreatePatientDto
     * @return The saved patient data.
     */
    @Override
    public PatientDto addPatient(CreatePatientDto patientDto) {
        Patient savedPatient = patientRepo.save(PatientMapper.mapToPatientFromCreatePatientDto(patientDto));
        return PatientMapper.mapToPatientDto(savedPatient);
    }

    /**
     * A Service method to update an existing patient in the database.
     * @param patientDto
     * @return The updated patient data.
     */
    @Override
    public PatientDto updatePatient(PatientDto patientDto) {
        if(patientRepo.existsById(patientDto.getId())){
            Patient updatedPatient = patientRepo.save(PatientMapper.mapToPatient(patientDto));
            return PatientMapper.mapToPatientDto(updatedPatient);
        }
        throw new NoSuchElementException("Update not possible as no patient exists under the given patient id: " + patientDto.getId() + ".");
    }

    /**
     * A service method to delete a patient from the database.
     * @param id To search for and delete the patient.
     */
    @Override
    public void deletePatientById(Long id) {
        if(!patientRepo.existsById(id)){
            throw new NoSuchElementException("Deletion not possible as no patient exists under the given patient id" + id + ".");
        }
        patientRepo.deleteById(id);
        if(patientRepo.existsById(id)){
            throw new RuntimeException("Exception while deletion of the patient with ID: " + id + ". A patient still exists in the Id.");
        }
    }
}
