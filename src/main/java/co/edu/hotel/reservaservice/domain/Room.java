package co.edu.hotel.reservaservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Setter
@Getter
@Document("Room")
public class Room {

    @Id
    private String id;

    private String code;
    private Hotel hotel;

    public Room() {}

    public Room(String id, String code, Hotel hotel) {
        this.id = id;
        this.code = code;
        this.hotel = hotel;
    }
}
