package de.vkb.dojo.es.reservations.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.common.model.ref.FeedbackReference;
import de.vkb.dojo.es.common.model.ref.Reference;
import de.vkb.dojo.es.reservations.kafka.ReservationCommandProducer;
import de.vkb.dojo.es.reservations.kafka.view.ReservationStore;
import de.vkb.dojo.es.reservations.model.command.CreateReservation;
import de.vkb.dojo.es.reservations.model.command.DeleteReservation;
import de.vkb.dojo.es.reservations.model.command.ReservationCommand;
import de.vkb.dojo.es.reservations.model.dto.ReservationOutput;
import de.vkb.dojo.es.reservations.services.ReferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    final ReferenceService referenceService;
    final ReservationStore reservationStore;
    final ReservationCommandProducer roomCommandProducer;

    public ReservationController(ReferenceService referenceService, ReservationStore reservationStore, ReservationCommandProducer roomCommandProducer) {
        this.referenceService = referenceService;
        this.reservationStore = reservationStore;
        this.roomCommandProducer = roomCommandProducer;
    }

    private ResponseEntity<Reference> sendCommand(CommandOp lambda) {
        var feedback = new FeedbackReference(UUID.randomUUID().toString());
        var command = lambda.op(feedback.getId());
        roomCommandProducer.publish(command);
        return referenceService.sendResponse(HttpStatus.ACCEPTED, feedback);
    }

    @GetMapping(value = "", produces = "application/json")
    public List<ReservationOutput> list() {
        return reservationStore.getAll();
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Reference> create(@RequestBody ReservationCreateData data) {
        return sendCommand( id -> new CreateReservation(
                id,
                data.getRoom(),
                data.getPerson()
        ));
    }

    @GetMapping(value = "/{aggregateId}", produces = "application/json")
    public ReservationOutput read(@PathVariable String aggregateId) {
        return reservationStore.get(aggregateId).orElse(null);
    }

    @DeleteMapping(value = "/{aggregateId}", produces = "application/json")
    public ResponseEntity<Reference> delete(@PathVariable String aggregateId) {
        return sendCommand( operationId -> new DeleteReservation(
                operationId,
                aggregateId
        ));
    }

    static class ReservationCreateData {
        private final String room;
        private final String person;

        @JsonCreator
        public ReservationCreateData(
                @JsonProperty("room") String room,
                @JsonProperty("person") String person
        ) {
            this.room = room;
            this.person = person;
        }

        @JsonGetter
        public String getRoom() {
            return room;
        }

        @JsonGetter
        public String getPerson() {
            return person;
        }
    }

    interface CommandOp {
        ReservationCommand op(String operationId);
    }
}
