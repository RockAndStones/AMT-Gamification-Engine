package ch.heigvd.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long   id;
    private String userAppId;
    private double points;

    @OneToMany
    private List<BadgeEntity> badges;

    @ManyToOne
    private ApplicationEntity app;
}
