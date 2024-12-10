package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpezDTO(@JsonProperty("spezialisierungid") int spezialisierungid,
                      @JsonProperty("bezeichnung") String bezeichnung) {
}
//"spezialisierungid": 0,
//        "bezeichnung": "string"