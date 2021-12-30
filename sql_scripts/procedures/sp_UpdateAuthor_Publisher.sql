DELIMITER //

CREATE PROCEDURE sp_AddAuthor (
	IN new_author VARCHAR(100),
    IN new_other_authors VARCHAR(500)
)
BEGIN
	INSERT INTO authors (author, other_authors)
	SELECT * FROM (SELECT new_author as author, new_other_authors as other_authors) AS temp
	WHERE NOT EXISTS (SELECT author FROM authors WHERE author = new_author) LIMIT 1;    
END //


DELIMITER //
CREATE PROCEDURE sp_AddImprint (
	IN new_imprint VARCHAR(100),
    IN new_publishing_house VARCHAR(500)
)
BEGIN
	INSERT INTO publishers (imprint, publishing_house)
	SELECT * FROM (SELECT new_imprint as imprint, new_publishing_house as publishing_house) AS temp
	WHERE NOT EXISTS (SELECT imprint FROM publishers WHERE imprint = new_imprint AND publishing_house = new_publishing_house) LIMIT 1;    
END //

DELIMITER //

CREATE PROCEDURE sp_AddNarrator (
	IN new_narrator VARCHAR(500)
)
BEGIN
	INSERT INTO narrators (narrarator)
    SELECT * FROM (SELECT new_narrator AS narrator) AS TEMP
    WHERE NOT EXISTS (SELECT narrator FROM narrators WHERE narrator = new_narrator) LIMIT 1;
END //


DELIMITER //

CREATE PROCEDURE sp_AddTranslator (
	IN new_translator VARCHAR(500)
)
BEGIN
	INSERT INTO translators (translator)
    SELECT * FROM (SELECT new_translator AS translator) AS TEMP
    WHERE NOT EXISTS (SELECT translator FROM translators WHERE translator = new_translator) LIMIT 1;
END //



DELIMITER //

CREATE PROCEDURE sp_AddIllustrator (
	IN new_illustrator VARCHAR(500)
)
BEGIN
	INSERT INTO illustrators (illustrator)
    SELECT * FROM (SELECT new_illustrator AS illustrator) AS TEMP
    WHERE NOT EXISTS (SELECT illustrator FROM illustrators WHERE illustrator = new_illustrator) LIMIT 1;
END //