package org.example;

import org.example.model.*;
import org.example.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HotelRepositoryTest {
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Hotel hotel1;
    private Hotel hotel2;
    private Amenity amenity1;
    private Amenity amenity2;

    @BeforeEach
    void setUp(){

        amenity1 = Amenity.builder().name("Free Wifi").build();
        amenity2 = Amenity.builder().name("Pool").build();
        entityManager.persist(amenity1);
        entityManager.persist(amenity2);

        Address address1 = Address.builder()
                .houseNumber(10)
                .street("Main st")
                .city("Minsk")
                .country("Belarus")
                .postCode("220000")
                .build();
        Address address2 = Address.builder()
                .houseNumber(5)
                .street("Lenia")
                .city("Moscow")
                .country("Russia")
                .postCode("101000")
                .build();

        Contacts contacts1 = Contacts.builder()
                .phone("+3752911111")
                .email("minsk@hilton.com")
                .build();
        Contacts contacts2 = Contacts.builder()
                .phone("+749522222")
                .email("moscow@marriot.com")
                .build();


        ArrivalTime arrivalTime1 = ArrivalTime.builder()
                .checkIn("14:00")
                .checkOut("12:00")
                .build();
        ArrivalTime arrivalTime2 = ArrivalTime.builder()
                .checkIn("15:00")
                .checkOut("11:00")
                .build();


        hotel1 = Hotel.builder()
                .name("Hilton Minsk")
                .description("Luxary hotel")
                .brand("Hilton")
                .address(address1)
                .contacts(contacts1)
                .arrivalTime(arrivalTime1)
                .amenities(List.of(amenity1))
                .build();
        hotel2 = Hotel.builder()
                .name("Marriott Moscow")
                .description("Business hotel")
                .brand("Marriott")
                .address(address2)
                .contacts(contacts2)
                .arrivalTime(arrivalTime2)
                .amenities(List.of(amenity2))
                .build();

        entityManager.persist(hotel1);
        entityManager.persist(hotel2);
        entityManager.flush();

    }

    @Test
    void searchHotels_With_ReturnsMatchingHotels(){
        List<Hotel> result = hotelRepository.searchHotels(null, null, "Minsk", null, null);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Hilton Minsk");
    }

    @Test
    void searchHotels_WithAmenity_ReturnsMatchingHotels() {
        List<Hotel> results = hotelRepository.searchHotels(null, null, null, null, "Free WiFi");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Hilton Minsk");
    }

    @Test
    void searchHotels_WithMultipleParams_ReturnsCorrectResults() {
        List<Hotel> results = hotelRepository.searchHotels("Hilton", "Hilton", null, null, null);
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getBrand()).isEqualTo("Hilton");
    }

    @Test
    void countByCity_ReturnsCorrectCounts() {
        List<Object[]> counts = hotelRepository.countByCity();
        assertThat(counts).hasSize(2);
        assertThat(counts).anyMatch(arr -> arr[0].equals("Minsk") && (Long) arr[1] == 1);
        assertThat(counts).anyMatch(arr -> arr[0].equals("Moscow") && (Long) arr[1] == 1);
    }

    @Test
    void countByBrand_ReturnsCorrectCounts() {
        List<Object[]> counts = hotelRepository.countByBrand();
        assertThat(counts).hasSize(2);
        assertThat(counts).anyMatch(arr -> arr[0].equals("Hilton") && (Long) arr[1] == 1);
        assertThat(counts).anyMatch(arr -> arr[0].equals("Marriott") && (Long) arr[1] == 1);
    }

    @Test
    void countByCountry_ReturnsCorrectCounts() {
        List<Object[]> counts = hotelRepository.countByCountry();
        assertThat(counts).hasSize(2);
        assertThat(counts).anyMatch(arr -> arr[0].equals("Belarus") && (Long) arr[1] == 1);
        assertThat(counts).anyMatch(arr -> arr[0].equals("Russia") && (Long) arr[1] == 1);
    }

    @Test
    void countByAmenities_ReturnsCorrectCounts() {
        List<Object[]> counts = hotelRepository.countByAmenities();
        assertThat(counts).hasSize(2);
    }
}
