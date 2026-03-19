package org.example;

import org.example.controller.HotelController;
import org.example.dto.*;
import org.example.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllHotels_ShouldReturnList() throws Exception {
        List<HotelSummaryDto> hotels = List.of(
                HotelSummaryDto.builder().id(1L).name("Hotel 1").build(),
                HotelSummaryDto.builder().id(2L).name("Hotel 2").build()
        );
        when(hotelService.getAllHotels()).thenReturn(hotels);

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].name").value("Hotel 2"));
    }

    @Test
    void getHotelById_WhenExists_ShouldReturnHotel() throws Exception {
        HotelDetailsDto hotel = HotelDetailsDto.builder().id(1L).name("Test Hotel").build();
        when(hotelService.getHotelById(1L)).thenReturn(hotel);

        mockMvc.perform(get("/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Hotel"));
    }

    @Test
    void getHotelById_WhenNotFound_ShouldReturn404() throws Exception {
        when(hotelService.getHotelById(99L)).thenThrow(new RuntimeException("Hotel not found with id: 99"));

        mockMvc.perform(get("/hotels/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchHotels_WithParams_ShouldReturnList() throws Exception {
        List<HotelSummaryDto> results = List.of(
                HotelSummaryDto.builder().id(1L).name("Minsk Hotel").build()
        );
        when(hotelService.searchHotels(any(), any(), eq("Minsk"), any(), any())).thenReturn(results);

        mockMvc.perform(get("/search").param("city", "Minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Minsk Hotel"));
    }

    @Test
    void createHotel_WithValidData_ShouldReturnCreated() throws Exception {
        HotelCreateDto createDto = HotelCreateDto.builder()
                .name("New Hotel")
                .brand("Brand")
                .address(AddressDto.builder().houseNumber(1).street("St").city("City").country("Country").postCode("123").build())
                .contacts(ContactsDto.builder().phone("123").email("a@b.com").build())
                .arrivalTime(ArrivalTimeDto.builder().checkIn("14:00").checkOut("12:00").build())
                .build();

        HotelSummaryDto summary = HotelSummaryDto.builder().id(1L).name("New Hotel").build();
        when(hotelService.createHotel(any(HotelCreateDto.class))).thenReturn(summary);

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Hotel"));
    }

    @Test
    void createHotel_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        HotelCreateDto invalidDto = HotelCreateDto.builder().build(); // missing required fields

        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void addAmenitiesToHotel_ShouldReturnOk() throws Exception {
        List<String> amenities = List.of("WiFi", "Pool");
        doNothing().when(hotelService).addAmenitiesToHotel(1L, amenities);

        mockMvc.perform(post("/hotels/1/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isOk());
    }

    @Test
    void getHistogram_WithValidParam_ShouldReturnMap() throws Exception {
        Map<String, Long> histogramData = Map.of("Minsk", 2L, "Moscow", 3L);
        HistogramDto histogramDto = HistogramDto.builder()
                        .histogram(histogramData)
                                .build();

        when(hotelService.getHistogram("city")).thenReturn(histogramDto);

        mockMvc.perform(get("/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.histogram.Minsk").value(2))
                .andExpect(jsonPath("$.histogram.Moscow").value(3));
    }

    @Test
    void getHistogram_WithInvalidParam_ShouldReturnBadRequest() throws Exception {
        when(hotelService.getHistogram("invalid")).thenThrow(new IllegalArgumentException("Invalid histogram parameter: invalid"));

        mockMvc.perform(get("/histogram/invalid"))
                .andExpect(status().isBadRequest());
    }
}
