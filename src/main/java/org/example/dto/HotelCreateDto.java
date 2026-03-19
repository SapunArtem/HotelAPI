package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Hotel creation request")
public class HotelCreateDto {
    @NotBlank(message = "Hotel name is required")
    @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
    private String name;
    @Schema(description = "Hotel description", example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
    private String description;
    @NotBlank(message = "Brand is required")
    @Schema(description = "Hotel brand",example = "Hilton")
    private String brand;
    @NotNull(message = "Address is required")
    @Schema(description = "Address information")
    private AddressDto address;
    @NotNull(message = "Contacts are required")
    @Schema(description = "Contact information")
    private ContactsDto contacts;
    @NotNull(message = "Arrival time is required")
    @Schema(description = "Arrival time information")
    private ArrivalTimeDto arrivalTime;
}
