package ch.heigvd.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class ApplicationEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String apiKey;

    @Column(unique = true)
    private String name;

    @OneToMany
    private List<EventEntity> events;

    static public String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
