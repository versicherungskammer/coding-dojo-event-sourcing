package de.vkb.dojo.es.reservations.rest

import de.vkb.dojo.es.common.model.ref.FeedbackReference
import de.vkb.dojo.es.common.model.ref.Reference
import de.vkb.dojo.es.reservations.kafka.view.ReservationStore
import de.vkb.dojo.es.reservations.model.command.CreateReservation
import de.vkb.dojo.es.reservations.model.command.DeleteReservation
import de.vkb.dojo.es.reservations.model.command.ReservationCommand
import de.vkb.dojo.es.reservations.model.dto.ReservationOutput
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.http.uri.UriBuilder
import java.net.URI
import java.util.*

@Controller("/reservations")
class ReservationController(
    private val reservationStore: ReservationStore,
    private val reservationCommandProducer: de.vkb.dojo.es.reservations.kafka.ReservationCommandProducer,
    private val httpHostResolver: HttpHostResolver
) {
    private fun sendCommand(request: HttpRequest<*>, lambda: (id: String) -> ReservationCommand): HttpResponse<Reference> {
        val feedback = FeedbackReference(id = UUID.randomUUID().toString())
        val command = lambda(feedback.id)
        reservationCommandProducer.sendCommand(command.operationId, command)
        return HttpResponse.accepted<Reference?>()
            .header("Location", UriBuilder.of(URI(httpHostResolver.resolve(request))).path(feedback.path).build().toString())
            .body(feedback)
    }

    @Get("/")
    fun list(): List<ReservationOutput> {
        return reservationStore.all()
    }

    @Post("/")
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(request: HttpRequest<*>, @Body data: ReservationCreateData): HttpResponse<Reference> {
        return sendCommand(request) { operationId ->
            CreateReservation(
                operationId = operationId,
                room = data.room,
                person = data.person
            )
        }
    }

    @Get("/{aggregateId}")
    fun read(@PathVariable aggregateId: String): Optional<ReservationOutput> {
        return reservationStore.get(aggregateId)
    }

    @Delete("/{aggregateId}")
    fun delete(request: HttpRequest<*>, @PathVariable aggregateId: String): HttpResponse<Reference> {
        return sendCommand(request) { operationId ->
            DeleteReservation(
                operationId = operationId,
                aggregateId = aggregateId
            )
        }
    }

    data class ReservationCreateData(
        val room: String,
        val person: String
    )
}
