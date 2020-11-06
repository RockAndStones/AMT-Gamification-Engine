package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.entities.EventEntity;
import ch.heigvd.amt.gamification.repositories.EventRepository;
import ch.heigvd.amt.gamification.api.EventsApi;
import ch.heigvd.amt.gamification.api.model.Event;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EventsApiController implements EventsApi {

    @Autowired
    EventRepository eventRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createEvent(@ApiParam(value = "", required = true) @Valid @RequestBody Event event) {
        EventEntity newEventEntity = toEventEntity(event);
        eventRepository.save(newEventEntity);
        Long id = newEventEntity.getId();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newEventEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<List<Event>> getEvents() {
        List<Event> events = new ArrayList<>();
        for (EventEntity eventEntity : eventRepository.findAll()) {
            events.add(toEvent(eventEntity));
        }
        return ResponseEntity.ok(events);
    }

    @Override
    public ResponseEntity<Event> getEvent(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        EventEntity existingEventEntity = eventRepository.findById(Long.valueOf(id)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(toEvent(existingEventEntity));
    }

    private EventEntity toEventEntity(Event event) {
        EventEntity entity = new EventEntity();
        entity.setUserId(event.getUserId());
        entity.setTimestamp(event.getTimestamp());
        entity.setEventType(event.getEventType());
//        entity.setEventProperties(event.getEventProperties());
        return entity;
    }

    private Event toEvent(EventEntity entity) {
        Event event = new Event();
        event.setUserId(entity.getUserId());
        event.setTimestamp(entity.getTimestamp());
        event.setEventType(entity.getEventType());
//        event.setEventProperties(entity.getEventProperties());
        return event;
    }

}
