package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

public record BuergerDTO(@JsonProperty("buergerid") int buergerid,
                         @JsonProperty("wohnortid") int wohnortid,
                         @JsonProperty("berufbezeichnung") String berufbezeichnung,
                         @JsonProperty("email") String email,
                         @JsonProperty("passwort") String passwort,
                         @JsonProperty("vorname") String vorname,
                         @JsonProperty("nachname") String nachname) {
}

//"buergerid": 0,
//        "wohnortid": 0,
//        "berufbezeichnung": "string",
//        "email": "string",
//        "passwort": "string",
//        "vorname": "string",
//        "nachname": "string"
