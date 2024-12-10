package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WohnortDTO(@JsonProperty("adresseid") int adresseid,
                         @JsonProperty("strasse") String strasse,
                         @JsonProperty("hausnummer") String hausnummer,
                         @JsonProperty("plz") String plz,
                         @JsonProperty("stadt") String stadt) {
}
//"adresseid": 0,
//        "strasse": "string",
//        "hausnummer": "string",
//        "plz": "string",
//        "stadt": "string"
