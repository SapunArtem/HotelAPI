package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.HistogramDto;
import org.example.dto.HotelCreateDto;
import org.example.dto.HotelDetailsDto;
import org.example.dto.HotelSummaryDto;
import org.example.mapper.DtoConverter;
import org.example.model.Amenity;
import org.example.model.Hotel;
import org.example.repository.AmenityRepository;
import org.example.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final DtoConverter dtoConverter;

    @Override
    @Transactional(readOnly = true)
    public List<HotelSummaryDto> getAllHotels() {
        log.info("Fetching all hotels");
        return hotelRepository.findAll().stream()
                .map(dtoConverter::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDetailsDto getHotelById(Long id) {
        log.info("Fetching hotel by id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + id));
        return dtoConverter.convertToDetailDto(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelSummaryDto> searchHotels(String name, String brand, String city, String country, String amenity) {
        log.info("Searching hotels with params - name: {}, brand: {}, city: {}, country: {}, amenity: {}",
                name, brand, city, country, amenity);
        return hotelRepository.searchHotels(name, brand, city, country, amenity).stream()
                .map(dtoConverter::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public HotelSummaryDto createHotel(HotelCreateDto hotelCreateDto) {
        log.info("Creating new hotel: {}", hotelCreateDto.getName());
        Hotel hotel = dtoConverter.convertToEntity(hotelCreateDto);
        Hotel savedHotel = hotelRepository.save(hotel);
        return dtoConverter.convertToSummaryDto(savedHotel);
    }

    @Override
    @Transactional
    public void addAmenitiesToHotel(Long hotelId, List<String> amenityNames) {
        log.info("Adding amenities to hotel {}: {}", hotelId, amenityNames);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + hotelId));

        List<Amenity> amenities = amenityNames.stream()
                .map(name -> amenityRepository.findByName(name)
                        .orElseGet(() -> {
                            Amenity newAmenity = Amenity.builder().name(name).build();
                            return amenityRepository.save(newAmenity);
                        }))
                .toList();

        hotel.getAmenities().addAll(amenities);
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public HistogramDto getHistogram(String param) {
        log.info("Generating histogram for param: {}", param);
        Map<String, Long> histogramData = new LinkedHashMap<>();

        List<Object[]> results = switch (param.toLowerCase()) {
            case "city" -> hotelRepository.countByCity();
            case "brand" -> hotelRepository.countByBrand();
            case "country" -> hotelRepository.countByCountry();
            case "amenities" -> hotelRepository.countByAmenities();
            default -> throw new IllegalArgumentException("Invalid histogram parameter: " + param);
        };

        for (Object[] result : results) {
            histogramData.put((String) result[0], (Long) result[1]);
        }

        return HistogramDto.builder().histogram(histogramData).build();
    }
}
