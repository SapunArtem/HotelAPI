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
@Schema(description = "Contact information")
public class ContactsDto {
    @Schema(description = "Phone number", example = "+375 17 309-80-00")
    private String phone;
    @Schema(description = "Email address", example = "doubletreeminsk.info@hilton.com")
    private String email;
}
