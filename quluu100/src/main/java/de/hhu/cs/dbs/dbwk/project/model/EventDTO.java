package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EventDTO(@JsonProperty("eventid") int eventid,
                       @JsonProperty("wohnortid") int wohnortid,
                       @JsonProperty("titel") String titel,
                       @JsonProperty("beschreibung") String beschreibung,
                       @JsonProperty("startdatum") String startdatum,
                       @JsonProperty("enddatum") String enddatum,
                       @JsonProperty("kosten") Double kosten) {
}
//"eventid": 0,
//        "wohnortid": 0,
//        "titel": "string",
//        "beschreibung": "string",
//        "startdatum": "2024-04-28",
//        "enddatum": "2024-04-28",
//        "kosten": 0