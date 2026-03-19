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
@Schema(description = "Address information")
public class AddressDto {
    @Schema(description = "House number", example = "9")
    private Integer houseNumber;
    @Schema(description = "street name", example = "Pobediteley Avenue")
    private String street;
    @Schema(description = "City", example = "Minsk")
    private String city;
    @Schema(description = "Country", example = "Belarus")
    private String country;
    @Schema(description = "Postal code", example = "220004")
    private String postCode;
}
