CREATE TABLE Wohnort(
    ID INTEGER NOT NULL, 
    Hausnummer VARCHAR(15) NOT NULL CHECK (NOT glob('*[^ -~]*', Hausnummer) and length(trim(Hausnummer))>0),
    Strasse VARCHAR(255) NOT NULL CHECK (NOT glob('*[^ -~]*', Strasse) and length(trim(Strasse))>0),
    PLZ VARCHAR(15) NOT NULL CHECK (NOT glob('*[^ -~]*', PLZ) and length(trim(PLZ))>0),
  	Stadt VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', Stadt) and length(trim(Stadt))>0),
    PRIMARY KEY (ID)
);

CREATE TABLE Beruf(
    ID INTEGER NOT NULL,
    Name VARCHAR(100) COLLATE NOCASE NOT NULL UNIQUE CHECK (Name NOT GLOB '*[^A-Za-z]*'),
    PRIMARY KEY (ID)
);


CREATE TABLE Buerger(
    EMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
    Vorname VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', Vorname) and length(trim(Vorname))>0),
    Nachname VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', Nachname) and length(trim(Nachname))>0),
    Password VARCHAR(8) NOT NULL CHECK(
      	LENGTH(Password) BETWEEN 4 AND 8 AND
      	NOT glob('*[^ -~]*', Password) AND
      	Password GLOB '*[0-9]*[0-9]*' and
        Password GLOB '*[A-Z]*[A-Z]*' AND
      	NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][a-zA-Z]*', Password) And
      	NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][^a-zA-Z0-9]*', Password) And
      	NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][0-9][a-zA-Z]*', Password) AND
      	NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][0-9][^a-zA-Z0-9]*', Password) And
		NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][0-9][0-9][0-9]*', Password) AND
      	NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][0-9][0-9][0-9][0-9]*', Password) AND
      	NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][0-9][0-9][0-9][0-9][0-9]*', Password) AND
      	NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][0-9][0-9][0-9][0-9][0-9][0-9]*', Password) AND
      	NOT GLOB('*[qwrtzpsdfghjklyxcvbnmQWRTZPSDFGHJKLYXCVBNM][0-9][0-9][0-9][0-9][0-9][0-9][0-9]*', Password)),
	WohnortID INTEGER NOT NULL,
  	BerufID INTEGER NOT NULL,
    PRIMARY KEY (EMailAdresse),
    FOREIGN KEY (WohnortID) REFERENCES Wohnort(ID) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (BerufID) REFERENCES Beruf(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE wohnt_mit(
    BuergerEmailAdresse1 VARCHAR(255) COLLATE NOCASE NOT NULL,
	BuergerEmailAdresse2 VARCHAR(255) COLLATE NOCASE NOT NULL,
    PRIMARY KEY (BuergerEmailAdresse1, BuergerEmailAdresse2),
    FOREIGN KEY (BuergerEmailAdresse1) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (BuergerEmailAdresse2) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE Gruppe(
    ID INTEGER NOT NULL,
    Gruppenbezeichnung VARCHAR(255) NOT NULL CHECK (Gruppenbezeichnung NOT GLOB '*[^A-Za-z]*'),
	Sprache VARCHAR(100) NOT NULL CHECK (NOT glob('*[^ -~]*', Sprache) and length(trim(Sprache))>0),
  	Oeffentlichkeit BOOLEAN not NULL,
  	BuergerEmailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
  	PRIMARY KEY (ID),
  	FOREIGN KEY (BuergerEmailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE BuergerPartizipiertGruppe(
    BuergerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
    GruppeID INTEGER NOT NULL,
    PRIMARY KEY (BuergerEMailAdresse, GruppeID),
    FOREIGN KEY (BuergerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (GruppeID) REFERENCES Gruppe(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TRIGGER Check_Gruppe
BEFORE INSERT ON BuergerPartizipiertGruppe
BEGIN
	select
	CASE WHEN (
    	SELECT COUNT(*) FROM BuergerPartizipiertGruppe bg
      	JOIN Gruppe g ON bg.GruppeID = g.ID
      	WHERE g.Oeffentlichkeit = false
   		AND bg.BuergerEMailAdresse = NEW.BuergerEMailAdresse
	) >= 2
    then RAISE(ABORT, 'Ein Bürger kann höchstens Mitglied von zwei privaten Gruppen sein.') END;
END;

CREATE TABLE Faehigkeit(
    ID INTEGER NOT NULL,
    Bezeichnung VARCHAR(100) COLLATE NOCASE UNIQUE NOT NULL CHECK (Bezeichnung NOT GLOB '*[^A-Za-z]*'),
    PRIMARY KEY (ID)
);

CREATE TABLE BuergerBesitztFaehikeit(
    BuergerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
    FaehigkeitID INTEGER NOT NULL,
    PRIMARY KEY (BuergerEMailAdresse, FaehigkeitID),
    FOREIGN KEY (BuergerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (FaehigkeitID) REFERENCES Faehigkeit(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE gewerblicherAnbieter(
    BuergerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
	Gruendungsjahr INTEGER NOT NULL CHECK (Gruendungsjahr > 0),
    PRIMARY KEY (BuergerEMailAdresse),
    FOREIGN KEY (BuergerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE Spezialisierung(
  	ID INTEGER NOT NULL,
    Bezeichnung VARCHAR(100) COLLATE NOCASE UNIQUE NOT NULL CHECK (Bezeichnung NOT GLOB '*[^A-Za-z]*'),
    PRIMARY KEY (ID)
);

CREATE TABLE GewAnbieterHatSpezialisierung(
    GAEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
  	SpezialisierungID INTEGER NOT NULL,
  	PRIMARY KEY (GAEMailAdresse, SpezialisierungID)
    FOREIGN KEY (GAEMailAdresse) REFERENCES gewerblicherAnbieter(BuergerEMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (SpezialisierungID) REFERENCES Spezialisierung(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Event(
  	ID INTEGER NOT NULL,
	Titel VARCHAR(255) NOT NULL CHECK (NOT glob('*[^ -~]*', Titel) and length(trim(Titel))>0),
  	Beschreibung TEXT NOT NULL  CHECK (NOT glob('*[^ -~]*', Beschreibung) and length(trim(Beschreibung))>0),
  	Kosten DOUBLE NOT NULL check (Kosten >= 0 AND ROUND(Kosten, 2) == Kosten),
  	Startdatum DATE NOT NULL CHECK (GLOB('[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]', Startdatum) AND strftime('%Y-%m-%d', Startdatum) = Startdatum),
	Enddatum DATE CHECK (GLOB('[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]', Enddatum) AND strftime('%Y-%m-%d', Enddatum) = Enddatum),
  	BuergerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
	WohnortID INTEGER NOT NULL,
  	PRIMARY KEY (ID),
    FOREIGN KEY (BuergerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (WohnortID) REFERENCES Wohnort(ID) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE BuergerNimmt_teilEvent(
    BuergerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
    EventID INTEGER NOT NULL,
    PRIMARY KEY (BuergerEMailAdresse, EventID),
    FOREIGN KEY (BuergerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (EventID) REFERENCES Event(ID) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE BuergerBewertetEvent(
    BuergerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
    EventID INTEGER NOT NULL,
    Skala INTEGER CHECK (Skala >= 1 AND Skala <= 5),
    PRIMARY KEY (BuergerEMailAdresse, EventID),
    FOREIGN KEY (BuergerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (EventID) REFERENCES Event(ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TRIGGER check_bewertung
BEFORE INSERT ON BuergerBewertetEvent
BEGIN
    SELECT
    case when NEW.BuergerEMailAdresse not in (
        SELECT BuergerEMailAdresse
        FROM BuergerNimmt_teilEvent
        WHERE EventID = NEW.EventID)
    then RAISE(ABORT, 'Ein Bürger kann nur Events bewerten, an denen er auch teilgenommen hat.') END;
END;


CREATE TABLE Anzeige(
	ID INTEGER NOT NULL,
	Titel VARCHAR(255) CHECK (NOT glob('*[^ -~]*', Titel) and length(trim(Titel))>0),
  	Beschreibung TEXT CHECK (NOT glob('*[^ -~]*', Beschreibung) and length(trim(Beschreibung))>0),
  	BuergerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (BuergerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Nachricht(
  	ID INTEGER NOT NULL,
  	Text1 TEXT NOT NULL CHECK (NOT glob('*[^ -~]*', Text1) and length(trim(Text1))>0),
  	BuergerEMailAdresse VARCHAR(255) COLLATE NOCASE NOT NULL,
    AnzeigeID INTEGER NOT NULL,
    PRIMARY KEY (ID),
    FOREIGN KEY (BuergerEMailAdresse) REFERENCES Buerger(EMailAdresse) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (AnzeigeID) REFERENCES Anzeige(ID)
);

CREATE TRIGGER delete_anzeige
BEFORE DELETE ON Anzeige
FOR EACH ROW
BEGIN
    SELECT 
    CASE WHEN (SELECT COUNT(*) FROM Nachricht WHERE AnzeigeID = OLD.ID) > 0 
    THEN RAISE(ABORT, 'Anzeige mit zugeordneten Nachrichten können nicht gelöscht werden.') END;
END;