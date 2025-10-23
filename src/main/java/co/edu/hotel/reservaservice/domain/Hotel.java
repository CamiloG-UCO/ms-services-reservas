package co.edu.hotel.reservaservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Setter
@Getter
@Document("Hotel")
public class Hotel {
    @Id
    private String Id;

    private String Name;

    public Hotel(){
    }

    public Hotel(String id, String name) {
        this.Id = id;
        this.Name = name;
    }

}
