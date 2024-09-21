package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/web/patients")
@AllArgsConstructor
public class PatientWebController {
    private PatientService patientService;

    /**
     * A mvc method for showing the home page
     */
    // http://localhost:8082/web/patients/home
    @GetMapping(value = "/home")
    public String goHome(Model model, Authentication authentication) {
        String patientName = patientService.getPatientFirstnameByUsername(authentication.getName());
        model.addAttribute("patientName", patientName);
        return "home";
    }

    /**
     * A mvc method for the logout confirmation page
     */
    // http://localhost:8082/web/patients/confirm-logout
    @GetMapping(value = "/confirm-logout")
    public String confirmLogout() {
        return "confirm-logout";
    }
}
