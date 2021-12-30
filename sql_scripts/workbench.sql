SELECT DISTINCT
	imprint
FROM publishers
WHERE COALESCE(publishing_house, '') = '';

SELECT
	imprint_id,
    imprint,
    publishing_house
FROM publishers
WHERE UPPER(publishing_house) LIKE '%LITTLE%BROWN%COMPANY%'
OR UPPER(imprint) LIKE '%LITTLE%BROWN%COMPANY%';

SELECT
	imprint,
    publishing_house,
    COUNT(imprint)
FROM publishers
GROUP BY imprint, publishing_house
HAVING COUNT(imprint) > 1;

SELECT
imprint_id,
imprint,
publishing_house
FROM publishers
WHERE imprint = 'Kadokawa';


SELECT
	b.title,
    b.imprint_id,
    i.imprint,
    i.publishing_house
FROM books b
INNER JOIN publishers i
ON b.imprint_id = i.imprint_id
WHERE i.imprint IN ('Yen On', 'Yen Press');


SELECT * FROM books where imprint_id IN (241, 242);
SELECT * from publishers where imprint_id IN (241, 242);

START TRANSACTION;
DELETE FROM publishers WHERE imprint_id IN (241, 242);
SELECT * FROM books where imprint_id IN (241, 242);
SELECT * from publishers where imprint_id IN (241, 242);
COMMIT;

START TRANSACTION;
SELECT @kodansha:=imprint_id 
FROM publishers
WHERE imprint = 'Kodansha';
UPDATE publishers 
SET publishing_house = 'Kodansha' 
WHERE imprint_id = @kodansha;
SELECT
	imprint,
    publishing_house
FROM publishers
WHERE imprint = 'Kodansha';
COMMIT;

SELECT
	imprint_id, imprint
FROM publishers
WHERE imprint = 'MacMillan Audio';

START TRANSACTION;
DELETE FROM publishers WHERE imprint_id = 227;
SELECT @old_imprint:=book_id FROM books WHERE imprint_id = 227;
UPDATE books SET imprint_id = 172 WHERE book_id = @old_imprint;
SELECT
	b.title,
    b.imprint_id,
    i.imprint,
    i.publishing_house
FROM books b
INNER JOIN publishers i
ON b.imprint_id = i.imprint_id
WHERE i.imprint_id = 172;
SELECT
imprint_id,
imprint,
publishing_house
FROM publishers
WHERE imprint = 'MacMillan Audio';
COMMIT;


START TRANSACTION;
SELECT
	b.title,
    b.imprint_id,
    i.imprint,
    i.publishing_house
FROM books b
INNER JOIN publishers i
ON b.imprint_id = i.imprint_id
WHERE i.imprint IN ('Yen On', 'Yen Press');
SELECT @yen_press:=imprint_id FROM publishers WHERE imprint = 'Yen Press';
SELECT @yen_on:=imprint_id FROM publishers WHERE imprint = 'Yen On';
UPDATE publishers SET publishing_house = 'Kadokawa' WHERE imprint_id = @yen_press;
DELETE FROM publishers WHERE imprint_id = @yen_on;
DROP TEMPORARY TABLE IF EXISTS temp_book_table;
CREATE TEMPORARY TABLE temp_book_table AS
	SELECT book_id
    FROM books
    WHERE imprint_id = @yen_on;
UPDATE books set imprint_id = @yen_press WHERE book_id IN (select book_id FROM temp_book_table);
SELECT
	b.title,
    b.imprint_id,
    i.imprint,
    i.publishing_house
FROM books b
INNER JOIN publishers i
ON b.imprint_id = i.imprint_id
WHERE i.imprint IN ('Yen On', 'Yen Press');
SELECT
	imprint_id,
    imprint,
    publishing_house
FROM publishers
WHERE imprint IN ('Yen On', 'Yen Press');
DROP TEMPORARY TABLE temp_book_table;
COMMIT;

START TRANSACTION;
SELECT @kadokawa:=imprint_id FROM publishers WHERE imprint = 'Kadokawa';
UPDATE publishers set publishing_house = 'Kadokawa' WHERE imprint_id = @kadokawa;
SELECT
	imprint_id,
    imprint,
    publishing_house
FROM publishers
WHERE imprint_id = @kadokawa;
COMMIT;

START TRANSACTION;
SELECT * FROM publishers WHERE imprint = 'Bond Street Books';
SELECT @bsb:=imprint_id FROM publishers WHERE imprint = 'Bond Street Books';
UPDATE publishers SET publishing_house = 'Penguin Random House' WHERE imprint_id = @bsb;
SELECT * from publishers WHERE publishing_house LIKE '%Penguin Random House%';
COMMIT;

START TRANSACTION;
SELECT @imprint_id:=imprint_id FROM publishers WHERE imprint = 'Samuel French';
UPDATE publishers SET publishing_house = 'Concord' WHERE imprint_id = @imprint_id;
SELECT * FROM publishers WHERE imprint = 'Samuel French';
COMMIT;


