package org.example.service;

import org.example.dto.HistogramDto;
import org.example.dto.HotelCreateDto;
import org.example.dto.HotelDetailsDto;
import org.example.dto.HotelSummaryDto;

import java.util.List;

public interface HotelService {
    List<HotelSummaryDto> getAllHotels();
    HotelDetailsDto getHotelById(Long id);
    List<HotelSummaryDto> searchHotels(String name, String brand, String city, String country, String amenity);
    HotelSummaryDto createHotel(HotelCreateDto hotelCreateDto);
    void addAmenitiesToHotel(Long hotelId, List<String> amenities);
    HistogramDto getHistogram(String param);
}
