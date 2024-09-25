package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SavePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/web/patients")
@AllArgsConstructor
public class PatientAuthController {
    private PatientService patientService;

    /**
     * A mvc method for the login page
     */
    // http://localhost:8082/web/patients/login
    @GetMapping(value = "/login")
    public String login(Model model) {
        return "login";
    }

    /**
     * A mvc method for the sign-up/register page
     */
    // http://localhost:8082/web/patients/register
    @GetMapping(value = "/register")
    public String register(Model model) {
        model.addAttribute("newPatient", new SavePatientDto());
        return "register";
    }

    /**
     * A mvc method to save the new patient
     */
    @PostMapping(value = "/savePatient")
    public String savePatient(@ModelAttribute SavePatientDto savePatientDto) {
        System.out.println(savePatientDto.toString());
        patientService.addPatient(savePatientDto);
        return "redirect:/web/patients/register?success";
    }

}
