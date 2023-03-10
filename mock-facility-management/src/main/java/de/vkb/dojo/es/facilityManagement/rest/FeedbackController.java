package de.vkb.dojo.es.facilityManagement.rest;

import de.vkb.dojo.es.facilityManagement.model.feedback.Feedback;
import de.vkb.dojo.es.facilityManagement.model.feedback.SuccessFeedback;
import de.vkb.dojo.es.facilityManagement.model.feedback.UnknownFeedback;
import de.vkb.dojo.es.facilityManagement.model.feedback.ref.FeedbackReference;
import de.vkb.dojo.es.facilityManagement.services.ReferenceService;
import de.vkb.dojo.es.facilityManagement.services.store.FeedbackStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rooms")
public class FeedbackController {
    @Autowired
    ReferenceService referenceService;
    @Autowired
    FeedbackStore feedbackStore;

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Feedback> read(@PathVariable String id) {
        var feedback = feedbackStore.get(id);
        if (feedback == null) {
            return ResponseEntity.status(HttpStatus.TOO_EARLY)
                    .header("Location", new FeedbackReference(id).getPath())
                    .body(new UnknownFeedback());
        } else if (feedback instanceof SuccessFeedback) {
            return ResponseEntity.status(HttpStatus.TOO_EARLY)
                    .header("Location", ((SuccessFeedback) feedback).getReference().getPath())
                    .body(feedback);
        } else {
            return ResponseEntity.ok(feedback);
        }
    }
}
