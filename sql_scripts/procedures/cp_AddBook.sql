START TRANSACTION;
set @new_title = 'Wehn Bad Thinking Happens to Good People';
set @new_pages = 204;
set @new_year = 2021;
set @new_format = 'hardcover';
set @new_genre = 'philosophy';
SET @new_author = 'Steven Nadler';
SET @new_other_authors = 'Lawrence Shapiro';
SET @new_imprint = 'Princeton University Press';
SET @new_publishing_house = 'Princeton University Press';
-- SET @new_translator = null;
set @new_source = 'Skylight Books';
set @new_price = 24.95*1.095;
set @new_purchase_date = '2021-12-27';
CALL sp_AddAuthor(@new_author, @new_other_authors);
SELECT author_id INTO @author_id FROM authors WHERE author = @new_author AND COALESCE(other_authors, '') = COALESCE(@new_other_authors, '');
CALL sp_AddImprint(@new_imprint, @new_publishing_house);
SELECT imprint_id INTO @imprint_id FROM publishers WHERE imprint = @new_imprint AND publishing_house = @new_publishing_house;
-- CALL sp_AddTranslator(@new_translator);
-- SELECT translator_id INTO @translator_id FROM translators WHERE translator = @new_translator;
INSERT INTO books (title, pages, year, format, genre, author_id, imprint_id)
VALUES
(@new_title, @new_pages, @new_year, @new_format, @new_genre, @author_id, @imprint_id);
SELECT
	b.book_id,
    b.title,
    b.pages,
    b.duration,
    b.year,
    b.format,
    b.genre,
    imp.imprint,
    imp.publishing_house,
    a.author,
    a.other_authors,
    t.translator,
    ill.illustrator,
    n.narrator
FROM books b
INNER JOIN authors a
ON b.author_id = a.author_id
INNER JOIN publishers imp
ON b.imprint_id = imp.imprint_id
LEFT JOIN translators t
ON b.translator_id = t.translator_id
LEFT JOIN illustrators ill
ON b.illustrator_id = ill.illustrator_id
LEFT JOIN narrators n
ON b.narrator_id = n.narrator_id
WHERE b.title = @new_title;
SELECT book_id INTO @book_id FROM books WHERE title = @new_title and pages=@new_pages;
INSERT INTO purchases (book_id, source, price, purchase_date)
VALUES
(@book_id, @new_source, round(@new_price, 2), @new_purchase_date);
SELECT
	b.book_id,
    b.title,
    p.source,
    p.price,
    p.purchase_date
FROM purchases p
INNER JOIN books b
ON p.book_id = b.book_id
WHERE p.book_id = @book_id;
commit;