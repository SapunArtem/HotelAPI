package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detailed hotel information")
public class HotelDetailsDto {
    @Schema(description = "Hotel ID", example = "1")
    private Long id;
    @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
    private String name;
    @Schema(description = "Hotel description",example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
    private String description;
    @Schema(description = "Hotel brand", example = "Hilton")
    private String brand;
    @Schema(description = "Address information")
    private AddressDto address;
    @Schema(description = "Contact information")
    private ContactsDto contacts;
    @Schema(description = "Arrival time information")
    private ArrivalTimeDto arrivalTime;
    @Schema(description = "List of amenities")
    private List<String> amenities;
}
