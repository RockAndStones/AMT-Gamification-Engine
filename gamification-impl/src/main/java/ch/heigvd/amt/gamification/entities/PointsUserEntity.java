package ch.heigvd.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class PointsUserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    double points;

    // TODO better implementation of pk and fk
    @ManyToOne
    private PointScaleEntity pointScale;

    @ManyToOne
    private UserEntity user;
}
