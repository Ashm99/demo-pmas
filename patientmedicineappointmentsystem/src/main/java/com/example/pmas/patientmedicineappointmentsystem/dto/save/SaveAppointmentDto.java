package com.example.pmas.patientmedicineappointmentsystem.dto.save;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveAppointmentDto {

    @NotNull(message = "Patient id cannot be null")
    private Long patientId;

    @NotNull(message = "Doctor id cannot be null")
    private Long doctorId;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$", message = "Enter value in <yyyy-MM-dd>T<HH:mm:ss>Z format")
    private String appointmentDateTime;
}
