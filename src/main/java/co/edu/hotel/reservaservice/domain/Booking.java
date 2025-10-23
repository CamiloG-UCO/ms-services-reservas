package co.edu.hotel.reservaservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document("booking")
public class Booking {

    @Id
    private UUID id;

    private Client client;

    private Room room;

    private Status status;

    private Date date;

    public Booking(){
    }

    public Booking(UUID id, Client client, Room room, Status status, Date date) {
        this.id = id;
        this.client = client;
        this.room = room;
        this.status = status;
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
