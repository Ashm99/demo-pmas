package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.save.SavePatientDto;
import com.example.pmas.patientmedicineappointmentsystem.service.PatientService;
import jakarta.validation.Valid;
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
     *
     * @return the login view.
     */
    // http://localhost:8082/web/patients/login
    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    /**
     * A mvc method for the sign-up/register page.
     *
     * @param model  Model interface object.
     * @return the view for registering a new patient.
     */
    // http://localhost:8082/web/patients/register
    @GetMapping(value = "/register")
    public String register(Model model) {
        model.addAttribute("newPatient", new SavePatientDto());
        return "register";
    }

    /**
     * A mvc method to save the new patient
     *
     * @param savePatientDto  Patient data to be saved,
     * @param model  Model interface object.
     * @return Success or Error page based on the outcome.
     */
    @PostMapping(value = "/savePatient")
    public String savePatient(@Valid @ModelAttribute SavePatientDto savePatientDto, Model model) {
//        System.out.println(savePatientDto.toString());
        try{
            patientService.addPatient(savePatientDto);
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            return "redirect:/web/patients/register?mobileAlreadyExists";
        }
        return "redirect:/web/patients/register?success";
    }

}
