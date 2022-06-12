package AB_02_EX_03;

import java.io.Serializable;

public class MySerializableClass implements Serializable {
    private static final long serialVersionUID = 10;

    /**
     * Eine SerialVersionUID wird als Versionsnummer bei der Serialisation automatisch jeder Klasse
     * hinzugefügt, die das Interface Serializable implementiert. Sie ist vom Typ long, kann vom
     * Entwickler selbst deklariert werden und muss dann als static final ausgezeichnet werden.
     * Der Zugriffsmodifikator private ist nicht vorgeschrieben, aber empfehlenswert.
     * Zur Berechnung einer SerialVersionUID werden die Elemente der Klasse der zu serialisierenden
     * Objekte (Felder, Methoden, Konstruktoren, etc.) hinzugezogen.
     */

    private int id;
    private String string;
    // B und C (C ist das transient keyword)
    transient private MyNonSerializableClass nonSer = null;

    MySerializableClass() {
        id = 1234;
        // B
        nonSer = new MyNonSerializableClass();
    }

    public void set(String string) {
        this.string = string;
    }

    public String toString() {
        // angepasst an B
        /* String nonSerText = nonSerializable.toString();
        return "id: " + id + "; string: " + string + "\n ---" + nonSerText;*/

        // angepasst an C
        return "id: " + id + "; string: " + string + "\n" + ((nonSer != null) ? (nonSer.toString()) : ("there is no nonSerializable!"));
    }
} 
	