package ch.heigvd.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class RuleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String eventType;
    private Double pointsToAdd;
    private String badgeName;

    @ManyToOne
    private PointScaleEntity pointScaleEntity;

    @ManyToOne
    private ApplicationEntity app;
}
