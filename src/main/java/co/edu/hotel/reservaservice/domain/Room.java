package co.edu.hotel.reservaservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("Room")
public class Room {

    private UUID id;

    private String code;

    private Hotel hotel;

    public Room(UUID id, String code, Hotel hotel) {
        this.id = id;
        this.code = code;
        this.hotel = hotel;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
