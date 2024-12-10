package de.hhu.cs.dbs.dbwk.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnbieterDTO(@JsonProperty("anbieterid") int anbieterid,
                          @JsonProperty("buergerid") int buergerid,
                          @JsonProperty("berufbezeichnung") String berufbezeichnung,
                          @JsonProperty("gruendungsjahr") int gruendungsjahr,
                          @JsonProperty("email") String email,
                          @JsonProperty("passwort") String passwort,
                          @JsonProperty("vorname") String vorname,
                          @JsonProperty("nachname") String nachname) {
}
//"anbieterid": 0,
//        "buergerid": 0,
//        "berufbezeichnung": "string",
//        "gruendungsjahr": 0,
//        "email": "string",
//        "passwort": "string",
//        "vorname": "string",
//        "nachname": "string"