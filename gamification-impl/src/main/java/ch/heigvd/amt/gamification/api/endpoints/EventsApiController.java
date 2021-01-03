package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.model.EventInfo;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.EventEntity;
import ch.heigvd.amt.gamification.entities.UserEntity;
import ch.heigvd.amt.gamification.repositories.EventRepository;
import ch.heigvd.amt.gamification.api.EventsApi;
import ch.heigvd.amt.gamification.api.model.Event;
import ch.heigvd.amt.gamification.repositories.UserRepository;
import ch.heigvd.amt.gamification.services.EventProcessor;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class EventsApiController implements EventsApi {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ServletRequest request;

    @Autowired
    EventProcessor eventProcessor;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createEvent(@ApiParam(value = "") @Valid @RequestBody(required = false) Event event) {
        // Tried the @Transactional notation but didn't find success so used synchronized block instead
        // Can have a concurrency problem if same events with same user arrive at same time will write th user twice
        synchronized (EventsApiController.class) {
            // Get application entity
            ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

            // Create the user if not present in the repository
            UserEntity userEntity = userRepository.findByUserAppIdAndAppApiKey(event.getUserAppId(), app.getApiKey());
            if (userEntity == null) {
                userEntity = new UserEntity();
                userEntity.setUserAppId(event.getUserAppId());
                userEntity.setBadges(new ArrayList<>());
                userEntity.setApp(app);
                userRepository.save(userEntity);
            }

            // Create and save the event entity
            EventEntity newEventEntity = toEventEntity(event);
            newEventEntity.setApp(app);
            eventRepository.save(newEventEntity);

            eventProcessor.processEvent(app, newEventEntity);

            // Return the id of the event entity
            Long id = newEventEntity.getId();
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newEventEntity.getId()).toUri();
            return ResponseEntity.created(location).build();
        }
    }

    public ResponseEntity<List<EventInfo>> getEvents() {

        // Get application entity
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        // Get and return the list of events
        List<EventInfo> events = new LinkedList<>();
        for (EventEntity eventEntity : eventRepository.findAllByApp(app)) {
            events.add(toEventInfo(eventEntity));
        }
        return ResponseEntity.ok(events);

    }

    @Override
    public ResponseEntity<Event> getEvent(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {

        // Get application entity
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        // Get and return the event
        EventEntity existingEventEntity = eventRepository.findById(Long.valueOf(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(toEvent(existingEventEntity));
    }

    private EventEntity toEventEntity(Event event) {
        EventEntity entity = new EventEntity();
        entity.setUserAppId(event.getUserAppId());
        entity.setTimestamp(event.getTimestamp());
        entity.setEventType(event.getEventType());
        return entity;
    }

    private Event toEvent(EventEntity entity) {
        Event event = new Event();
        event.setUserAppId(entity.getUserAppId());
        event.setTimestamp(entity.getTimestamp());
        event.setEventType(entity.getEventType());
        return event;
    }

    private EventInfo toEventInfo(EventEntity entity) {
        EventInfo event = new EventInfo();
        event.setUserAppId(entity.getUserAppId());
        event.setTimestamp(entity.getTimestamp());
        event.setEventType(entity.getEventType());
        event.setId((int)entity.getId());
        return event;
    }

}
