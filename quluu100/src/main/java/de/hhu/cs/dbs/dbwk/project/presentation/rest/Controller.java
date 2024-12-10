package de.hhu.cs.dbs.dbwk.project.presentation.rest;

import de.hhu.cs.dbs.dbwk.project.model.User;
import de.hhu.cs.dbs.dbwk.project.security.CurrentUser;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import javax.lang.model.type.NullType;
import java.lang.reflect.Type;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;


@RequestMapping("/")
@RestController
public class Controller {
    private final JdbcTemplate jdbcTemplate;
    private Appservice appservice;


    public Controller(JdbcTemplate jdbcTemplate, Appservice appservice) {
        this.jdbcTemplate = jdbcTemplate;
        this.appservice = appservice;
    }

//    @GetMapping("foo")
//    @RolesAllowed("BUERGER")
//    // GET http://localhost:8080/foo => OK. Siehe SQLiteUserRepository.
//    public String halloFoo(@CurrentUser User user) {
//        return "\"Hallo " + user.getUniqueString() + "\"!";
//    }
//
//    @GetMapping("foo2")
//    @RolesAllowed("BUERGER")
//    // GET http://localhost:8080/foo2 => FORBIDDEN. Siehe SQLiteUserRepository.
//    public String halloFoo2(@CurrentUser User user) {
//        return "\"Hallo " + user.getUniqueString() + "\"!";
//    }

