select ID, Titel, Beschreibung, Kosten, Startdatum, Enddatum, e.BuergerEMailAdresse, WohnortID from Event e
join gewerblicherAnbieter g on e.BuergerEMailAdresse = g.BuergerEMailAdresse
WHERE julianday(e.Enddatum) - julianday(e.Startdatum) > 2;


SELECT DISTINCT b1.*
FROM Buerger b1
JOIN Buerger b2 ON b1.BerufID = b2.BerufID
JOIN wohnt_mit wm ON (b1.EMailAdresse = wm.BuergerEmailAdresse1 AND b2.EMailAdresse = wm.BuergerEmailAdresse2)
                   OR (b1.EMailAdresse = wm.BuergerEmailAdresse2 AND b2.EMailAdresse = wm.BuergerEmailAdresse1);

SELECT * FROM gewerblicherAnbieter
where gewerblicherAnbieter.Gruendungsjahr > 1997;