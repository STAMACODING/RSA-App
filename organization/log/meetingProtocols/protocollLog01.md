# LOG - Meeting No. 1 (27.05.20, 11:00Uhr) 📝

## Logging-System

### Strukturierung von .log Dateien

{Time:Timezone}[State][Client:ID/Server]{Class}[messageKind]: Message

{Zeitpunkt}[Status der Applikation][Client oder Server]{Klasse, aus der die Information stammt}[Log-Typ]: Information

### Hauptziele

#### File System

Modulares Speicher- und Lesesystem von Dateien, das für verschiedene Zwecke genutzt werden kann.

#### Debug-Klasse

Klasse, mit verschiedenen Tools zum Debuggen. Beinhaltet die Log-Klasse, um die es hier geht.

#### Log-Klasse

Modulare Log-Klasse mit verschieden Einstellungen zum Ausgeben von .log Dateien. Sortierung, Auswahl, etc. beim Debuggen.

### Infos für die Anderen

Um Information in .log Dateien bzw. in der Konsole auzugeben, stellen wir eine Funktion zur Verfügung, die sich darum kümmert. Hierbei appellieren wir dafür, dass alle ausschließlich unsere Funktionen nutzen, um Informationen zum Testen, etc. auszugeben, um eine einheitliche Lösung zu garantieren.

Je mehr Informationen in das Logging-System eingegeben werden, desto effizienter ist später das Debuggen! Entscheidet aber selbst, ob es sinnvoll ist, die jeweilige Information auszugeben!

Ihr werdet in unserer Klasse auf mehrere Funktionen mit dem selben Aufbau stößen, von denen Ihr euch eine aussuchen müsst. Die Syntax ist jeweils identisch - nur die **Art** der Information unterscheidet sich.

### Variablen für die Log-Funktionen

- Time

   2008-02-01T09:00:22,1111+05

   --> 09:00:22 und 1111 ms (Zeitzone + 5h) am 1. Februar im Jahre 2008

- State

   öffentliche Variable

- Client/Server

   öffentliche Variable?

- Class

   String (Name aus Funktion)

- messageKind

   gegeben durch Funktion (Log-Type)

- Message

   String

#### Beispiel

Log.debug(Klasse, Information);

--> {Time:Timezone}[State][Client:ID/Server]{Class}[messageKind]: Message

### Log-Typen

Log.test();

- _test();_
  
  Prozesse, die nur während der Implementation oder des Testens gelogt werden. Alle Funktionen, die _.test()_ benutzen, sollen spätestens beim Release in _.debug()_ verändert werden.

- _debug();_

   Alle Meldungen, die keine besondere Priorität haben. Hiermit sollen keine Fehler ausgegeben werden, sondern generelle Informationen.

- _warning();_

   Fehlermeldungen, die **nicht kritisch** sind, aber dennoch auf Fehler hinweisen. Achtet hierbei darauf, ob es sich bei der gewünschten Information um eine generelle Information oder eine Fehlermeldung handeln soll.

- _error();_

   Fehlermeldungen, die **kritisch** sind, d.h., dass sie das Programm zum abstürzen bringen würden. Empfohlen wird, dass die **try - catch** Funktion in Verbindung mit diesem Log-Type genutzt wird.

### Nächste Ziele

- Debug bzw. Log Klasse

- Konsolenoutput

- public Interface

- einzelne Log Funktionen
