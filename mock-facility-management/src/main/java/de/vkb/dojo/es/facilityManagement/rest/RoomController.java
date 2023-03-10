package de.vkb.dojo.es.facilityManagement.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.facilityManagement.kafka.RoomCommandProducer;
import de.vkb.dojo.es.facilityManagement.kafka.view.RoomStore;
import de.vkb.dojo.es.facilityManagement.model.Change;
import de.vkb.dojo.es.facilityManagement.model.command.*;
import de.vkb.dojo.es.facilityManagement.model.dto.RoomOutput;
import de.vkb.dojo.es.facilityManagement.model.ref.FeedbackReference;
import de.vkb.dojo.es.facilityManagement.model.ref.Reference;
import de.vkb.dojo.es.facilityManagement.services.ReferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    final ReferenceService referenceService;
    final RoomStore roomStore;
    final RoomCommandProducer roomCommandProducer;

    public RoomController(ReferenceService referenceService, RoomStore roomStore, RoomCommandProducer roomCommandProducer) {
        this.referenceService = referenceService;
        this.roomStore = roomStore;
        this.roomCommandProducer = roomCommandProducer;
    }

    private ResponseEntity<Reference> sendCommand(CommandOp lambda) {
        var feedback = new FeedbackReference(UUID.randomUUID().toString());
        var command = lambda.op(feedback.getId());
        roomCommandProducer.publish(command);
        return referenceService.sendResponse(HttpStatus.ACCEPTED, feedback);
    }

    @GetMapping(value = "", produces = "application/json")
    public List<RoomOutput> list() {
        return roomStore.getAll();
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Reference> create(@RequestBody RoomCreateData data) {
        return sendCommand( id -> new CreateRoom(
                id,
                data.name
        ));
    }

    @GetMapping(value = "/{aggregateId}", produces = "application/json")
    public RoomOutput read(@PathVariable String aggregateId) {
        return roomStore.get(aggregateId).orElse(null);
    }

    @PutMapping(value = "/{aggregateId}", produces = "application/json")
    public ResponseEntity<Reference> update(@PathVariable String aggregateId, @RequestBody RoomUpdateData data) {
        return sendCommand( operationId -> new EditRoom(
                operationId,
                aggregateId,
                new Change<>(data.oldName, data.newName)
        ));
    }

    @DeleteMapping(value = "/{aggregateId}", produces = "application/json")
    public ResponseEntity<Reference> delete(@PathVariable String aggregateId) {
        return sendCommand( operationId -> new DeleteRoom(
                operationId,
                aggregateId
        ));
    }

    @PostMapping(value = "/{aggregateId}/lock", produces = "application/json")
    public ResponseEntity<Reference> lock(@PathVariable String aggregateId) {
        return sendCommand( operationId -> new LockRoom(
                operationId,
                aggregateId
        ));
    }

    @DeleteMapping(value = "/{aggregateId}/unlock", produces = "application/json")
    public ResponseEntity<Reference> unlock(@PathVariable String aggregateId) {
        return sendCommand( operationId -> new UnlockRoom(
                operationId,
                aggregateId
        ));
    }

    static class RoomCreateData {
        private final String name;

        @JsonCreator
        public RoomCreateData(
                @JsonProperty("name") String name
        ) {
            this.name = name;
        }

        @JsonGetter
        public String getName() {
            return name;
        }
    }

    static class RoomUpdateData {
        private final String oldName;
        private final String newName;

        @JsonCreator
        public RoomUpdateData(
                @JsonProperty("oldName") String oldName,
                @JsonProperty("newName") String newName
        ) {
            this.oldName = oldName;
            this.newName = newName;
        }

        @JsonGetter
        public String getOldName() {
            return oldName;
        }

        @JsonGetter
        public String getNewName() {
            return newName;
        }
    }

    interface CommandOp {
        RoomCommand op(String operationId);
    }
}
