package com.example.pmas.patientmedicineappointmentsystem.repo;

import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Will load the Entity and JPA related classes -> The repository layer
public class DoctorRepositoryTests {
    @Autowired
    private DoctorRepo doctorRepo;
    private Doctor doctor;

    @BeforeEach
    public void setUp(){
        doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpeciality("General");
    }

    @DisplayName(value = "JUnit test for find all method when no data given")
    @Test
    public void givenNoDoctorList_whenFindAll_thenReturnEmptyList(){

        List<Doctor> doctors = doctorRepo.findAll();

        assertThat(doctors).isEmpty();
    }
    @DisplayName(value = "JUnit test for find all method")
    @Test
    public void givenDoctorList_whenFindAll_thenReturnDoctorList(){
        Doctor doctor1 = new Doctor();
        doctor1.setFirstName("Rebecca");
        doctor1.setLastName("Doe");
        doctor1.setSpeciality("Gynecologist");

        doctorRepo.save(doctor);
        doctorRepo.save(doctor1);
        List<Doctor> doctors = doctorRepo.findAll();

        assertThat(doctors).isNotEmpty();
        assertThat(doctors.size()).isEqualTo(2);

        assertThat(doctors.get(0).getId()).isGreaterThan(0);
        assertThat(doctors.get(0).getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(doctors.get(0).getLastName()).isEqualTo(doctor.getLastName());
        assertThat(doctors.get(0).getSpeciality()).isEqualTo(doctor.getSpeciality());
        assertThat(doctors.get(1).getId()).isGreaterThan(1);
        assertThat(doctors.get(1).getFirstName()).isEqualTo(doctor1.getFirstName());
        assertThat(doctors.get(1).getLastName()).isEqualTo(doctor1.getLastName());
        assertThat(doctors.get(1).getSpeciality()).isEqualTo(doctor1.getSpeciality());
    }

    @DisplayName(value = "JUnit test for find doctor by id method")
    @Test
    public void givenDoctor_whenFindById_thenReturnDoctor(){
        Long id = doctorRepo.save(doctor).getId();
        Optional<Doctor> fetchedDoctor = doctorRepo.findById(id);

        assertThat(fetchedDoctor).isNotEmpty();

        assertThat(fetchedDoctor.get().getId()).isGreaterThan(0);
        assertThat(fetchedDoctor.get().getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(fetchedDoctor.get().getLastName()).isEqualTo(doctor.getLastName());
        assertThat(fetchedDoctor.get().getSpeciality()).isEqualTo(doctor.getSpeciality());
    }

    @DisplayName(value = "JUnit test for find doctor by id method without a doctor")
    @Test
    public void givenNoDoctor_whenFindById_thenReturnEmptyOptional(){
        Optional<Doctor> fetchedDoctor = doctorRepo.findById(1L);

        assertThat(fetchedDoctor).isEmpty();
    }

    @DisplayName(value = "JUnit test method for save doctor method.")
    @Test
    public void givenDoctorObject_whenSave_thenReturnSavedDoctorObject(){
        Doctor savedDoctor  = doctorRepo.save(doctor);

        assertThat(savedDoctor).isNotNull();
        assertThat(savedDoctor.getId()).isGreaterThan(0);
        assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
        assertThat(savedDoctor.getSpeciality()).isEqualTo(doctor.getSpeciality());
    }

    @DisplayName(value = "JUnit test method for save doctor method during updation.")
    @Test
    public void givenDoctorObject_whenUpdate_thenReturnUpdatedDoctorObject(){
        Long id  = doctorRepo.save(doctor).getId();
        Doctor savedDoctor = doctorRepo.findById(id).get();

        savedDoctor.setFirstName("Monica");
        savedDoctor.setLastName("Bell");
        savedDoctor.setSpeciality("Pediatrics");

        Doctor updatedDoctor  = doctorRepo.save(savedDoctor);

        assertThat(updatedDoctor).isNotNull();
        assertThat(updatedDoctor.getId()).isGreaterThan(0);

        assertThat(updatedDoctor.getId()).isEqualTo(savedDoctor.getId());
        assertThat(updatedDoctor.getFirstName()).isEqualTo(savedDoctor.getFirstName());
        assertThat(updatedDoctor.getLastName()).isEqualTo(savedDoctor.getLastName());
        assertThat(updatedDoctor.getSpeciality()).isEqualTo(savedDoctor.getSpeciality());
    }

    @DisplayName(value = "JUnit test method for exists by id method.")
    @Test
    public void givenDoctorObject_whenExists_thenReturnTrue(){
        Long id  = doctorRepo.save(doctor).getId();
        Boolean doctorSaved = doctorRepo.existsById(id);

        assertThat(doctorSaved).isTrue();
    }

    @DisplayName(value = "JUnit test method for exists by id method without any object.")
    @Test
    public void givenNoObject_whenExists_thenReturnFalse(){
        Boolean doctorSaved = doctorRepo.existsById(1L);

        assertThat(doctorSaved).isFalse();
    }



    @DisplayName(value = "JUnit test method for delete by id method.")
    @Test
    public void givenDoctorObject_whenDelete_thenRemoveObject(){
        Long id  = doctorRepo.save(doctor).getId();

        doctorRepo.deleteById(id);

        Optional<Doctor> optionalDoctor = doctorRepo.findById(id);
        assertThat(optionalDoctor).isEmpty();
    }


}
