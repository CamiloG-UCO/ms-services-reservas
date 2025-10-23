package co.edu.hotel.reservaservice.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("Client")
public class Client {
    private UUID id;

    private String name;

    private String email;

    public Client(){
    }

    public Client(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
