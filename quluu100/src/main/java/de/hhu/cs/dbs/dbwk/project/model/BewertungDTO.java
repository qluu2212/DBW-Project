package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BewertungDTO(@JsonProperty("titel") String titel,
                           @JsonProperty("beschreibung") String beschreibung,
                           @JsonProperty("startdatum") String startdatum,
                           @JsonProperty("enddatum") String enddatum,
                           @JsonProperty("kosten") int kosten,
                           @JsonProperty("eventid") int eventid,
                           @JsonProperty("avg_bewertung") double avg_bewertung) {
}
//"titel": "string",
//        "beschreibung": "string",
//        "startdatum": "2024-04-29",
//        "enddatum": "2024-04-29",
//        "kosten": 0,
//        "eventid": 0,
//        "avg_bewertung": 0