package co.edu.hotel.reservaservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Setter
@Getter
@Document("Client")
public class Client {
    @Id
    private String Id;

    private String name;
    private String email;

    public Client() {
    }

    public Client(String Id, String name, String email) {
        this.Id = Id;
        this.name = name;
        this.email = email;
    }

}