START TRANSACTION;
SELECT @imprint_id:=imprint_id FROM publishers WHERE imprint = 'Bold Type Books';
UPDATE publishers SET publishing_house = 'Hachette' WHERE imprint_id = @imprint_id;
SELECT * FROM publishers where publishing_house LIKE '%Hachette%';
COMMIT;

START TRANSACTION;
SELECT @back_bay:=imprint_id FROM publishers WHERE imprint = 'Back Bay Books';
UPDATE publishers SET publishing_house = 'Hachette' WHERE imprint_id = @back_bay;
SELECT * FROM publishers WHERE publishing_house = 'Hachette';
COMMIT;

START TRANSACTION;
CREATE TEMPORARY TABLE IF NOT EXISTS temp_publishers_table AS
	SELECT imprint_id
    FROM publishers
    WHERE imprint IN (
    'Libraries Unlimited',
	'Books on Tape',
	'Mins Publishing',
	'Insight Editions',
	'Adams Media',
	'Bt Bound',
	'Arthur A. Levine Books',
	'MacLehose Press',
	'Ria University Press',
	'Sage',
	'Burford Books',
	'Chivers Audio Books',
	'Nick Hern Books',
	'Public Domain Books',
	'State University of New York Press',
	'University of Massachusetts Press'
    );
UPDATE publishers SET publishing_house = imprint WHERE imprint_id IN (SELECT imprint_id FROM temp_publishers_table);
SELECT * FROM publishers WHERE imprint IN (
'Libraries Unlimited',
	'Books on Tape',
	'Mins Publishing',
	'Insight Editions',
	'Adams Media',
	'Bt Bound',
	'Arthur A. Levine Books',
	'MacLehose Press',
	'Ria University Press',
	'Sage',
	'Burford Books',
	'Chivers Audio Books',
	'Nick Hern Books',
	'Public Domain Books',
	'State University of New York Press',
	'University of Massachusetts Press');
DROP TEMPORARY TABLE temp_publishers_table;
COMMIT;

START TRANSACTION;
SELECT @book_jungle:=imprint_id FROM publishers WHERE imprint = 'Book Jungle';
UPDATE publishers SET publishing_house = 'Future Plc' WHERE imprint_id = @book_jungle;
SELECT * from publishers where imprint = 'Book Jungle';
COMMIT;

START TRANSACTION;
CALL sp_UpdatePublisherTable('Chapman & Hall', 'CRC Press');
COMMIT;

START TRANSACTION;
CALL sp_UpdatePublisherTable('HighBridge Audio', 'Recorded Books');
COMMIT;

START TRANSACTION;
CALL sp_UpdatePublisherTable('Dey Street Books', 'HarperCollins');
COMMIT;


START TRANSACTION;
CALL sp_UpdatePublisherTable('Palgrave MacMillan', 'Macmillan');
COMMIT;

START TRANSACTION;
CALL sp_UpdatePublisherTable('Puffin Books', 'Penguin Random House');
COMMIT;

START TRANSACTION;
CALL sp_UpdatePublisherTable('Candlewick Press', 'Walker Books');
COMMIT;

START TRANSACTION;
SELECT * FROM publishers WHERE imprint = 'TarcherPerigee';
SELECT imprint_id INTO @tp FROM publishers WHERE imprint = 'TarcherPerigee' AND publishing_house = 'Penguin Random House';
SELECT imprint_id INTO @tp_bad FROM publishers WHERE imprint = 'TarcherPerigee' AND COALESCE(publishing_house, '') = '';
CREATE TEMPORARY TABLE IF NOT EXISTS temp_book_table AS
	SELECT
		book_id
	FROM books
	WHERE imprint_id = @tp_bad;
UPDATE books SET imprint_id = @tp WHERE book_id IN (SELECT book_id FROM temp_book_table);
UPDATE publishers set publishing_house = 'Penguin Random House' WHERE imprint_id = @tp;
UPDATE publishers set imprint = 'TarcherPerigee' WHERE imprint_id = @tp;
DELETE FROM publishers WHERE imprint_id = @tp_bad;
SELECT * FROM publishers WHERE imprint = 'TarcherPerigee';
SELECT
	b.title,
    i.imprint_id,
    i.imprint,
    i.publishing_house
FROM books b
INNER JOIN publishers i
ON b.imprint_id = i.imprint_id
WHERE i.imprint = 'TarcherPerigee';
DROP TEMPORARY TABLE temp_book_table;
COMMIT;

START TRANSACTION;
SELECT DISTINCT
	imprint
FROM publishers
WHERE COALESCE(publishing_house, '') = '';
CREATE TEMPORARY TABLE IF NOT EXISTS temp_publishers_table AS
	SELECT imprint_id
    FROM publishers
    WHERE COALESCE(publishing_house, '') = '';
UPDATE publishers SET publishing_house = imprint WHERE imprint_id IN (SELECT imprint_id FROM temp_publishers_table);
SELECT DISTINCT
	imprint,
    publishing_house
FROM publishers
WHERE imprint_id IN (SELECT imprint_id FROM temp_publishers_table);
DROP TEMPORARY TABLE temp_publishers_table;
COMMIT;
