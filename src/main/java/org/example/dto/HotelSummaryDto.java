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
@Schema(description = "Hotel summary information")
public class HotelSummaryDto {
    @Schema(description = "Hotel id", example = "1")
    private Long id;
    @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
    private String name;
    @Schema(description = "Hotel description", example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
    private String description;
    @Schema(description = "Hotel full address", example = "9 Pobediteley Avenue, Minsk, 220004, Belarus")
    private String address;
    @Schema(description = "Phone number", example = "+375 17 309-80-00")
    private String phone;
}
