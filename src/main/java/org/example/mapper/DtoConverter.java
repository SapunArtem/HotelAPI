package org.example.mapper;

import org.example.dto.*;
import org.example.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DtoConverter {
    public HotelSummaryDto convertToSummaryDto(Hotel hotel) {
        if (hotel == null) return null;

        return HotelSummaryDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(formatAddress(hotel.getAddress()))
                .phone(hotel.getContacts() != null ? hotel.getContacts().getPhone() : null)
                .build();
    }

    public HotelDetailsDto convertToDetailDto(Hotel hotel) {
        if (hotel == null) return null;

        return HotelDetailsDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .brand(hotel.getBrand())
                .address(convertToAddressDto(hotel.getAddress()))
                .contacts(convertToContactsDto(hotel.getContacts()))
                .arrivalTime(convertToArrivalTimeDto(hotel.getArrivalTime()))
                .amenities(hotel.getAmenities().stream()
                        .map(Amenity::getName)
                        .collect(Collectors.toList()))
                .build();
    }

    public Hotel convertToEntity(HotelCreateDto dto) {
        if (dto == null) return null;

        Address address = null;
        if (dto.getAddress() != null) {
            address = Address.builder()
                    .houseNumber(dto.getAddress().getHouseNumber())
                    .street(dto.getAddress().getStreet())
                    .city(dto.getAddress().getCity())
                    .country(dto.getAddress().getCountry())
                    .postCode(dto.getAddress().getPostCode())
                    .build();
        }

        Contacts contacts = null;
        if (dto.getContacts() != null) {
            contacts = Contacts.builder()
                    .phone(dto.getContacts().getPhone())
                    .email(dto.getContacts().getEmail())
                    .build();
        }

        ArrivalTime arrivalTime = null;
        if (dto.getArrivalTime() != null) {
            arrivalTime = ArrivalTime.builder()
                    .checkIn(dto.getArrivalTime().getCheckIn())
                    .checkOut(dto.getArrivalTime().getCheckOut())
                    .build();
        }

        return Hotel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .brand(dto.getBrand())
                .address(address)
                .contacts(contacts)
                .arrivalTime(arrivalTime)
                .build();
    }

    public AddressDto convertToAddressDto(Address address) {
        if (address == null) return null;

        return AddressDto.builder()
                .houseNumber(address.getHouseNumber())
                .street(address.getStreet())
                .city(address.getCity())
                .country(address.getCountry())
                .postCode(address.getPostCode())
                .build();
    }

    public ContactsDto convertToContactsDto(Contacts contacts) {
        if (contacts == null) return null;

        return ContactsDto.builder()
                .phone(contacts.getPhone())
                .email(contacts.getEmail())
                .build();
    }

    public ArrivalTimeDto convertToArrivalTimeDto(ArrivalTime arrivalTime) {
        if (arrivalTime == null) return null;

        return ArrivalTimeDto.builder()
                .checkIn(arrivalTime.getCheckIn())
                .checkOut(arrivalTime.getCheckOut())
                .build();
    }

    private String formatAddress(Address address) {
        if (address == null) return null;
        return String.format("%d %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry());
    }
}
