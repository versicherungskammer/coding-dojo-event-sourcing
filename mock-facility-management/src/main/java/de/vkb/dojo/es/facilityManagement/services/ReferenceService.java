package de.vkb.dojo.es.facilityManagement.services;

import de.vkb.dojo.es.facilityManagement.model.feedback.ref.Reference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class ReferenceService {
    public ResponseEntity<Reference> sendResponse(HttpStatus status, Reference reference) {
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(reference.getPath())
                .build()
                .toUri()
                .toString();
        return ResponseEntity.status(status)
                .header("Location", location)
                .body(reference);
    }
}
