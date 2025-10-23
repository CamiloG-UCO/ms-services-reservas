package co.edu.hotel.reservaservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("Status")
public class Status {
    private UUID id;

    private String status;

    public Status(){
    }

    public Status(UUID id, String status) {
        this.id = id;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
