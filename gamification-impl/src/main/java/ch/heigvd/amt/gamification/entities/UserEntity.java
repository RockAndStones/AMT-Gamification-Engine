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

    @ManyToMany
    @JoinTable(name = "user_badges",
            joinColumns = { @JoinColumn(name = "fk_user") },
            inverseJoinColumns = { @JoinColumn(name = "fk_badge") } )
    private List<BadgeEntity> badges;

    @ManyToOne
    private ApplicationEntity app;
}
