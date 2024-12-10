INSERT INTO Wohnort VALUES
	(1, '123A', 'Neustr.', '40215', 'Duesseldorf'),
    (2, '10', 'Bahnhofstr.', '10000', 'Dusseldorf'),
    (3, '1', 'Blaugarten', '31213', 'Bochum'),
    (4, '12/14', 'Highland Park', 'K1A 0A1', 'Ottawa');

Insert into Beruf VALUES
	(1, 'freelancer'),
    (2, 'Student'),
    (3, 'Lehrer');

INSERT INTO Buerger VALUES 
    ('abc123@gmail.com', 'John', 'Doe', 'Ab12C1', 1, 1),
    ('aaa999@dhl.de', 'Alice', 'Smith', 'AA999', 2, 1),
    ('bobu10@bbank1.de', 'Bob', 'Johnson', 'S23W12', 3, 2),
    ('2819hanna@ex1.de', 'Hanna', 'Miller', 'H12An', 4, 2),
    ('babyy@example.com', 'Baby', 'Doll', 'B00A00', 1, 1),
    ('maxM@exp.de', 'Max', 'Mustermann', 'm45AAk', 1, 3),
    ('timmy@baby12.de', 'Timmy', 'Jones', 'T89IO', 2, 3);

Insert into wohnt_mit VALUES
	('abc123@gmail.com', 'aaa999@dhl.de'),
    ('aaa999@dhl.de', '2819hanna@ex1.de'),
    ('timmy@baby12.de', 'abc123@gmail.com'),
    ('babyy@example.com', 'abc123@gmail.com'),
    ('bobu10@bbank1.de','abc123@gmail.com'),
    ('bobu10@bbank1.de', 'maxM@exp.de');

INSERT Into Gruppe VALUES
	(1, 'Stadtmitte', 'Englisch', false, 'babyy@example.com'),
	(2, 'Derendorf', 'Deutsch', true, 'bobu10@bbank1.de'),
	(3, 'WohnungSuchen', 'Deutsch', true, 'abc123@gmail.com'),
	(4, 'roteHilfe', 'Englisch', true, 'abc123@gmail.com');
    
insert into BuergerPartizipiertGruppe VALUES
	('bobu10@bbank1.de', 1),
	('bobu10@bbank1.de', 3),
	('timmy@baby12.de', 1),
	('aaa999@dhl.de', 4),
	('abc123@gmail.com', 4),
	('abc123@gmail.com', 3);

insert into Faehigkeit VALUES
	(1, 'GitarreSpielen'),
	(2, 'Tanzen');

insert into BuergerBesitztFaehikeit VALUES
	('bobu10@bbank1.de', 1),
	('bobu10@bbank1.de', 2),
    ('abc123@gmail.com', 2);

insert into gewerblicherAnbieter VALUES
	('bobu10@bbank1.de', 1995),
	('2819hanna@ex1.de', 2006),
    ('abc123@gmail.com', 2015);

INSERT into Spezialisierung values
	(1, 'Gastronomie'),
    (2, 'Handwerk'),
    (3, 'Bildung');

insert into GewAnbieterHatSpezialisierung VALUES
	('bobu10@bbank1.de', 1),
	('2819hanna@ex1.de', 2),
	('abc123@gmail.com', 1);
    
INSERT INTO Event VALUES 
	(1, 'Concert at Midnight', 'Live performance of a famous band', 25.59, '2023-05-10', '2024-05-11', 'aaa999@dhl.de', 4),
	(2, 'Conference', 'Annual tech conference', 100.00, '2023-09-20', '2023-09-25', 'bobu10@bbank1.de', 2),
	(3, 'Workshop', 'Hands-on learning experience', 0, '2024-01-10', NULL , 'abc123@gmail.com', 3);

Insert into BuergerNimmt_teilEvent VALUES
	('abc123@gmail.com', 1),
    ('aaa999@dhl.de', 1),
    ('aaa999@dhl.de', 2),
	('abc123@gmail.com', 3),
    ('2819hanna@ex1.de', 3),
    ('babyy@example.com', 3);

INSERT into BuergerBewertetEvent VALUES
	('abc123@gmail.com', 1, 4),
    ('abc123@gmail.com', 3, 3),
    ('babyy@example.com', 3, 5);

INSERT INTO Anzeige VALUES 
	(1, 'Car for sale', 'Used BMW in good condition', 'timmy@baby12.de'),
	(2, 'Apartment for rent', 'Spacious apartment with a good view', 'maxM@exp.de'),
	(3, 'Job vacancy', 'Software engineer position available', 'babyy@example.com');

INSERT INTO Nachricht VALUES 
    (1, 'Interested in your car for sale. Could you provide more details?', 'abc123@gmail.com', 1),
    (2, 'I would like to schedule a viewing of the apartment.', 'aaa999@dhl.de', 2);