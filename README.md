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

alle Topics sind für den Einstieg mit nur einer Partition konfiguriert.

## Topology Layout

in diesem Beispiel wird das CQRS Pattern mit folgenden Topologien/Streams umgesetzt:

![Ui Bunt](https://github.com/versicherungskammer/coding-dojo-event-sourcing/raw/main/topology.png)


## Aufgabe

- Sucht euch eine der beiden `todo` Projekte aus (Micronaut/Kotlin oder SpringBoot/Java)
- Implementiere die relevanten Streams (markiert mit vielen `TODO` Markern, die Klassen heissen `StreamFactory` bzw `StreamTopologiesFactory`)
- Wenn alle Stricke reissen, kann man ja in einem der beiden Upstream Projekte nachschlagen
- Wichtig: ReservierungCommand hat nur ID des Raums/Person, im Event soll aber eine Struct gespeichert werden (Events müssen self-contained sein, dürfen keine Abhängigkeit zu anderen Systemen haben)
- die Klassen StoreNames und TopicNames (werden aus der application.yml befüllt) beinhalten eine Reihe von Topic/Store-Namen, die man nutzen kann (Stores kann man natürlich gern bei Bedarf erweitern) 
- Nutze die GUI oder Curl (bzw die Hilfs-Bash-Skripte), um Räume/Personen und Reservierungen zu erstellen

Im ersten Wurf ist die Datenstruktur bewusst einfach gehalten: Reservierungen haben erstmal keinen Zeitraum (Tag)

Fortschrittliche Aufgabe:
- Erweitert das Datenmodell der Reservierungen um einen Tag (inkl. Validierung)
- Dazu dann natürlich auch im http-gui die Reservierungs-GUI
- ein Raum soll natürlich nicht gleichzeitig mehrfach reservierbar sein
- auch soll eine Reservierung nicht nachträglich erstellbar sein
- Bonus-Punkte, wenn das System Replay-fähig ist und mit mehreren Partitionen/horizontaler Skalierung umgehen kann
