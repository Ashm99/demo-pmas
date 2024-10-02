package com.example.pmas.patientmedicineappointmentsystem.config;

import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PatientDetails implements UserDetails {
    private Patient patient;
    public PatientDetails(Patient patient){
        this.patient = patient;
    }
    /**
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_PATIENT"));
    }

    /**
     * @return
     */
    @Override
    public String getPassword() {
        return patient.getPassword();
    }

    /**
     * @return
     */
    @Override
    public String getUsername() {
        return patient.getMobile();
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
