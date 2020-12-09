package ch.heigvd.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class RuleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String eventType;
    private Double pointsToAdd;
    private String badgeName;

    @ManyToOne
    private PointScaleEntity pointScale;

    @ManyToOne
    private ApplicationEntity app;
}
