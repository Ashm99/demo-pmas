package com.example.pmas.patientmedicineappointmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class SlotDto {
    private LocalTime time;
    private boolean isBooked;
    private String displayString;
}
