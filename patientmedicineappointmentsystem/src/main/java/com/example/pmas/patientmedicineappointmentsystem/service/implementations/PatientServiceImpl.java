package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SavePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.PatientMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.AppointmentService;
import com.example.pmas.patientmedicineappointmentsystem.service.MedicationService;
import com.example.pmas.patientmedicineappointmentsystem.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {
    private PatientRepo patientRepo;
    private AppointmentService appointmentService;
    private MedicationService medicationService;
    private PasswordEncoder passwordEncoder;

    /**
     * A service method to get all the patients in the database.
     *
     * @return A list of patients
     */
    @Override
    public List<PatientDto> getAllPatients() {
        List<PatientDto> patientDtoList = new ArrayList<>();
        patientRepo.findAll().forEach(
                (patient) -> patientDtoList.add(PatientMapper.mapToPatientDto(patient))
        );
        if (patientDtoList.isEmpty()) {
            throw new NoSuchElementException("No Patients present in the database.");
        }
        return patientDtoList;
    }

    /**
     * A Service method to find a specific patient with its ID.
     *
     * @param id
     * @return The patient data.
     * @throws NoSuchElementException If no element exists with given id.
     */
    @Override
    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepo.findById(id).orElseThrow(
                () -> new NoSuchElementException("No patient present in our database under given id: " + id + ".")
        );
        return PatientMapper.mapToPatientDto(patient);
    }

    /**
     * A Service method to add a new patient into the database.
     *
     * @param savePatientDto of type SavePatientDto
     * @return The saved patient data.
     */
    @Override
    public PatientDto addPatient(SavePatientDto savePatientDto) {
        if(patientRepo.findByMobile(savePatientDto.getMobile()).isPresent()){
            throw new RuntimeException("Patient already exists with given mobile.");
        }
        Patient newPatient = PatientMapper.mapToPatientFromSavePatientDto(null, savePatientDto);
        newPatient.setPassword(passwordEncoder.encode(newPatient.getPassword()));
        Patient savedPatient = patientRepo.save(newPatient);
        return PatientMapper.mapToPatientDto(savedPatient);
    }

    /**
     * A Service method to update an existing patient in the database.
     *
     * @param savePatientDto A Dto object.
     * @return The updated patient data.
     */
    @Override
    public PatientDto updatePatient(Long id, SavePatientDto savePatientDto) {
        if (patientRepo.existsById(id)) {
            Patient updatedPatient = patientRepo.save(PatientMapper.mapToPatientFromSavePatientDto(id, savePatientDto));
            return PatientMapper.mapToPatientDto(updatedPatient);
        }
        throw new NoSuchElementException("Update not possible as no patient exists under the given patient id: " + id + ".");
    }

    /**
     * A service method to delete a patient from the database.
     *
     * @param id To search for and delete the patient.
     */
    @Override
    @Transactional
    public void deletePatientById(Long id) {
        if (!patientRepo.existsById(id)) {
            throw new NoSuchElementException("Deletion not possible as no patient exists under the given patient id" + id + ".");
        }
        if (appointmentService.existsByPatientId(id)) {
            appointmentService.deleteAllAppointmentByPatientId(id);
        }
        if (medicationService.existsByPatientId(id)) {
            medicationService.deleteAllMedicationByPatientId(id);
        }
        patientRepo.deleteById(id);
        if (patientRepo.existsById(id)) {
            throw new RuntimeException("Exception while deletion of the patient with ID: " + id + ". A patient still exists in the Id.");
        }
    }

    /**
     * A Service method to find a specific patient with its mobile no.
     *
     * @param username Mobile number of the patient
     * @return The patient data.
     * @throws NoSuchElementException If no element exists with given id.
     */
    @Override
    public PatientDto getPatientByUsername(String username) {
        Patient fetchedPatient = patientRepo.findByMobile(username).orElseThrow(
                () -> new NoSuchElementException("No patient present in our database under given mobile no.: " + username + "."));
        return PatientMapper.mapToPatientDto(fetchedPatient);
    }

    /**
     * A Service method to find a specific patient's first name with its mobile no..
     *
     * @param username Mobile number of the patient
     * @return The patient's first name.
     * @throws NoSuchElementException If no element exists with given id.
     */
    @Override
    public String getPatientFirstnameByUsername(String username) {
        Patient fetchedPatient = patientRepo.findByMobile(username).orElseThrow(
                () -> new NoSuchElementException("No patient present in our database under given mobile no.: " + username + "."));
        return fetchedPatient.getFirstName();
    }
}
