package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Arrival time information")
public class ArrivalTimeDto {
    @Schema(description = "Check-in time", example = "14:00")
    private String checkIn;
    @Schema(description = "Check-out time", example = "12:00")
    private String checkOut;
}
