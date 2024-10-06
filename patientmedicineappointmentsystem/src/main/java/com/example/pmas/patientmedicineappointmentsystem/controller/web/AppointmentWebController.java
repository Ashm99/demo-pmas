package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.SlotDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/web/appointments")
@AllArgsConstructor
public class AppointmentWebController {
    private AppointmentService appointmentService;

    // http://localhost:8082/web/appointments/list
    /**
     * A method to get all the appointments of the patient.
     *
     * @param authentication Authentication Interface object to get current user data.
     * @param model Model interface object to add attributes to the HTML page.
     * @return An HTML view rendering the upcoming and completed appointment tables.
     */
    @GetMapping(value = "/list")
    public String getAppointments(Authentication authentication, Model model){
        List<List<AppointmentDto>> appointmentDtosList = appointmentService.getAllAppointmentByUsername(authentication.getName());
        List<AppointmentDto> upcomingAppointments = appointmentDtosList.get(0);
        List<AppointmentDto> completedAppointments = appointmentDtosList.get(1);
        if (!upcomingAppointments.isEmpty()) {
            model.addAttribute("upcomingAppointments", upcomingAppointments);
            model.addAttribute("noUpcomingAppointments", false);
        } else {
            model.addAttribute("noUpcomingAppointments", true);
        }
        if (!completedAppointments.isEmpty()) {
            model.addAttribute("completedAppointments", completedAppointments);
            model.addAttribute("noCompletedAppointments", false);
        } else {
            model.addAttribute("noCompletedAppointments", true);
        }
        return "appointments/appointments-list";
    }

    /**
     * A method to get all the doctors of a particular speciality.
     *
     * @param speciality A string to filter out doctors based on their speciality.
     * @return A JSON response with a doctors list of the required speciality.
     */
    @PostMapping("/doctors")
    @ResponseBody
    public List<DoctorDto> getDoctorsBySpeciality(@RequestParam String speciality) {
        return appointmentService.getDoctorsBySpeciality(speciality);
    }

    // http://localhost:8082/web/appointments/add-appointment
    /**
     * A method to get new appointment details through a form.
     *
     * @param model Model interface object to add attributes to the HTML page.
     * @param authentication Authentication Interface object to get current user data.
     * @return An HTML view asking for details for a new appointment.
     */
    @GetMapping(value = "/add-appointment")
    public String bookAppointment(Model model, Authentication authentication) {
        SaveAppointmentDto saveAppointmentDto = new SaveAppointmentDto();
        saveAppointmentDto.setPatientId(appointmentService.getPatientIdByUsername(authentication.getName()));
        model.addAttribute("appointment", saveAppointmentDto);
        model.addAttribute("specialties", appointmentService.getAllDoctorSpecialties());
        return "appointments/add-appointment";
    }

    /**
     * A method to get all the slots of the given doctor on the give date.
     *
     * @param doctorId Doctor id
     * @param date The date on which the appointments are to be fetched.
     * @return A JSON response having the slot data.
     */
    @PostMapping("/checkSlots")
    @ResponseBody
    public List<SlotDto> checkAvailableSlots(@RequestParam Long doctorId, @RequestParam String date) {
        List<SlotDto> slotDtos = appointmentService.getAvailableSlots(doctorId, date);
        return slotDtos;
    }

    /**
     * A method to book appointment once all the appointment details are submitted through the form
     *
     * @param saveAppointmentDto Appointment object with details
     * @param model Model interface object to add attributes to the HTML page.
     * @return A success or an error page based on the outcome.
     */
    @PostMapping("/book")
    public String bookAppointment(@Valid @ModelAttribute SaveAppointmentDto saveAppointmentDto, Model model) {
        System.out.println("saveAppointmentDto.toString(): " + saveAppointmentDto.toString());
        AppointmentDto savedAppointment = appointmentService.createAppointment(saveAppointmentDto);
        if(savedAppointment == null){
            model.addAttribute("message", "Error while adding new appointment.");
            return "error";
        }
        model.addAttribute("message", "Appointment added with ID: " + savedAppointment.getId() + ".\nCheck out the appointments main page.");
        return "success";
    }

    // http://localhost:8082/web/appointments/delete/{id}
    /**
     * Deletes the appointment object upon clicking delete from the webpage
     *
     * @param id Appointment id,
     * @param model Model interface object to add attributes to the HTML page.
     * @return A success or an error page based on the outcome.
     */
    @GetMapping(value = "/delete/{id}")
    public String deleteAppointment(@PathVariable(value = "id") Long id, Model model) {
        String message = "";
        try{
            message = appointmentService.deleteAppointment(id);
        } catch(Exception e){
            message = e.getMessage();
        }
        if(message.equals("Successfully cancelled the appointment.")) {
            model.addAttribute("message", message);
            return "success";
        } else {
            model.addAttribute("message", message);
            return "error";
        }
        //If any issue here comment out these lines and uncomment below lines.
        // Made this change while writing the AppointmentWebControllerTest case for this method

//        appointmentService.deleteAppointment(id);
//        try {
//            appointmentService.deleteAppointment(id);
//        } catch (NoSuchElementException e) {
//            String message = "Successfully deleted appointment from the database.";
//            model.addAttribute("message", message);
//            return "success";
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        model.addAttribute("message", "Error deleting the appointment.");
//        return "error";
    }
}
