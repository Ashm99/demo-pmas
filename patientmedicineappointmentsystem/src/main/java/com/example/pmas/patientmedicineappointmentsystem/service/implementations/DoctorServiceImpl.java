package com.example.pmas.patientmedicineappointmentsystem.service.implementations;

import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveDoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.mapper.DoctorMapper;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.repo.DoctorRepo;
import com.example.pmas.patientmedicineappointmentsystem.service.AppointmentService;
import com.example.pmas.patientmedicineappointmentsystem.service.DoctorService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private DoctorRepo doctorRepo;
    private AppointmentService appointmentService;

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
        Doctor doctor = doctorRepo.findById(id).orElseThrow(
                ()->new NoSuchElementException("No doctor present with the given id: " + id + ".")
        );
        return DoctorMapper.mapToDoctorDto(doctor);
    }

    /**
     * A Service method to add multiple new doctors into the database.
     * @param saveDoctorDtoList of type SaveDoctorDto
     * @return The saved doctor list.
     */
    @Override
    public List<DoctorDto> addAllDoctors(List<SaveDoctorDto> saveDoctorDtoList) {
        List<Doctor> doctorList = new ArrayList<>();
        saveDoctorDtoList.forEach(
                saveDoctorDto -> {
                    doctorList.add(DoctorMapper.mapToDoctorFromSaveDoctorDto(null, saveDoctorDto));
                }
        );

        List<Doctor> savedDoctorList = doctorRepo.saveAll(doctorList);

        List<DoctorDto> doctorDtoList = new ArrayList<>();
        savedDoctorList.forEach(
                fetchedDoctor -> {
                    doctorDtoList.add(DoctorMapper.mapToDoctorDto(fetchedDoctor));
                }
        );
        return doctorDtoList;
    }

    /**
     * A Service method to add a new doctor into the database.
     * @param saveDoctorDto of type SaveDoctorDto
     * @return The saved doctor data.
     */
    @Override
    public DoctorDto addDoctor(SaveDoctorDto saveDoctorDto) {
        if(doctorRepo.findByMobile(saveDoctorDto.getMobile()).isPresent()){
            throw new RuntimeException("Doctor already exists with given mobile.");
        }
        Doctor savedDoctor = doctorRepo.save(DoctorMapper.mapToDoctorFromSaveDoctorDto(null, saveDoctorDto));
        return DoctorMapper.mapToDoctorDto(savedDoctor);
    }

    /**
     * A Service method to update an existing doctor in the database.
     * @param saveDoctorDto A Dto object that is to be updated into the database.
     * @return The updated doctor data.
     */
    @Override
    public DoctorDto updateDoctor(Long id,SaveDoctorDto saveDoctorDto) {
        if(doctorRepo.existsById(id)){
            Doctor updatedDoctor = doctorRepo.save(DoctorMapper.mapToDoctorFromSaveDoctorDto(id, saveDoctorDto));
            return DoctorMapper.mapToDoctorDto(updatedDoctor);
        }
        throw new NoSuchElementException("Update not possible as no one exists under the given doctor's id.");
    }

    /**
     * A service method to delete a doctor from the database.
     * @param id To search for and delete the doctor.
     */
    @Override
    @Transactional
    public void deleteDoctorById(Long id) {
        if(!doctorRepo.existsById(id)){
            throw new NoSuchElementException("Deletion not possible as no doctor exists under the given doctor's id.");
        }
        if(appointmentService.existsByDoctorId(id)){
            appointmentService.deleteAllAppointmentByDoctorId(id);
        }
        doctorRepo.deleteById(id);
        if(doctorRepo.existsById(id)){
            throw new RuntimeException("Exception while deletion of the doctor with ID: " + id + ". A doctor still exists in the Id.");
        }
    }
}
