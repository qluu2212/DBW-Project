package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GruppeDTO(@JsonProperty("gruppeid") int gruppeid,
                        @JsonProperty("bezeichnung") String bezeichnung,
                        @JsonProperty("sprache") String sprache,
                        @JsonProperty("ist_privat") boolean ist_privat) {
}
//"gruppeid": 0,
//        "bezeichnung": "string",
//        "sprache": "string",
//        "ist_privat": true