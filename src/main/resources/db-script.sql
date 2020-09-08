CREATE TABLE camel.person (
	id serial PRIMARY KEY,
	name VARCHAR(30) NOT NULL
);

INSERT INTO camel.person(name) VALUES('Denny');

INSERT INTO camel.person(name) VALUES('Jimmy');
INSERT INTO camel.person(name) VALUES('Hima');
INSERT INTO camel.person(name) VALUES('John');
INSERT INTO camel.person(name) VALUES('Seema');

person
SELECT version();


CREATE TABLE test.customer (
	id serial PRIMARY KEY,
	name VARCHAR(30) NOT NULL
);



INSERT INTO test.customer(name) VALUES('Jacob');
INSERT INTO test.customer(name) VALUES('Nita');
INSERT INTO test.customer(name) VALUES('Raveena');
INSERT INTO test.customer(name) VALUES('Deepak');
INSERT INTO test.customer(name) VALUES('James');


CREATE TABLE CAMEL_MESSAGEPROCESSED (
	processorName VARCHAR(255), 
	messageId VARCHAR(100), 
	createdAt TIMESTAMP,
	UNIQUE(processorName, messageId)
);

SELECT * FROM camel.person;

SELECT * FROM test.customer;


SELECT * FROM camel_messageprocessed;
camel_messageprocessed

INSERT INTO test.customer(name) VALUES('Anish');

SELECT 1 FROM CAMEL_MESSAGEPROCESSED WHERE 1 = 0;