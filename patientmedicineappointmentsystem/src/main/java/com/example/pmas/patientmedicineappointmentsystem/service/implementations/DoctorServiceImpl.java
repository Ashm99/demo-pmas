package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.CreateDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.DoctorMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.repo.DoctorRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private DoctorRepo doctorRepo;

    /**
     * A service method to get all the doctors in the database.
     * @return A list of doctors
     */
    @Override
    public List<DoctorDto> getAllDoctors() {
        List<DoctorDto> doctorDtoList = new ArrayList<>();
        doctorRepo.findAll().forEach(
                (doctor) -> doctorDtoList.add(DoctorMapper.mapToDoctorDto(doctor))
        );
        if(doctorDtoList.isEmpty()){
            throw new NoSuchElementException("No doctors present in the database.");
        }
        return doctorDtoList;
    }

    /**
     * A Service method to find a specific doctor with his ID.
     * @param id The id of the doctor in the database.
     * @throws NoSuchElementException If no element exists with given id.
     * @return The doctor data.
     **/
    @Override
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepo.findById(id).orElseThrow();
        return DoctorMapper.mapToDoctorDto(doctor);
    }

    /**
     * A Service method to add a new doctor into the database.
     * @param doctorDto of type CreateDoctorDto
     * @return The saved doctor data.
     */
    @Override
    public DoctorDto addDoctor(CreateDoctorDto doctorDto) {
        Doctor savedDoctor = doctorRepo.save(DoctorMapper.mapToDoctorFromCreateDoctorDto(doctorDto));
        return DoctorMapper.mapToDoctorDto(savedDoctor);
    }

    /**
     * A Service method to update an existing doctor in the database.
     * @param doctorDto A Dto object that is to be updated into the database.
     * @return The updated doctor data.
     */
    @Override
    public DoctorDto updateDoctor(DoctorDto doctorDto) {
        if(doctorRepo.existsById(doctorDto.getId())){
            Doctor updatedDoctor = doctorRepo.save(DoctorMapper.mapToDoctor(doctorDto));
            return DoctorMapper.mapToDoctorDto(updatedDoctor);
        }
        throw new NoSuchElementException("Update not possible as no one exists under the given doctor's id.");
    }

    /**
     * A service method to delete a doctor from the database.
     * @param id To search for and delete the doctor.
     */
    @Override
    public void deleteDoctorById(Long id) {
        if(doctorRepo.existsById(id)){
            doctorRepo.deleteById(id);
            if(doctorRepo.existsById(id)){
                throw new RuntimeException("Exception while deletion of the doctor with ID: " + id + ". A doctor still exists in the Id.");
            }
            return;
        }
        throw new NoSuchElementException("Deletion not possible as no one exists under the given doctor's id.");
    }
}
