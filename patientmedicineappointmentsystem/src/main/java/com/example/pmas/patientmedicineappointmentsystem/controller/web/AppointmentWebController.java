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

    @PostMapping("/doctors")
    @ResponseBody
    public List<DoctorDto> getDoctorsBySpeciality(@RequestParam String speciality) {
        return appointmentService.getDoctorsBySpeciality(speciality);
    }

    // http://localhost:8082/web/appointments/add-appointment
    @GetMapping(value = "/add-appointment")
    public String bookAppointment(Model model, Authentication authentication) {
        SaveAppointmentDto saveAppointmentDto = new SaveAppointmentDto();
        saveAppointmentDto.setPatientId(appointmentService.getPatientIdByUsername(authentication.getName()));
        model.addAttribute("appointment", saveAppointmentDto);
        model.addAttribute("specialties", appointmentService.getAllDoctorSpecialties());
        return "appointments/add-appointment";
    }

    @PostMapping("/checkSlots")
    @ResponseBody
    public List<SlotDto> checkAvailableSlots(@RequestParam Long doctorId, @RequestParam String date) {
        List<SlotDto> slotDtos = appointmentService.getAvailableSlots(doctorId, date);
        return slotDtos;
    }

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

    // http://localhost:8082/web/appointments//delete/{id}
    /**
     * Deletes the appointment object upon clicking delete from the webpage
     *
     * @param id
     * @param model
     * @return a success or an error page based on the outcome.
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
