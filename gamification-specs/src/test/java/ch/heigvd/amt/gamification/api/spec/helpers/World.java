package ch.heigvd.amt.gamification.api.spec.helpers;

import ch.heigvd.amt.gamification.api.dto.*;
import lombok.Getter;
import lombok.Setter;

public class World {
    @Getter @Setter
    Event event;
    @Getter @Setter
    Badge badge;
    @Getter @Setter
    Rule rule;
    @Getter @Setter
    static Application app;
    @Getter @Setter
    InlineObject obj;
    @Getter @Setter
    PointScale pointScale;
    @Getter @Setter
    Stage stage;
    @Getter @Setter
    Event lastReceivedEvent;
    @Getter @Setter
    Badge lastReceivedBadge;
}