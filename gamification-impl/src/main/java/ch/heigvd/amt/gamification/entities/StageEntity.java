package ch.heigvd.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class StageEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double points;

    @ManyToOne
    private PointScaleEntity pointScale;

    @ManyToOne
    private BadgeEntity badge;

    @ManyToOne
    private ApplicationEntity app;
}
