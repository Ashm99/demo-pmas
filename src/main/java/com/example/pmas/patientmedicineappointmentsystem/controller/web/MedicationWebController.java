package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.MedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveMedicationDto;
import com.example.pmas.patientmedicineappointmentsystem.service.MedicationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(value = "/web/medications")
@AllArgsConstructor
public class MedicationWebController {
    private MedicationService medicationService;

    /**
     * A mvc method for showing the medication list.
     * @param authentication Authentication interface object,
     * @param model Model interface object.
     * @return the medication list view for rendering.
     */
    // http://localhost:8082/web/medications/list
    @GetMapping(value = "/list")
    public String getMedicationsForUser(Authentication authentication, Model model){
        List<MedicationDto> medicationList = medicationService.getAllMedicationByUsername(authentication.getName());
        if (!medicationList.isEmpty()) {
            model.addAttribute("medicineList", medicationList);
            model.addAttribute("isEmpty", false);
        } else {
            model.addAttribute("isEmpty", true);
        }
        return "medications/medication-list";
    }

    // http://localhost:8082/web/medications/view/{id}

    /**
     * On selection of a particular medication, shows that specific medication's details.
     *
     * @param id Medication id,
     * @param model Model interface object.
     * @return view-medication html page.
     */
    @GetMapping(value = "/view/{id}")
    public String showMedicationById(@PathVariable(value = "id") Long id, Model model) {
        MedicationDto medicationDto = medicationService.getMedicationById(id);
        model.addAttribute("medicineDto", medicationDto);
        return "medications/view-medication";
    }


    /**
     * Renders the HTML form page for adding a new medication
     *
     * @param model Model interface object,
     * @return add-medication html page.
     */
    // http://localhost:8082/web/medications/add-medication
    @GetMapping(value = "/add-medication")
    public String addMedication(Authentication authentication, Model model) {
        model.addAttribute("medicineDto", new SaveMedicationDto());
        return "medications/add-medication";
    }

    /**
     * Gets a new medication object and saves into the repository.
     * Loads the success or error page based on the outcome.
     *
     * @param model Model interface object,
     * @param newMedication  Medication data to be saved,
     * @return success or error page.
     */
    @PostMapping(value = "/add")
    public String saveMedication(Authentication authentication, Model model, @Valid @ModelAttribute SaveMedicationDto newMedication) {
        newMedication.setPatientId(medicationService.getPatientIdByUsername(authentication.getName()));
//        System.out.println("Patient Id: " + newMedication.getPatientId());
        MedicationDto savedMedication;
        try {
            savedMedication = medicationService.saveMedication(newMedication);
//            System.out.println(newMedication.toString());
        } catch (Exception exception) {
            model.addAttribute("message", "Error while adding new medication." + exception.getMessage());
            System.out.println("Stack trace:");
            exception.printStackTrace();
            System.out.println("Resuming application...");
            return "error";
        }
        model.addAttribute("message", "Medication added with medication ID: " + savedMedication.getId() + ".");
        return "success";
    }

    // http://localhost:8082/web/medications/update-medication/{id}
    /**
     * Updates the medication object upon clicking update from the webpage.
     * This method actually renders the webpage for the update process.
     *
     * @param id Medication id,
     * @param model Model interface object,
     * @return a success or an error page based on the outcome.
     */
    @GetMapping(value = "/update-medication/{id}")
    public String updateMedication(@PathVariable(value = "id") Long id, Model model) {
        MedicationDto medicationDto = medicationService.getMedicationById(id);
        model.addAttribute("medicineDto", medicationDto);
        return "medications/update-medication";
    }

    /**
     * Updates the selected medication object with the values filled from the webpage.
     * Loads the success or error page based on the update process.
     *
     * @param model Model interface object,
     * @param updatedMedication Medication data to be updated.
     * @return success or error page.
     */
    @PostMapping(value = "/update/{id}")
    public String saveUpdatedMedication(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "prescriptionDate") LocalDate prescriptionDate,
            Authentication authentication,
            Model model,
            @Valid @ModelAttribute SaveMedicationDto updatedMedication
    ) {
        updatedMedication.setPatientId(medicationService.getPatientIdByUsername(authentication.getName()));

        MedicationDto savedMedication;
        try {
            savedMedication = medicationService.updateMedication(id, prescriptionDate, updatedMedication);
//            System.out.println(updatedMedication.toString());
//            System.out.println(prescriptionDate);
        } catch (Exception exception) {

            model.addAttribute("message", "Error while updating the medication-" + exception.getMessage());
            System.out.println("Stack trace:");
            exception.printStackTrace();
            System.out.println("Resuming application...");
            return "error";
        }
        model.addAttribute("message", "Medication updated with medication ID: " + id + ".");
        return "success";
    }

    // http://localhost:8082/web/medications/delete/{id}
    /**
     * Deletes the medication object upon clicking delete from the webpage.
     *
     * @param id Medication id,
     * @param model Model interface object.
     * @return a success or an error page based on the outcome.
     */
    @GetMapping(value = "/delete/{id}")
    public String deleteMedication(@PathVariable(value = "id") Long id, Model model) {
        medicationService.deleteMedication(id);
        try {
            medicationService.getMedicationById(id);
        } catch (NoSuchElementException e) {
            String message = "Successfully deleted medication from the database.";
            model.addAttribute("message", message);
            return "success";
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        model.addAttribute("message", "Error deleting the Medication.");
        return "error";
    }
}
