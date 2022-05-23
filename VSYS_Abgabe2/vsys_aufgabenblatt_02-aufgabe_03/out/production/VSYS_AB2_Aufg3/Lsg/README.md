# Aufgabe 3: Nebenläufigkeitsgrade

Diese Aufgabe baut auf der Aufgabe 2 des Aufgabenblatts 1 auf. Es gibt dafür eine neue Version des Java-Archivs
`rm.requestResponse.jar` und der Datei `rm.requestResponse.javadoc.zip`. Beachten Sie dabei die neue Methode `cleanUp`
der Klasse `Component`.

## a)

Rufen Sie sich die Teilaufgabe e. der Aufgabe2 von Aufgabenblatt 1 in Erinnerung. Nennen Sie eine Möglichkeit, wie man
das Verhalten bei mehreren nebenläufigen Clients verbessern kann, insb. dann, wenn alle Instanzen, der Server und alle
Clients auf derselben Maschine ablaufen.

> Alle Clients schicken Anfragen an den einen zentralen `ServerSocket`. Die Antwort erfolgt jedoch nicht über diesen
> `ServerSocket`, sondern über einen separaten, auf einem anderen Port laufenden, `Socket`.
> Dadurch kann der Haupt-Port sofort wieder frei gegeben und damit für andere Clients verfügbar gemacht werden.

## b)

Erweitern Sie Ihre Lösung der Aufgabe 2 von Aufgabenblatt 1 so, dass beliebig viele Clients gleichzeitig mit dem nicht
nebenläufigen Server fehlerfrei kommunizieren können. Das bedeutet am Beispiel zweier Clients A und B, dass die Anfragen
von B nicht erst dann beantwortet werden, wenn alle Anfragen von A beantwortet wurden. Verwenden Sie zum Testen
beispielsweise 10 Clients mit ähnlichen Einstellungen, die gleichzeitig Anfragen an den Server stellen.

> Der Client sendet eine `Message`, die einen `sendPort` beinhaltet, an Haupt-Server-Port. Die Antwort hingegen erwartet
> er dann auf dem `sendPort`. Der Server kann auf diese Weise neue Anfragen Annehmen, bevor die vorherigen fertig
> bearbeitet wurden.

## c)

Erhöhen Sie die Nebenläufigkeit des Servers maximal, sodass die Wartezeiten der anfragenden Clients minimiert werden.
Wiederholen Sie den Test mit den 10 Clients und beobachten Sie das veränderte Verhalten.

> Neuer Thread pro Anfrage (oder dynamischer ThreadPool mit unbegrenzt Threads).

## d)

Integrieren Sie einen nebenläufigen ThreadCounter in den Server, der immer die gerade aktuelle genutzte Threadanzahl im
Server für den Anwender sichtbar in der Console ausgibt. Der Threadcounter soll dabei ohne Sleep-Zeiten auskommen.

> Der `ThreadCounter` prüft in einer Endlosschleife die Anzahl der laufenden Threads, und gibt diese aus falls sie sich
> seit der letzten Abfrage geändert haben sollte.

## e)

Erweitern Sie den Server aus Teilaufgabe c. und d. so, dass zudem auch noch ein konstanter und ein dynamischer
Thread-Pool für die Kontrolle des Nebenläufigkeitsgrads des Servers verwendet werden kann. Testen Sie erneut mit 10
Clients.

> Durch die `-executor` Flag können verschiedene `ExecutorService`s gewählt werden.
