package de.vkb.dojo.es.humanResources.rest

import de.vkb.dojo.es.humanResources.kafka.PersonCommandProducer
import de.vkb.dojo.es.humanResources.kafka.view.PersonStore
import de.vkb.dojo.es.humanResources.model.Change
import de.vkb.dojo.es.humanResources.model.command.*
import de.vkb.dojo.es.humanResources.model.dto.PersonOutput
import de.vkb.dojo.es.humanResources.model.ref.FeedbackReference
import de.vkb.dojo.es.humanResources.model.ref.Reference
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.http.uri.UriBuilder
import java.net.URI
import java.util.*

@Controller("/persons")
class PersonController(
    private val personStore: PersonStore,
    private val personCommandProducer: PersonCommandProducer,
    private val httpHostResolver: HttpHostResolver
) {
    private fun sendCommand(request: HttpRequest<*>, lambda: (id: String) -> PersonCommand): HttpResponse<Reference> {
        val feedback = FeedbackReference(id = UUID.randomUUID().toString())
        val command = lambda(feedback.id)
        personCommandProducer.sendCommand(command.operationId, command)
        return HttpResponse.accepted<Reference?>()
            .header("Location", UriBuilder.of(URI(httpHostResolver.resolve(request))).path(feedback.path).build().toString())
            .body(feedback)
    }

    @Get("/")
    fun list(): List<PersonOutput> {
        return personStore.all()
    }

    @Post("/")
    @Consumes(MediaType.APPLICATION_JSON)
    fun create(request: HttpRequest<*>, @Body data: PersonCreateData): HttpResponse<Reference> {
        return sendCommand(request) { operationId ->
            CreatePerson(
                operationId = operationId,
                username = data.username,
                fullname = data.fullname
            )
        }
    }

    @Get("/{aggregateId}")
    fun read(@PathVariable aggregateId: String): Optional<PersonOutput> {
        return personStore.get(aggregateId)
    }

    @Put("/{aggregateId}")
    @Consumes(MediaType.APPLICATION_JSON)
    fun update(request: HttpRequest<*>, @PathVariable aggregateId: String, @Body data: PersonUpdateData): HttpResponse<Reference> {
        return sendCommand(request) { operationId ->
            EditPerson(
                operationId = operationId,
                aggregateId = aggregateId,
                username = if (data.newUsername.isNullOrBlank()) null else Change(data.oldUsername ?: "", data.newUsername),
                fullname = if (data.newFullname.isNullOrBlank()) null else Change(data.oldFullname ?: "", data.newFullname)
            )
        }
    }

    @Delete("/{aggregateId}")
    fun delete(request: HttpRequest<*>, @PathVariable aggregateId: String): HttpResponse<Reference> {
        return sendCommand(request) { operationId ->
            DeletePerson(
                operationId = operationId,
                aggregateId = aggregateId
            )
        }
    }

    @Post("/{aggregateId}/sick")
    fun sick(request: HttpRequest<*>, @PathVariable aggregateId: String): HttpResponse<Reference> {
        return sendCommand(request) { operationId ->
            MarkPersonAsSick(
                operationId = operationId,
                aggregateId = aggregateId
            )
        }
    }

    @Delete("/{aggregateId}/sick")
    fun healthy(request: HttpRequest<*>, @PathVariable aggregateId: String): HttpResponse<Reference> {
        return sendCommand(request) { operationId ->
            MarkPersonAsHealthy(
                operationId = operationId,
                aggregateId = aggregateId
            )
        }
    }

    data class PersonCreateData(
        val username: String,
        val fullname: String
    )

    data class PersonUpdateData(
        val oldUsername: String?,
        val newUsername: String?,
        val oldFullname: String?,
        val newFullname: String?
    )
}
