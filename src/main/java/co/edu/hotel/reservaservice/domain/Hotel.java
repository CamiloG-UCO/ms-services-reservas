package co.edu.hotel.reservaservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("Hotel")
public class Hotel {
    private UUID id;

    private String name;

    public Hotel(){
    }

    public Hotel(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
