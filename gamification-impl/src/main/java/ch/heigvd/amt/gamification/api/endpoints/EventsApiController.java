package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.EventEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class EventsApiController implements EventsApi {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ServletRequest request;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createEvent(@ApiParam(value = ""  )  @Valid @RequestBody(required = false) Event event) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        EventEntity newEventEntity = toEventEntity(event);
        newEventEntity.setApp(app);
        eventRepository.save(newEventEntity);
        Long id = newEventEntity.getId();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newEventEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    public ResponseEntity<List<Event>> getEvents() {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");

        List<Event> events = new LinkedList<>();
        for (EventEntity eventEntity : eventRepository.findAll()) {
            //TODO compare Apps and not apiKey
            if(eventEntity.getApp().getApiKey().equals(app.getApiKey())) {
                events.add(toEvent(eventEntity));
            }
        }

        return ResponseEntity.ok(events);
    }

    @Override
    public ResponseEntity<Event> getEvent(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        ApplicationEntity app = (ApplicationEntity) request.getAttribute("ApplicationEntity");
        if (app == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

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
