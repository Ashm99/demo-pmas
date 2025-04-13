package com.example.pmas.patientmedicineappointmentsystem.config;

import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import com.example.pmas.patientmedicineappointmentsystem.repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientDetailsService implements UserDetailsService {
    @Autowired
    private PatientRepo patientRepo;
    public PatientDetailsService(){
    }
    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Patient> optionalPatient = patientRepo.findByMobile(username);
//        if(optionalPatient.isPresent()){
//            return new PatientDetails(optionalPatient.get()); // will need a PatientDetails class for this method
//        }
        if(optionalPatient.isPresent()){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(optionalPatient.get().getMobile())
                    .password(optionalPatient.get().getPassword())
                    .roles("PATIENT")
                    .build();
        }
        throw new UsernameNotFoundException("Invalid username.");
    }
}
