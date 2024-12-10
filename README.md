# Datenbanken: Weiterführende Konzepte – Praktisches Projekt - Abgabe

## Aufgabenstellung

Das aktuell zu bearbeitende Blatt finden Sie im entsprechenden Repository in
der [Gruppe des aktuellen Durchlaufs](https://git.hhu.de/dbsinfo/teaching/datenbanken-weiterfuehrende-konzepte/project). Die für dieses Blatt hier vorgegebenen Dateien dienen als Vorlage für die Abgabe.

## Kritische Entscheidungen

Hinterlegen Sie bitte in demjenigen Unterabschnitt, der zum aktuell zu bearbeitenden Blatt gehört, Ihre kritischen Entscheidungen.

### Blatt 1

- Die E-Mail-Adresse ist der Primärschlüssel.
- Eine Gruppe muss mindestens eine Person enthalten.
- Die Öffentlichkeit der Gruppe wurde als Attribut dargestellt, das später als Spalte in der Tabelle "Gruppe" umgesetzt wird. Diese Spalte kann z.B. Werte wie "privat" oder "öffentlich" enthalten.
- Die Verantwortung für die Gruppe wird als Beziehung zwischen "Buerger" und "Gruppe" dargestellt. Die Bedingung "Buerger aus der Gruppe" kann in späteren Phasen umgesetzt werden.
- Außerdem wird festgelegt, welche Sprache in der Gruppe primär gesprochen wird. Die Sprache ist im Singular und wurde als Attribut darsgestellt z.B Englisch, Deutsch usw.
- Das Enddatum des Events ist ein optionales Attribut. Manche Events sind kostenpflichtig. Kosten ist ein Attribut von Event. Wenn ein Event kostenfrei ist, erhält Kosten den Wert 0€. 
- Die Entweder oder Beziehung wurde mit der Kardinalität [0,1] dargestellt. Es wird angenommen, dass eine Beziehung für das "Veranstalten" nicht ausreicht. Zum Beispiel, wenn das Event nicht von einem Bürger veranstaltet wird, entspricht dies einer 0 in der Kardinalität. Wenn es nur eine Beziehung gibt, kann nicht dargestellt werden, von welchem gewerblichen Anbieter das Event veranstaltet wurde. Daher wurden zwei Beziehungen für das "Veranstalten" erstellt.
- Jedes Event kann von beliebig vielen Bürgern bewertet werden, und jeder Bürger kann beliebig viele Events einmal bewerten.
- Jede Nachricht soll nur von genau einem Buerger geschickt werden.

### Blatt 2

- Korrigiert: Erstens wurde eine neue Beziehung gehoert_zu zwischen Nachricht und Anzeige mit den Kardinalitäten [1,1] und [0,*] erstellt. Zweitens wurde eine neue Beziehung adressiert zwischen Wohnort und Event hinzugefügt, auch mit Kardinalitäten [1,1] und [0,*], weil Event.Adresse nicht atomar ist. Drittens wurde die Beziehung ga_veranstaltet gelöscht und [1,1] wurde zwischen bg_veranstaltet und Event gesetzt.
- Die ID wurde als künstlicher Schlüssel für 
Entity hinzugefügt, für die kein primärer Schlüssel vorhanden ist.
- Die Verschmelzung wurde durchgeführt, wo die Kardinalität [1,1] vorkommt.
- Verschmelzungen: Buerger + BuergerHatWohnort, Buerger + BuergerUebt_ausBeruf, Gruppe + BuergerVerantwortetGruppe, Event + BuergerVeranstaltetEvent, Event + EventAdresse, Nachricht + BuergerSchicktNachricht, Anzeige + BuergerErstelltAnzeige, Nachricht + NachrichtGehoert_zuAnzeige.

### Blatt 3

- Beruf.Name, Faehigkeit.Bezeichnung, Spezialisierung.Bezeichnung bestehen nur aus Zeichen des lateinischen Alphabets und sind unique und case-insensitive.
- Gruppe.Gruppebezeichnung soll nicht unique sein, da beispielsweise zwei Gruppen mit derselben Bezeichnung in verschiedenen Sprachen existieren können.
- Gruppe. Oeffentlichkeit ist vom Typ Boolean mit den möglichen Werten true und false.
- Das Kosten eines Events kann nur als Zahlen mit 2 Nachkommastellen angegeben werden.
- Wenn eine Person zu 2 privaten Gruppen hinzugefügt wird, kann diese keine weitere Gruppe mehr betreten.
- In Bezug auf die 2. Anfrage "Geben Sie genau die Bürger aus, die mit mindestens einem weiteren Bürger zusammenwohnen, wobei beide den gleichen Beruf ausüben": Wenn ein Datensatz in der Tabelle wohnt_mit existiert, in dem zwei verschiedene Bürger denselben Beruf haben, sollen beide Bürger ausgegeben werden. Das heißt, Bürger 1 wohnt zusammen mit Bürger 2 und beide haben denselben Beruf.

### Blatt 4

- Die Autorisierung wurde unter Verwendung der vorhandenen E-Mail-Adressen in der Tabelle Buerger richtig durchgeführt.
- Wenn keine Kosten für das Event eingegeben werden, beträgt der Kostenwert dieses Events in der Datenbank 0€.
- Alle curl-Befehle in der Datei curl.sh wurden richtig ausgeführt.
