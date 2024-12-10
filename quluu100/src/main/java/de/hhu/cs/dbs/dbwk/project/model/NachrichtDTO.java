package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NachrichtDTO(@JsonProperty("anzeigeid") int anzeigeid,
                           @JsonProperty("buergerid") int buergerid,
                           @JsonProperty("text") String text) {
}
