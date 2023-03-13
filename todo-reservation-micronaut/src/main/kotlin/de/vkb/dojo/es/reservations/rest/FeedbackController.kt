package de.vkb.dojo.es.reservations.rest

import de.vkb.dojo.es.common.model.feedback.Feedback
import de.vkb.dojo.es.common.model.feedback.SuccessFeedback
import de.vkb.dojo.es.common.model.feedback.UnknownFeedback
import de.vkb.dojo.es.common.model.ref.FeedbackReference
import de.vkb.dojo.es.reservations.kafka.view.FeedbackStore
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.http.uri.UriBuilder
import org.apache.kafka.streams.KeyValue
import java.net.URI
import java.util.*

@Controller("/feedback")
class FeedbackController(
    private val feedbackStore: FeedbackStore,
    private val httpHostResolver: HttpHostResolver
) {
    @Get("/")
    fun list(): List<KeyValue<String, Feedback>> {
        return feedbackStore.all()
    }

    @Get("/{id}")
    fun read(request: HttpRequest<*>, @PathVariable id: String): HttpResponse<Feedback> {
        val feedback = feedbackStore.get(id)
            .orElse(UnknownFeedback())
        return when(feedback) {
            is SuccessFeedback ->
                HttpResponse.seeOther<Feedback>(UriBuilder.of(URI(httpHostResolver.resolve(request))).path(feedback.reference.path).build())
                    .body(feedback)
            is UnknownFeedback ->
                HttpResponse.status<Feedback>(HttpStatus.TOO_EARLY)
                    .header("Location", UriBuilder.of(URI(httpHostResolver.resolve(request))).path(FeedbackReference(id).path).build().toString())
                    .body(UnknownFeedback())
            else ->
                HttpResponse.ok(feedback)
        }
    }

}
