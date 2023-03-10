package de.vkb.dojo.es.facilityManagement.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.vkb.dojo.es.facilityManagement.kafka.RoomCommandProducer;
import de.vkb.dojo.es.facilityManagement.model.Change;
import de.vkb.dojo.es.facilityManagement.model.command.*;
import de.vkb.dojo.es.facilityManagement.model.feedback.ref.FeedbackReference;
import de.vkb.dojo.es.facilityManagement.model.feedback.ref.Reference;
import de.vkb.dojo.es.facilityManagement.model.state.Room;
import de.vkb.dojo.es.facilityManagement.services.ReferenceService;
import de.vkb.dojo.es.facilityManagement.services.store.RoomStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("rooms")
public class RoomController {
    @Autowired
    ReferenceService referenceService;
    @Autowired
    RoomStore roomStore;
    @Autowired
    RoomCommandProducer roomCommandProducer;
    
    private ResponseEntity<Reference> sendCommand(CommandOp lambda) {
        var feedback = new FeedbackReference(UUID.randomUUID().toString());
        var command = lambda.op(feedback.getId());
        roomCommandProducer.publish(command);
        return referenceService.sendResponse(HttpStatus.ACCEPTED, feedback);
    }

    @GetMapping(value = "/", produces = "application/json")
    public List<Room> list() {
        return roomStore.getAll();
    }

    @GetMapping(value = "/{aggregateId}", produces = "application/json")
    public Room read(@PathVariable String aggregateId) {
        return roomStore.get(aggregateId);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Reference> create(@RequestBody RoomCreateData data) {
        return sendCommand( id -> new CreateRoom(
                id,
                data.name
        ));
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
