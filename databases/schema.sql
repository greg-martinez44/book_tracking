CREATE TABLE IF NOT EXISTS book (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    authorID INTEGER,
    publisherID INTEGER,
    name TEXT,
    pages INTEGER,
    startDate DATE,
    finishDate DATE,
    rating INTEGER,
    FOREIGN KEY(authorID) REFERENCES author(id),
    FOREIGN KEY(publisherID) REFERENCES imprint(id)
);

CREATE TABLE IF NOT EXISTS author (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstName TEXT,
    lastName TEXT
);

CREATE TABLE IF NOT EXISTS publishingHouse (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT
);

CREATE TABLE IF NOT EXISTS imprint (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    mainHouseID INTEGER,
    FOREIGN KEY(mainHouseID) REFERENCES publishingHouse(id)
);
