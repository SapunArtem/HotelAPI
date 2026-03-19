package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.HistogramDto;
import org.example.dto.HotelCreateDto;
import org.example.dto.HotelDetailsDto;
import org.example.dto.HotelSummaryDto;
import org.example.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@Tag(name = "Hotels",description = "Hotel management API")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels",description = "Returns list of all hotels with summary information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500",description = "Internal server error")
    })

    public ResponseEntity<List<HotelSummaryDto>> getAllHotels(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel by ID", description = "Returns detailed information about a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved hotel"),
            @ApiResponse(responseCode = "404", description = "Hotel not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HotelDetailsDto> getHotelById(
            @Parameter(description = "Hotel ID", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels", description = "Search hotels by various parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<HotelSummaryDto>> searchHotels(
            @Parameter(description = "Hotel name") @RequestParam(required = false) String name,
            @Parameter(description = "Hotel brand") @RequestParam(required = false) String brand,
            @Parameter(description = "City") @RequestParam(required = false) String city,
            @Parameter(description = "Country") @RequestParam(required = false) String country,
            @Parameter(description = "Amenity") @RequestParam(required = false) String amenity) {
        return ResponseEntity.ok(hotelService.searchHotels(name, brand, city, country, amenity));
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create new hotel", description = "Creates a new hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HotelSummaryDto> createHotel(
            @Valid @RequestBody HotelCreateDto hotelCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.createHotel(hotelCreateDto));
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities to hotel", description = "Adds a list of amenities to a specific hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Amenities successfully added"),
            @ApiResponse(responseCode = "404", description = "Hotel not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> addAmenitiesToHotel(
            @Parameter(description = "Hotel ID") @PathVariable Long id,
            @RequestBody List<String> amenities) {
        hotelService.addAmenitiesToHotel(id, amenities);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram", description = "Returns histogram data grouped by specified parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved histogram"),
            @ApiResponse(responseCode = "400", description = "Invalid parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HistogramDto> getHistogram(
            @Parameter(description = "Parameter for histogram (brand, city, country, amenities)",
                    example = "city")
            @PathVariable String param) {
        return ResponseEntity.ok(hotelService.getHistogram(param));
    }
}
