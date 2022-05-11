

/**
 * Alle Clients schicken Anfragen an den einen zentralen ServerSocket.
 * Die Antwort erfolgt jedoch nicht über diesen ServerSocket, sondern über einen separaten, auf einem anderen Port laufenden, Socket.
 * Dadurch kann der Haupt-Port sofort wieder freigegeben und damit für andere Clients verfügbar gemacht werden.
 *
 * → ein port kann doch mehrere Verbindungen eingehen?
 * → dann ist das doch trotzdem nebenläufig auf der Serverseite?
 * → was meinte der Müller auch mit der e)?
 *
 */

public class A {
}
