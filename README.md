# Event Sourcing Coding Dojo

## Szenario

es soll ein Raum-Buchungs-System programmiert werden in Event-Sourcing und CQRS Architektur mit Kafka als Kommunikations- und Persistenz-Schicht.

## Lektüre

[https://eventmodeling.org/posts/what-is-event-modeling/](https://eventmodeling.org/posts/what-is-event-modeling/)
[https://www.martinfowler.com/bliki/CQRS.html](https://www.martinfowler.com/bliki/CQRS.html)
[https://martinfowler.com/eaaDev/EventSourcing.html](https://martinfowler.com/eaaDev/EventSourcing.html)

## Environment

es gibt zwei Upstream Domänen:
- `facility-management` kümmert sich um Räume, ist implementiert in Spring-Boot und Java 
- `human-resources` kümmert sich um Personen, ist implementiert in Micronaut und Kotlin

beide nutzen (soweit vom Framework möglich) dieselben Klassen/Pattern/Namenskonventionen

die Domäne `reservations` soll zur Übung implementiert werden, dazu finden sich vorgefertigte Projekte in den Ordnern `todo-reservation-springboot` bzw `todo-reservation-micronaut` (eins auswählen, je nach Vorliebe)
die Projekte haben schon die Input/Output-Klassen (Commands, Events, States) festgenagelt, so dass die http-gui benutzbar ist.

mit der `docker-compose.yml` kann ein Verbund aus Zookeeper, Kafka, facility-management, human-resources und einer mini http-gui gestartet werden.
die http-gui ist unter [http://localhost:8079](http://localhost:8079) erreichbar.
*Hinweis*: wegen einer Race-Condition mit dem Anlegen der Topics ist es nötig, beim ersten Mal per Hand die beiden upstream backends neu zu starten:
````bash
docker-compose restart facility-management
docker-compose restart human-resources
````


## Topology Layout

in diesem Beispiel wird das CQRS Pattern mit folgenden Topologien/Streams umgesetzt:

![Ui Bunt](https://raw.githubusercontent.com/versicherungskammer/coding-dojo-event-sourcing/main/topology.png)
