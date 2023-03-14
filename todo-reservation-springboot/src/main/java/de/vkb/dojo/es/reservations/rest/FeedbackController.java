package de.vkb.dojo.es.reservations.rest;

import de.vkb.dojo.es.common.model.feedback.Feedback;
import de.vkb.dojo.es.common.model.feedback.SuccessFeedback;
import de.vkb.dojo.es.common.model.feedback.UnknownFeedback;
import de.vkb.dojo.es.common.model.ref.FeedbackReference;
import de.vkb.dojo.es.reservations.kafka.view.FeedbackStore;
import de.vkb.dojo.es.reservations.services.ReferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    final ReferenceService referenceService;
    final FeedbackStore feedbackStore;

    public FeedbackController(ReferenceService referenceService, FeedbackStore feedbackStore) {
        this.referenceService = referenceService;
        this.feedbackStore = feedbackStore;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Feedback> read(@PathVariable String id) {
        var feedback = feedbackStore.get(id);
        if (feedback == null) {
            return ResponseEntity.status(HttpStatus.TOO_EARLY)
                    .header("Location", new FeedbackReference(id).getPath())
                    .body(new UnknownFeedback());
        } else if (feedback instanceof SuccessFeedback) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .header("Location", ((SuccessFeedback) feedback).getReference().getPath())
                    .body(feedback);
        } else {
            return ResponseEntity.ok(feedback);
        }
    }
}
