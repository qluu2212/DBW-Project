package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnzeigeDTO(@JsonProperty("anzeigeid") int anzeigeid,
                         @JsonProperty("buergerid") int buergerid,
                         @JsonProperty("nachrichten_anzahl") int nachrichten_anzahl,
                         @JsonProperty("titel") String titel,
                         @JsonProperty("beschreibung") String beschreibung) {
}
//"anzeigeid": 0,
//        "buergerid": "string",
//        "nachrichten_anzahl": 0,
//        "titel": "string",
//        "beschreibung": "string"