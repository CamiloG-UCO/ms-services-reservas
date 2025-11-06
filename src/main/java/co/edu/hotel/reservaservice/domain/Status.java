package co.edu.hotel.reservaservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Setter
@Getter
@Document("Status")
public class Status {
    @Id
    private String Id;

    private String Status;

    public Status(){
    }

    public Status(String Id, String status) {
        this.Id = Id;
        this.Status = status;
    }

}
