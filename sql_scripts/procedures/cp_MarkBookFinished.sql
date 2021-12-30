START TRANSACTION;
SET @completed_title = 'Rent-A-Girlfriend%(Volume 7)%';
SET @finished = '2021-12-30';
SET @rating = 3;
SELECT b.book_id INTO @book_id
FROM books b
left join starts s
on b.book_id = s.book_id
WHERE b.title like @completed_title
and s.added_to_reads = 0;
SELECT started INTO @started FROM starts WHERE book_id = @book_id AND added_to_reads = 0;
SELECT * FROM books where book_id = @book_id;
INSERT INTO completed_reads (book_id, started, finished, rating)
VALUES
(@book_id, @started, @finished, @rating);
SELECT
	b.book_id,
    b.title,
    cr.read_id,
    cr.started,
    cr.finished,
    cr.rating
FROM completed_reads cr
INNER JOIN books b
ON b.book_id = cr.book_id
WHERE b.book_id = @book_id AND cr.started = @started;
UPDATE starts SET added_to_reads = 1
WHERE book_id = @book_id
AND started = @started
AND added_to_reads = 0;
commit;