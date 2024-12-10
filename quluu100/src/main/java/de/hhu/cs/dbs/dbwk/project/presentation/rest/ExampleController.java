package de.hhu.cs.dbs.dbwk.project.presentation.rest;

import de.hhu.cs.dbs.dbwk.project.model.*;
import de.hhu.cs.dbs.dbwk.project.security.CurrentUser;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequestMapping("/")
@PermitAll
@RestController
public class ExampleController {

    private final JdbcTemplate jdbcTemplate;
    private Appservice appservice;

    public ExampleController(JdbcTemplate jdbcTemplate, Appservice appservice) {
        this.jdbcTemplate = jdbcTemplate;
        this.appservice = appservice;
    }

    @GetMapping("/buerger")
    public ResponseEntity<List<BuergerDTO>> getAllBuerger(@RequestParam(name = "plz", required = false) String plz){
        String query = "SELECT Buerger.rowid as bid, Buerger.*, Wohnort.rowid as wid, Wohnort.*, Beruf.Name as bname FROM Buerger " +
                "JOIN Beruf ON BerufID = Beruf.ID " +
                "JOIN Wohnort ON WohnortID = Wohnort.ID";
        List<BuergerDTO> buerger = jdbcTemplate.query(query,
                (rs, rowNum) -> new BuergerDTO(
                        rs.getInt("bid"),
                        rs.getInt("wid"),
                        rs.getString("bname"),
                        rs.getString("EMailAdresse"),
                        rs.getString("Password"),
                        rs.getString("Vorname"),
                        rs.getString("Nachname")
                )).stream().toList();
        if (plz != null) {
            String query1 = query + " WHERE lower(plz) = lower(?)";
            buerger = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new BuergerDTO(
                            rs.getInt("bid"),
                            rs.getInt("wid"),
                            rs.getString("bname"),
                            rs.getString("EMailAdresse"),
                            rs.getString("Password"),
                            rs.getString("Vorname"),
                            rs.getString("Nachname")
                    ), plz).stream().toList();
        }
        return new ResponseEntity<List<BuergerDTO>>(buerger, HttpStatus.OK);
    }

    @GetMapping("/gewerbliche_anbieter")
    public ResponseEntity<List<AnbieterDTO>> getAnbieter(@RequestParam(name = "email", required = false) String email){
        String query = "SELECT Buerger.rowid as bid, Buerger.*, gewerblicherAnbieter.rowid AS gaid, Beruf.Name as bname, gewerblicherAnbieter.* FROM gewerblicherAnbieter " +
                    "JOIN Buerger ON Buerger.EMailAdresse = gewerblicherAnbieter.BuergerEMailAdresse " +
                    "JOIN Beruf ON BerufID = Beruf.ID";
        List<AnbieterDTO> anbieter = jdbcTemplate.query(query,
                    (rs, rowNum) -> new AnbieterDTO(
                            rs.getInt("gaid"),
                            rs.getInt("bid"),
                            rs.getString("bname"),
                            rs.getInt("Gruendungsjahr"),
                            rs.getString("EMailAdresse"),
                            rs.getString("Password"),
                            rs.getString("Vorname"),
                            rs.getString("Nachname")
                    )).stream().toList();
        if (email != null) {
            String query1 = query + " WHERE lower(BuergerEMailAdresse) = lower(?)";
            anbieter = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new AnbieterDTO(
                            rs.getInt("gaid"),
                            rs.getInt("bid"),
                            rs.getString("bname"),
                            rs.getInt("Gruendungsjahr"),
                            rs.getString("EMailAdresse"),
                            rs.getString("Password"),
                            rs.getString("Vorname"),
                            rs.getString("Nachname")
                    ), email).stream().toList();
        }
        return new ResponseEntity<List<AnbieterDTO>>(anbieter, HttpStatus.OK);
    }

    @GetMapping("/wohnorte")
    public ResponseEntity<List<WohnortDTO>> getWohnorte(@RequestParam(name = "strasse", required = false) String strasse){
        String query = "SELECT * FROM Wohnort";
        List<WohnortDTO> wohnort = jdbcTemplate.query(query,
                    (rs, rowNum) -> new WohnortDTO(
                            rs.getInt("ID"),
                            rs.getString("Strasse"),
                            rs.getString("Hausnummer"),
                            rs.getString("PLZ"),
                            rs.getString("Stadt")
                    )).stream().toList();
        if (strasse != null) {
            String query1 = query + " WHERE lower(strasse) = lower(?)";
            wohnort = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new WohnortDTO(
                            rs.getInt("ID"),
                            rs.getString("Strasse"),
                            rs.getString("Hausnummer"),
                            rs.getString("PLZ"),
                            rs.getString("Stadt")
                    ), strasse).stream().toList();
        }
        return new ResponseEntity<List<WohnortDTO>>(wohnort, HttpStatus.OK);
    }
    @GetMapping("/spezialisierungen")
    public ResponseEntity<List<SpezDTO>> getSpez(@RequestParam(name = "bezeichnung", required = false) String bezeichnung){
        String query = "SELECT * FROM Spezialisierung";
        List<SpezDTO> spez = jdbcTemplate.query(query,
                    (rs, rowNum) -> new SpezDTO(
                            rs.getInt("ID"),
                            rs.getString("Bezeichnung")
                    )).stream().toList();
        if (bezeichnung != null) {
            String query1 = query + " WHERE lower(bezeichnung) = lower(?)";
            spez = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new SpezDTO(
                            rs.getInt("ID"),
                            rs.getString("Bezeichnung")
                    ), bezeichnung).stream().toList();
        }
        return new ResponseEntity<List<SpezDTO>>(spez, HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getEvent(@RequestParam(name = "titel", required = false) String titel,
                                                  @RequestParam(name = "mehrtaegig", required = false) Boolean mehrtaegig){
        String query = "SELECT * FROM Event";
        List<EventDTO> events = jdbcTemplate.query(query,
                (rs, rowNum) -> new EventDTO(
                        rs.getInt("ID"),
                        rs.getInt("WohnortID"),
                        rs.getString("Titel"),
                        rs.getString("Beschreibung"),
                        rs.getString("Startdatum"),
                        rs.getString("Enddatum"),
                        rs.getDouble("Kosten")
                )).stream().toList();

        if (titel != null && mehrtaegig != null) {
            if (mehrtaegig == true) {
                query += " WHERE lower(Titel) like lower(?) AND Enddatum NOT LIKE Startdatum";
                events = jdbcTemplate.query(query,
                        (rs, rowNum) -> new EventDTO(
                                rs.getInt("ID"),
                                rs.getInt("WohnortID"),
                                rs.getString("Titel"),
                                rs.getString("Beschreibung"),
                                rs.getString("Startdatum"),
                                rs.getString("Enddatum"),
                                rs.getDouble("Kosten")
                        ), titel).stream().toList();
            } else if (mehrtaegig == false) {
                query += " WHERE lower(Titel) like lower(?) AND Enddatum LIKE Startdatum";
                events = jdbcTemplate.query(query,
                        (rs, rowNum) -> new EventDTO(
                                rs.getInt("ID"),
                                rs.getInt("WohnortID"),
                                rs.getString("Titel"),
                                rs.getString("Beschreibung"),
                                rs.getString("Startdatum"),
                                rs.getString("Enddatum"),
                                rs.getDouble("Kosten")
                        ), titel).stream().toList();
            }
        } else if (titel != null) {
            String query1 = query + " WHERE lower(Titel) like lower(?)";
            events = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new EventDTO(
                            rs.getInt("ID"),
                            rs.getInt("WohnortID"),
                            rs.getString("Titel"),
                            rs.getString("Beschreibung"),
                            rs.getString("Startdatum"),
                            rs.getString("Enddatum"),
                            rs.getDouble("Kosten")
                    ), titel).stream().toList();
        } else if (mehrtaegig != null) {
            if (mehrtaegig == true) {
                String query1 = query + " WHERE Enddatum NOT LIKE Startdatum";
                events = jdbcTemplate.query(query1,
                        (rs, rowNum) -> new EventDTO(
                                rs.getInt("ID"),
                                rs.getInt("WohnortID"),
                                rs.getString("Titel"),
                                rs.getString("Beschreibung"),
                                rs.getString("Startdatum"),
                                rs.getString("Enddatum"),
                                rs.getDouble("Kosten")
                        )).stream().toList();
            } else if (mehrtaegig == false) {
                String query1 = query + " WHERE Enddatum LIKE Startdatum";
                events = jdbcTemplate.query(query1,
                        (rs, rowNum) -> new EventDTO(
                                rs.getInt("ID"),
                                rs.getInt("WohnortID"),
                                rs.getString("Titel"),
                                rs.getString("Beschreibung"),
                                rs.getString("Startdatum"),
                                rs.getString("Enddatum"),
                                rs.getDouble("Kosten")
                        )).stream().toList();
            }
        }
        return new ResponseEntity<List<EventDTO>>(events, HttpStatus.OK);
    }

    @GetMapping("/gruppen")
    public ResponseEntity<List<GruppeDTO>> getGruppen(@RequestParam(name = "bezeichnung", required = false) String bezeichnung){
        String query = "SELECT * FROM Gruppe";
        List<GruppeDTO> gruppe = jdbcTemplate.query(query,
                    (rs, rowNum) -> new GruppeDTO(
                            rs.getInt("ID"),
                            rs.getString("Gruppenbezeichnung"),
                            rs.getString("Sprache"),
                            rs.getBoolean("Oeffentlichkeit")
                    )).stream().toList();
        if (bezeichnung != null) {
            String query1 = query + " WHERE lower(Gruppenbezeichnung) = lower(?)";
            gruppe = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new GruppeDTO(
                            rs.getInt("ID"),
                            rs.getString("Gruppenbezeichnung"),
                            rs.getString("Sprache"),
                            rs.getBoolean("Oeffentlichkeit")
                    ), bezeichnung).stream().toList();
        }
        return new ResponseEntity<List<GruppeDTO>>(gruppe, HttpStatus.OK);
    }

    @GetMapping("/anzeigen")
    public ResponseEntity<List<AnzeigeDTO>> getAnzeigen(@RequestParam(name = "nachrichten_anzahl", required = false) Integer nachrichten_anzahl){
        String query = "SELECT COUNT(Nachricht.AnzeigeID) as nachrichten_anzahl, Anzeige.ID as aid, Anzeige.*, Buerger.rowid as bid from Anzeige " +
                "JOIN Buerger on Buerger.EMailAdresse = Anzeige.BuergerEMailAdresse " +
                "LEFT JOIN Nachricht on Nachricht.AnzeigeID = Anzeige.ID " +
                "GROUP By aid";
        List<AnzeigeDTO> anzeigen = jdbcTemplate.query(query,
                (rs, rowNum) -> new AnzeigeDTO(
                        rs.getInt("ID"),
                        rs.getInt("bid"),
                        rs.getInt("nachrichten_anzahl"),
                        rs.getString("Titel"),
                        rs.getString("Beschreibung")
                )).stream().toList();
        if (nachrichten_anzahl != null) {
            String query1 = query + " HAVING nachrichten_anzahl >= ?";
            anzeigen = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new AnzeigeDTO(
                            rs.getInt("aid"),
                            rs.getInt("bid"),
                            rs.getInt("nachrichten_anzahl"),
                            rs.getString("Titel"),
                            rs.getString("Beschreibung")
                    ), nachrichten_anzahl).stream().toList();
        }
        return new ResponseEntity<List<AnzeigeDTO>>(anzeigen, HttpStatus.OK);
    }

    @GetMapping("/events/bewertungen")
    public ResponseEntity<List<BewertungDTO>> getBewertung(@RequestParam(name = "min_grenze", required = false) Integer min_grenze){
        String query = "SELECT AVG(skala) as durch, Event.* From Event " +
                "JOIN BuergerBewertetEvent On Event.ID = BuergerBewertetEvent.EventID " +
                "Group BY Event.ID";
        List<BewertungDTO> bew = jdbcTemplate.query(query,
                (rs, rowNum) -> new BewertungDTO(
                        rs.getString("Titel"),
                        rs.getString("Beschreibung"),
                        rs.getString("Startdatum"),
                        rs.getString("Enddatum"),
                        rs.getInt("Kosten"),
                        rs.getInt("ID"),
                        rs.getDouble("durch")
                )).stream().toList();
        if (min_grenze != null) {
            String query1 = query + " HAVING durch >= ?";
            bew = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new BewertungDTO(
                            rs.getString("Titel"),
                            rs.getString("Beschreibung"),
                            rs.getString("Startdatum"),
                            rs.getString("Enddatum"),
                            rs.getInt("Kosten"),
                            rs.getInt("ID"),
                            rs.getDouble("durch")
                    ), min_grenze).stream().toList();
        }
        return new ResponseEntity<List<BewertungDTO>>(bew, HttpStatus.OK);
    }

    @GetMapping("/anzeigen/nachrichten")
    public ResponseEntity<List<NachrichtDTO>> getNachrichten(@RequestParam(name = "anzeigeid", required = false) Integer anzeigeid){
        String query = "SELECT Anzeige.ID as anzeigeid, Buerger.rowid as bid, Nachricht.* from Nachricht " +
                "JOIN Buerger on Buerger.EMailAdresse = Nachricht.BuergerEMailAdresse " +
                "JOIN Anzeige on Nachricht.AnzeigeID = Anzeige.ID ";
        List<NachrichtDTO> nachrichten = jdbcTemplate.query(query,
                (rs, rowNum) -> new NachrichtDTO(
                        rs.getInt("anzeigeid"),
                        rs.getInt("bid"),
                        rs.getString("Text1")
                )).stream().toList();
        if (anzeigeid != null) {
            String query1 = query + " WHERE anzeigeid = ?";
            nachrichten = jdbcTemplate.query(query1,
                    (rs, rowNum) -> new NachrichtDTO(
                            rs.getInt("anzeigeid"),
                            rs.getInt("bid"),
                            rs.getString("Text1")
                    ), anzeigeid).stream().toList();
        }
        return new ResponseEntity<List<NachrichtDTO>>(nachrichten, HttpStatus.OK);
    }

    @PostMapping(value = "/buerger")
    public ResponseEntity<String> createbuerger(@RequestParam("email") String email,
                                             @RequestParam("passwort") String passwort,
                                             @RequestParam("vorname") String vorname,
                                             @RequestParam("nachname") String nachname,
                                             @RequestParam("wohnortid") Integer wohnortid,
                                             @RequestParam("berufbezeichnung") String beruf) {
        //check email

        if (!appservice.emailValidation(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format.");
        }

        Integer berufId = appservice.findBerufid(beruf);
        if (berufId == null) {
            // Insert new profession if it doesn't exist
            int addBeruf = jdbcTemplate.update("INSERT INTO Beruf(Name) VALUES (?)", beruf);
            if (addBeruf == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add profession."); // hay la 500
            }
            berufId = appservice.findBerufid(beruf);
        }

        int row = jdbcTemplate.update(
                "INSERT INTO Buerger(EMailAdresse, Password, Vorname, Nachname, WohnortID, BerufID) VALUES (?, ?, ?, ?, ?, ?)",
                email, passwort, vorname, nachname, wohnortid, berufId);

        if (row != 0) {
            Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(Buerger.EMailAdresse) FROM Buerger", Integer.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, "/buerger/" + maxRow);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error beim einfügen");
    }
    @PostMapping(value = "/gewerbliche_anbieter") //, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json"
    public ResponseEntity<String> createGA(@RequestParam("email") String email,
                                            @RequestParam("passwort") String passwort,
                                            @RequestParam("vorname") String vorname,
                                            @RequestParam("nachname") String nachname,
                                            @RequestParam("wohnortid") Integer wohnortid,
                                            @RequestParam("berufbezeichnung") String beruf,
                                            @RequestParam("gruendungsjahr") String gruendungsjahr) {
        //check email

        if (!appservice.emailValidation(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format.");
        }

        Integer buergerid = appservice.findBuergerid(email);
        if (buergerid == null) {
            // Insert new Buerger if it doesn't exist
            Integer berufId = appservice.findBerufid(beruf);
            if (berufId == null) {
                // Insert new profession if it doesn't exist
                int addBeruf = jdbcTemplate.update("INSERT INTO Beruf(Name) VALUES (?)", beruf);
                if (addBeruf == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add profession."); //hay la 500
                }
                berufId = appservice.findBerufid(beruf);
            }
            int addBuerger = jdbcTemplate.update(
                    "INSERT INTO Buerger(EMailAdresse, Password, Vorname, Nachname, WohnortID, BerufID) VALUES (?, ?, ?, ?, ?, ?)",
                    email, passwort, vorname, nachname, wohnortid, berufId);
            if (addBuerger == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add profession.");
            }
        }
        int row = jdbcTemplate.update(
                "INSERT INTO gewerblicherAnbieter(BuergerEMailAdresse, Gruendungsjahr) VALUES (?, ?)",
                email, gruendungsjahr);

        if (row != 0) {
            Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(gewerblicherAnbieter.BuergerEMailAdresse) FROM gewerblicherAnbieter", Integer.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, "/gewerbliche_anbieter/" + maxRow);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error beim einfügen");
    }

    @PostMapping(value = "/wohnorte")
    public ResponseEntity<String> newAd(@RequestParam(name="strasse", required = true) String strasse,
                                        @RequestParam(name="hausnummer", required = true) String hausnummer,
                                        @RequestParam(name="plz", required = true) String plz,
                                        @RequestParam(name="stadt", required = true) String stadt) {
        int row = jdbcTemplate.update("INSERT INTO Wohnort(Hausnummer, Strasse, PLZ, Stadt) VALUES (?,?,?,?)", hausnummer, strasse, plz, stadt);

        if (row != 0) {
            Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(Wohnort.ID) FROM Wohnort", Integer.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, "/wohnorte/" + maxRow);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error beim einfügen");
    }
    @PostMapping(value = "/spezialisierungen")
    public ResponseEntity<String> newSpe(@RequestParam(name="bezeichnung", required = true) String bezeichnung) {
        int row = jdbcTemplate.update("INSERT INTO Spezialisierung(Bezeichnung) VALUES (?)", bezeichnung);

        if (row != 0) {
            Integer maxRow = jdbcTemplate.queryForObject("SELECT COUNT(Spezialisierung.ID) FROM Spezialisierung", Integer.class);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, "/spezialisierungen/" + maxRow);
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error beim einfügen");
    }



    @GetMapping
    // GET http://localhost:8080
    public String halloWelt() {
        return "\"Hallo Welt!\"";
    }

    @GetMapping("/exception")
    // GET http://localhost:8080
    public String halloException() {
        throw new RuntimeException("Hallo Exception!");
    }


    @GetMapping("foo3/{foo}")
    // GET http://localhost:8080/foo3/bar
    public String halloFoo3(@PathVariable("foo") String foo) {
        if (!foo.equals("bar")) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return "\"Hallo " + foo + "\"!";
    }

    @GetMapping("foo4")
    // GET http://localhost:8080/foo4?bar=xyz
    public String halloFoo4(@RequestParam("bar") String bar) {
        return "\"Hallo " + bar + "\"!";
    }

    @GetMapping("bar")
    // GET http://localhost:8080/bar => BAD REQUEST; http://localhost/bar?foo=xyz => OK
    public ResponseEntity<?> halloBar(@RequestParam(name = "foo", required = false) String foo) {
        if (foo == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "foo must not be foo.");
        return ResponseEntity.status(HttpStatus.OK).body("Hallo " + foo + "!");
    }

    @GetMapping("bar2")
    // GET http://localhost:8080/bar2
    public String halloBar2(
            @RequestParam(name = "name", defaultValue = "Max Mustermann") List<String> names) {
        int random = ThreadLocalRandom.current().nextInt(0, names.size());
        return jdbcTemplate.queryForObject("SELECT ?", String.class, names.get(random));
    }

    @PostMapping("foo")
    // POST http://localhost:8080/foo
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file, UriComponentsBuilder uriComponentsBuilder) {
        try {
            String length = String.valueOf(file.getBytes().length);
            return ResponseEntity.created(uriComponentsBuilder.path("/{id}").build(length)).build();
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null, exception);
        }
    }
}
