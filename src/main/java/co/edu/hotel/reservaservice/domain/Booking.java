package co.edu.hotel.reservaservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Document("booking")
public class Booking {

    @Id
    private String id;

    private Client client;
    private Room room;
    private Status status;
    private String date;

    public Booking() {
        this.id = UUID.randomUUID().toString();
    }

    public Booking(String id, Client client, Room room, Status status, Date date) {
        this.id = id;
        this.client = client;
        this.room = room;
        this.status = status;
        this.date = date.toString();
    }
}
