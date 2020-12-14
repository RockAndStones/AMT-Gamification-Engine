package ch.heigvd.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class BadgeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private Boolean usable;

    @ManyToMany(mappedBy = "badges")
    private List<UserEntity> users;

    @PreRemove
    private void removeBadgeFromUsers() {
        for (UserEntity u : users) {
            u.getBadges().remove(this);
        }
    }

    @ManyToOne
    private ApplicationEntity app;
}