    @PostMapping(value = "/gruppen")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> newGr(@CurrentUser User user, @RequestParam(name="bezeichnung", required = true) String bezeichnung,
                                        @RequestParam(name="sprache", required = true) String sprache,
                                        @RequestParam(name="ist_privat", required = true) Boolean ist_privat) {
        try {
            int row = jdbcTemplate.update("INSERT INTO Gruppe(Gruppenbezeichnung, Sprache, Oeffentlichkeit, BuergerEMailAdresse) VALUES (?,?,?,?)", bezeichnung, sprache, ist_privat, user.getUniqueString());

            if (row != 0) {
                Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(Gruppe.ID) FROM Gruppe", Integer.class);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, "/gruppen/" + maxRow);
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            }
        } catch (NoSuchElementException e){
            return ResponseEntity.status(404).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 404));
        } catch (DataAccessException e){
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 500));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error beim einfügen");
    }

    @PostMapping(value = "/events")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> newEvent(@CurrentUser User user, @RequestParam(name="titel", required = true) String titel,
                                      @RequestParam(name="beschreibung", required = true) String beschreibung,
                                      @RequestParam(name="startdatum", required = true) String startdatum,
                                      @RequestParam(name="enddatum", required = false) String enddatum,
                                      @RequestParam(name="kosten", required = false) Double kosten,
                                      @RequestParam(name="wohnortid", required = true) Integer wohnortid) {
        try {
            int row = 0;
            if (kosten == null && enddatum == null) {
                row = jdbcTemplate.update("INSERT INTO Event(Titel, Beschreibung, Startdatum, Enddatum, Kosten, WohnortID, BuergerEMailAdresse) VALUES (?,?,?,?,?,?,?)",
                        titel, beschreibung, startdatum, null, 0, wohnortid, user.getUniqueString());
            } else if (kosten != null && enddatum == null) {
                row = jdbcTemplate.update("INSERT INTO Event(Titel, Beschreibung, Startdatum, Enddatum, Kosten, WohnortID, BuergerEMailAdresse) VALUES (?,?,?,?,?,?,?)",
                        titel, beschreibung, startdatum, null, kosten, wohnortid, user.getUniqueString());
            } else if (kosten == null && enddatum != null) {
                row = jdbcTemplate.update("INSERT INTO Event(Titel, Beschreibung, Startdatum, Enddatum, Kosten, WohnortID, BuergerEMailAdresse) VALUES (?,?,?,?,?,?,?)",
                        titel, beschreibung, startdatum, enddatum, 0, wohnortid, user.getUniqueString());
            } else if (kosten != null && enddatum != null) {
                row = jdbcTemplate.update("INSERT INTO Event(Titel, Beschreibung, Startdatum, Enddatum, Kosten, WohnortID, BuergerEMailAdresse) VALUES (?,?,?,?,?,?,?)",
                        titel, beschreibung, startdatum, enddatum, kosten, wohnortid, user.getUniqueString());
            }

            if (row != 0) {
                Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(Event.ID) FROM Event", Integer.class);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, "/events/" + maxRow);
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            }
        } catch (NoSuchElementException e){
            return ResponseEntity.status(404).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 404));
        } catch (DataAccessException e){
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 500));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/events/{eventid}/bewertungen")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> newEvent(@CurrentUser User user, @PathVariable(name="eventid") Integer eventid,
                                      @RequestParam(name="punktzahl", required = true) Integer punktzahl) {
        try {
            int row = jdbcTemplate.update("INSERT INTO BuergerBewertetEvent(BuergerEMailAdresse, EventID, Skala) VALUES (?,?,?)",
                    user.getUniqueString(), eventid,punktzahl);
            if (row != 0) {
                Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(rowid) FROM BuergerBewertetEvent", Integer.class);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, "/events/" + eventid + "/bewertungen/" + maxRow);
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            }
        } catch (NoSuchElementException e){
            return ResponseEntity.status(404).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 404));
        } catch (DataAccessException e){
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 500));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error beim einfügen");
    }

    @PostMapping(value = "/anzeigen")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> newAnz(@CurrentUser User user, @RequestParam(name="titel", required = true) String titel,
                                      @RequestParam(name="beschreibung", required = true) String beschreibung) {
        try {
            int row = jdbcTemplate.update("INSERT INTO Anzeige(Titel, Beschreibung, BuergerEMailAdresse) VALUES (?,?,?)",
                    titel, beschreibung, user.getUniqueString());

            if (row != 0) {
                Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(Anzeige.ID) FROM Anzeige", Integer.class);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, "/anzeigen/" + maxRow);
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            }
        } catch (NoSuchElementException e){
            return ResponseEntity.status(404).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 404));
        } catch (DataAccessException e){
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 500));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error beim einfügen");
    }

    @PostMapping(value = "/anzeigen/{anzeigeid}/nachrichten")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> newNachricht(@CurrentUser User user, @PathVariable(name="anzeigeid") Integer anzeigeid,
                                      @RequestParam(name="text", required = true) String text) {

        try {
            int row = jdbcTemplate.update("INSERT INTO Nachricht(Text1, BuergerEMailAdresse, AnzeigeID) VALUES (?,?,?)",
                    text, user.getUniqueString(), anzeigeid);
            if (row != 0) {
                Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(rowid) FROM Nachricht", Integer.class);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.LOCATION, "/anzeigen/" + anzeigeid + "/nachrichten/" + maxRow);
                return new ResponseEntity<>(headers, HttpStatus.CREATED);
            }
        } catch (NoSuchElementException e){
            return ResponseEntity.status(404).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 404));
        } catch (DataAccessException e){
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 500));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error beim einfügen");
    }

    @PatchMapping("/anzeigen/{anzeigeid}")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> changeAnz(@CurrentUser User user, @PathVariable(name = "anzeigeid") Integer anzeigeid,
                                       @RequestParam(name = "titel", required = false) String titel,
                                       @RequestParam(name = "beschreibung", required = false) String beschreibung) {

        try {
            String email = appservice.findErsteller(anzeigeid);
            int row = 0;
            if (email.equals(user.getUniqueString())) {
                if (jdbcTemplate.queryForObject("SELECT COUNT(rowid) FROM Anzeige", Integer.class) < anzeigeid) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                }
                if (titel != null && beschreibung == null) {
                    row = jdbcTemplate.update("UPDATE Anzeige SET Titel = ? WHERE ID = ?", titel, anzeigeid);
                } else if (titel == null && beschreibung != null) {
                    row = jdbcTemplate.update("UPDATE Anzeige SET Beschreibung = ? WHERE ID = ?", beschreibung, anzeigeid);
                } else if (titel != null && beschreibung != null) {
                    row = jdbcTemplate.update("UPDATE Anzeige SET Beschreibung = ?, Titel = ? WHERE ID = ?", beschreibung, titel, anzeigeid);
                } else if (titel == null && beschreibung == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mind. ein Param");
                }
            }
            if (row > 0) {
                return ResponseEntity.status(204).build();
            }
        } catch (NoSuchElementException e){
            return ResponseEntity.status(404).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 404));
        } catch (DataAccessException e){
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        } catch (Exception e){
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/anzeigen/{anzeigeid}")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> deleteAnz(@CurrentUser User user, @PathVariable(name = "anzeigeid") Integer anzeigeid) {
        try {
            String email = appservice.findErsteller(anzeigeid);
            int row = 0;
            if (email.equals(user.getUniqueString())) {
                row = jdbcTemplate.update("DELETE FROM Anzeige WHERE ID = ?", anzeigeid);
            }
            if (row > 0) {
                return ResponseEntity.status(204).build();
            }
        } catch (NoSuchElementException e){
            return ResponseEntity.status(404).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 404));
        } catch (DataAccessException e) {
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 500));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/anzeigen/{anzeigeid}/nachrichten/{nachrichtid}")
    @RolesAllowed("BUERGER")
    public ResponseEntity<?> deleteNachricht(@CurrentUser User user, @PathVariable(name = "anzeigeid") Integer anzeigeid,
                                             @PathVariable(name = "nachrichtid") Integer nachrichtid) {
        try {
            String email = appservice.findAutor(nachrichtid);
            int row = 0;
            if (email.equals(user.getUniqueString())) {
                row = jdbcTemplate.update("DELETE FROM Nachricht WHERE ID = ? AND AnzeigeID = ?", nachrichtid, anzeigeid);
            }
            if (row > 0) {
                return ResponseEntity.status(204).build();
            }
        } catch (NoSuchElementException e){
            return ResponseEntity.status(404).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 404));
        } catch (DataAccessException e) {
            return ResponseEntity.status(400).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 400));
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ErrorControllerAdvice.ExceptionResponse(e.getLocalizedMessage(), 500));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
