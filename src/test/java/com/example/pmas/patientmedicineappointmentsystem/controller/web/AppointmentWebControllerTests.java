package com.example.pmas.patientmedicineappointmentsystem.controller.web;

import com.example.pmas.patientmedicineappointmentsystem.dto.AppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.DoctorDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.PatientDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.SlotDto;
import com.example.pmas.patientmedicineappointmentsystem.dto.save.SaveAppointmentDto;
import com.example.pmas.patientmedicineappointmentsystem.model.Appointment;
import com.example.pmas.patientmedicineappointmentsystem.model.Doctor;
import com.example.pmas.patientmedicineappointmentsystem.model.Patient;
import com.example.pmas.patientmedicineappointmentsystem.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentWebController.class)
public class AppointmentWebControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @Mock
    private Authentication authentication;
//    @Autowired
//    private Model model;
//    As Autowiring or mocking Model is not required.
//    The required functionality in testing can be got from org.springframework.test.web.servlet.result.MockMvcResultMatchers.model()

    // Global variables
    private Appointment appointment;
    private AppointmentDto appointmentDto;
    private SaveAppointmentDto saveAppointmentDto;
    private Patient patient;
    private PatientDto patientDto;
    private Doctor doctor;
    private DoctorDto doctorDto;
    private LocalDateTime dayAfterTomorrowNoon;
    private LocalDateTime now;

    @BeforeEach
    public void setup() {
        patient = new Patient(
                1L,
                "John",
                "Doe",
                "jdoe@gmail.com",
                "9876543210",
                "John@2024",
                "USA",
                24,
                "Male",
                "A1+",
                "Johanna",
                "9876543211",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true
        );

        doctor = new Doctor(
                1L,
                "John",
                "Smith",
                "Male",
                "9988776655",
                "jsmith@gmail.com",
                "General",
                5,
                "MBBS",
                "English",
                "USA"
        );
        patientDto = new PatientDto(
                1L,
                "John",
                "Doe",
                "jdoe@gmail.com",
                "9876543210",
                "USA",
                24,
                "Male",
                "A1+",
                "Johanna",
                "9876543211",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true

        );

        doctorDto = new DoctorDto(
                1L,
                "John",
                "Smith",
                "Male",
                "9988776655",
                "jsmith@gmail.com",
                "General",
                5,
                "MBBS",
                "English",
                "USA"
        );

        dayAfterTomorrowNoon = LocalDateTime
                .of(LocalDate.now().plusDays(2), LocalTime.NOON);
        now = LocalDateTime.now();

        appointment = new Appointment(
                1L,
                patient,
                doctor,
                dayAfterTomorrowNoon,
                now
        );

        appointmentDto = new AppointmentDto(
                1L,
                patientDto,
                doctorDto,
                dayAfterTomorrowNoon.toString(),
                now.toString()
        );

        saveAppointmentDto = new SaveAppointmentDto(
                patient.getId(),
                doctor.getId(),
                dayAfterTomorrowNoon.toString()
        );
    }

    @WithMockUser
    @DisplayName("JUnit test method for getting appointment list")
    @Test
    public void givenAuthentication_whenGetAppointments_thenReturnAppointmentsListPage() throws Exception {
        // Arrange
        List<AppointmentDto> upcomingAppointments = new ArrayList<>();
        List<AppointmentDto> completedAppointments = new ArrayList<>();
        PatientDto patientDto1 = new PatientDto(
                1L,
                "John",
                "Doe",
                "jdoe@gmail.com",
                "9876543210",
                "USA",
                24,
                "Male",
                "A1+",
                "Johanna",
                "9876543211",
                "Sister",
                "None",
                "None",
                "None",
                "None",
                false,
                true
        );

        DoctorDto doctorDto1 = new DoctorDto(
                1L,
                "John",
                "Smith",
                "Male",
                "9988776655",
                "jsmith@gmail.com",
                "General",
                5,
                "MBBS",
                "English",
                "USA"
        );
        AppointmentDto appointmentDto1 = new AppointmentDto(
                2L,
                patientDto1,
                doctorDto1,
                dayAfterTomorrowNoon.toString(),
                now.toString()
        );

        upcomingAppointments.add(appointmentDto);
        completedAppointments.add(appointmentDto1);

        when(authentication.getName()).thenReturn("9999999999");
        when(appointmentService.getAllAppointmentByUsername(anyString())).thenReturn(List.of(upcomingAppointments, completedAppointments));

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/appointments/list")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("appointments/appointments-list"))
                .andExpect(model().attributeExists("upcomingAppointments"))
                .andExpect(model().attributeExists("completedAppointments"));
    }

    @WithMockUser
    @DisplayName("JUnit test method for getting doctors by specialty")
    @Test
    public void givenSpecialty_whenGetDoctorsBySpeciality_thenReturnDoctorList() throws Exception {
        // Arrange
        List<DoctorDto> doctors = new ArrayList<>();
        doctors.add(doctorDto);
        when(appointmentService.getDoctorsBySpeciality(anyString())).thenReturn(doctors);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/appointments/doctors")
                        .with(csrf())
                        .param("speciality", "General")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(doctors)));
    }

    @WithMockUser // Simulating an authenticated user
    @DisplayName("JUnit test method for booking an appointment view")
    @Test
    public void givenAuthentication_whenBookAppointment_thenReturnAppointmentPage() throws Exception {
        // Arrange
        String username = "9876543210";
        Long patientId = 1L;
        SaveAppointmentDto emptyAppointment = new SaveAppointmentDto();
        emptyAppointment.setPatientId(patientId);

        when(authentication.getName()).thenReturn(username);
        when(appointmentService.getPatientIdByUsername(anyString())).thenReturn(patientId);
        when(appointmentService.getAllDoctorSpecialties()).thenReturn(List.of("Cardiology", "Neurology"));

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/appointments/add-appointment")
                );

        // Assert
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("appointments/add-appointment"))
                .andExpect(model().attributeExists("appointment"))
                .andExpect(model().attribute("appointment",emptyAppointment))
                .andExpect(model().attribute("specialties", List.of("Cardiology", "Neurology")))
                .andExpect(model().attributeExists("specialties"));

    }

    @WithMockUser
    @DisplayName("JUnit test method for checking available slots")
    @Test
    public void givenDoctorIdAndDate_whenCheckAvailableSlots_thenReturnSlotList() throws Exception  {
        // Arrange
        List<SlotDto> slots = new ArrayList<>();
        LocalTime time = LocalTime.NOON;
        boolean isBooked = false;
        String displayString = time.format(DateTimeFormatter.ofPattern("hh:mm a"))
                + " to "
                + time.plusMinutes(30).format(DateTimeFormatter.ofPattern("hh:mm a"));
        SlotDto slotDto = new SlotDto(
                time,
                isBooked,
                displayString
        );
        slots.add(slotDto);
        when(appointmentService.getAvailableSlots(anyLong(), anyString())).thenReturn(slots);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/appointments/checkSlots")
                        .with(csrf())
                        .param("doctorId", "1")
                        .param("date", "2024-10-10")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(slotDto))));
    }
    @WithMockUser
    @DisplayName("JUnit test method for booking an appointment")
    @Test
    public void givenValidAppointmentData_whenBookAppointment_thenReturnSuccessPage() throws Exception {
        // Arrange
        when(appointmentService.createAppointment(any(SaveAppointmentDto.class))).thenReturn(appointmentDto);

        System.out.println("saveAppointmentDto.getAppointmentDateTime(): " + saveAppointmentDto.getAppointmentDateTime().toString());
        // Was needed for checking the logic at line 340
        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/appointments/book")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("patientId", String.valueOf(saveAppointmentDto.getPatientId()))
                        .param("doctorId", String.valueOf(saveAppointmentDto.getDoctorId()))
                        .param("appointmentDateTime", String.valueOf(LocalDate.now().plusDays(2)) + "T12:00:00")
//                        .param("appointmentDateTime", saveAppointmentDto.getAppointmentDateTime() + ":00")
                        // Reason for adding ":00" is while calculating "saveAppointmentDto.getAppointmentDateTime()" the program drops the seconds
                        // This is evident from the sysout print of line 330.
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Appointment added with ID: " + appointmentDto.getId() + ".\nCheck out the appointments main page."));
    }

    @WithMockUser
    @DisplayName("JUnit test method for booking an appointment with error")
    @Test
    public void givenInvalidAppointmentData_whenBookAppointment_thenReturnErrorPage() throws Exception {
        // Arrange
        when(appointmentService.createAppointment(any(SaveAppointmentDto.class))).thenReturn(null);

        // Act
        ResultActions resultActions = mockMvc.perform(
                post("/web/appointments/book")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("patientId", String.valueOf(saveAppointmentDto.getPatientId()))
                        .param("doctorId", String.valueOf(saveAppointmentDto.getDoctorId()))
                        .param("appointmentDateTime", String.valueOf(LocalDate.now().plusDays(2)) + "T12:00:00")
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Error while adding new appointment."));
    }

    @WithMockUser
    @DisplayName("JUnit test method for deleting an appointment")
    @Test
    public void givenAppointmentId_whenDeleteAppointment_thenReturnSuccessPage() throws Exception {
        // Arrange
        Long appointmentId = 1L;
        when(appointmentService.deleteAppointment(anyLong())).thenReturn("Successfully cancelled the appointment.");

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/appointments/delete/{id}", appointmentId)
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Successfully cancelled the appointment."));
    }

    @WithMockUser
    @DisplayName("JUnit test method for deleting an appointment with error")
    @Test
    public void givenInvalidAppointmentId_whenDeleteAppointment_thenReturnErrorPage() throws Exception {
        // Arrange
        Long appointmentId = 1L;
        doThrow(new NoSuchElementException("Appointment not found")).when(appointmentService).deleteAppointment(anyLong());

        // Act
        ResultActions resultActions = mockMvc.perform(
                get("/web/appointments/delete/{id}", appointmentId)
        );

        // Assert
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message","Appointment not found"));
    }

}

