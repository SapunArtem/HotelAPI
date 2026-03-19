package org.example;

import org.example.dto.*;
import org.example.mapper.DtoConverter;
import org.example.model.*;
import org.example.repository.AmenityRepository;
import org.example.repository.HotelRepository;
import org.example.service.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private DtoConverter dtoConverter;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelSummaryDto summaryDto;
    private HotelDetailsDto detailsDto;
    private HotelCreateDto createDto;

    @BeforeEach
    void setUp() {
        hotel = Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .description("Test Description")
                .brand("Test Brand")
                .address(Address.builder().houseNumber(1).street("Test St").city("Test City").country("Test Country").postCode("12345").build())
                .contacts(Contacts.builder().phone("123456789").email("test@test.com").build())
                .arrivalTime(ArrivalTime.builder().checkIn("14:00").checkOut("12:00").build())
                .amenities(new ArrayList<>())
                .build();

        summaryDto = HotelSummaryDto.builder()
                .id(1L)
                .name("Test Hotel")
                .description("Test Description")
                .address("1 Test St, Test City, 12345, Test Country")
                .phone("123456789")
                .build();

        detailsDto = HotelDetailsDto.builder()
                .id(1L)
                .name("Test Hotel")
                .description("Test Description")
                .brand("Test Brand")
                .address(AddressDto.builder().houseNumber(1).street("Test St").city("Test City").country("Test Country").postCode("12345").build())
                .contacts(ContactsDto.builder().phone("123456789").email("test@test.com").build())
                .arrivalTime(ArrivalTimeDto.builder().checkIn("14:00").checkOut("12:00").build())
                .amenities(Collections.emptyList())
                .build();

        createDto = HotelCreateDto.builder()
                .name("Test Hotel")
                .description("Test Description")
                .brand("Test Brand")
                .address(AddressDto.builder().houseNumber(1).street("Test St").city("Test City").country("Test Country").postCode("12345").build())
                .contacts(ContactsDto.builder().phone("123456789").email("test@test.com").build())
                .arrivalTime(ArrivalTimeDto.builder().checkIn("14:00").checkOut("12:00").build())
                .build();
    }

    @Test
    void getAllHotels_ShouldReturnListOfSummaries() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(dtoConverter.convertToSummaryDto(hotel)).thenReturn(summaryDto);

        List<HotelSummaryDto> result = hotelService.getAllHotels();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(summaryDto);
        verify(hotelRepository).findAll();
    }

    @Test
    void getHotelById_WhenHotelExists_ShouldReturnDetails() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(dtoConverter.convertToDetailDto(hotel)).thenReturn(detailsDto);

        HotelDetailsDto result = hotelService.getHotelById(1L);

        assertThat(result).isEqualTo(detailsDto);
        verify(hotelRepository).findById(1L);
    }

    @Test
    void getHotelById_WhenHotelNotFound_ShouldThrowException() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Hotel not found with id: 99");
    }

    @Test
    void searchHotels_ShouldCallRepositoryWithParams() {
        when(hotelRepository.searchHotels(any(), any(), any(), any(), any())).thenReturn(List.of(hotel));
        when(dtoConverter.convertToSummaryDto(hotel)).thenReturn(summaryDto);

        List<HotelSummaryDto> result = hotelService.searchHotels("Test", null, "City", null, "WiFi");

        assertThat(result).hasSize(1);
        verify(hotelRepository).searchHotels("Test", null, "City", null, "WiFi");
    }

    @Test
    void createHotel_ShouldSaveAndReturnSummary() {
        when(dtoConverter.convertToEntity(createDto)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        when(dtoConverter.convertToSummaryDto(hotel)).thenReturn(summaryDto);

        HotelSummaryDto result = hotelService.createHotel(createDto);

        assertThat(result).isEqualTo(summaryDto);
        verify(hotelRepository).save(hotel);
    }

    @Test
    void addAmenitiesToHotel_WhenHotelExists_ShouldAddNewAndExistingAmenities() {
        Long hotelId = 1L;
        List<String> amenityNames = List.of("Free WiFi", "Pool");

        Amenity existingAmenity = Amenity.builder().id(10L).name("Free WiFi").build();
        Amenity newAmenity = Amenity.builder().name("Pool").build();

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByName("Free WiFi")).thenReturn(Optional.of(existingAmenity));
        when(amenityRepository.findByName("Pool")).thenReturn(Optional.empty());
        when(amenityRepository.save(any(Amenity.class))).thenReturn(newAmenity);

        hotelService.addAmenitiesToHotel(hotelId, amenityNames);

        assertThat(hotel.getAmenities()).contains(existingAmenity, newAmenity);
        verify(hotelRepository).save(hotel);
        verify(amenityRepository).save(any(Amenity.class));
    }

    @Test
    void addAmenitiesToHotel_WhenHotelNotFound_ShouldThrowException() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.addAmenitiesToHotel(99L, List.of("WiFi")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Hotel not found with id: 99");
    }

    @Test
    void getHistogram_WithValidParam_ShouldReturnMappedData() {
        String param = "city";
        List<Object[]> rawData = List.of(
                new Object[]{"Minsk", 2L},
                new Object[]{"Moscow", 3L}
        );
        when(hotelRepository.countByCity()).thenReturn(rawData);

        HistogramDto result = hotelService.getHistogram(param);

        assertThat(result.getHistogram())
                .containsEntry("Minsk", 2L)
                .containsEntry("Moscow", 3L);
    }

    @Test
    void getHistogram_WithInvalidParam_ShouldThrowException() {
        assertThatThrownBy(() -> hotelService.getHistogram("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid histogram parameter: invalid");
    }
}
