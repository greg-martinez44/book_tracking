from .Database import Database

def all_books():
    query = """
    SELECT
        b.title,
        b.pages,
        b.duration,
        b.year,
        b.format,
        b.genre,
        pub.imprint,
        a.author,
        a.other_authors,
        t.translator,
        n.narrator,
        i.illustrator
    FROM books b
    LEFT JOIN authors a
    ON b.author_id = a.author_id
    LEFT JOIN publishers pub
    ON b.imprint_id = pub.imprint_id
    LEFT JOIN narrators n
    ON b.narrator_id = n.narrator_id
    LEFT JOIN translators t
    ON b.translator_id = t.translator_id
    LEFT JOIN illustrators i
    ON b.illustrator_id = i.illustrator_id
    """

    with Database() as db:
        return db.query(query)

def completed_books():
    query = """
    SELECT
        b.book_id,
        b.title,
        b.genre,
        b.pages,
        b.duration,
        b.format,
        b.year,
        cr.started,
        cr.finished,
        cr.rating,
        pur.source,
        pub.imprint,
        CASE
            WHEN b.genre IN (
                'horror',
                'fantasy',
                'general_fiction',
                'comedy',
                'sci_fi',
                'poems',
                'romance',
                'young_adult',
                'mystery'
            ) THEN 'f'
            ELSE 'nf'
        END 'f_nf',
        YEAR(cr.finished) 'year_read',
        MONTHNAME(cr.finished) 'month_read',
        DATEDIFF(cr.finished, cr.started) + 1 'days_to_finish'
    FROM books b
    INNER JOIN completed_reads cr
    ON b.book_id = cr.book_id
    LEFT JOIN publishers pub
    ON pub.imprint_id = b.imprint_id
    LEFT JOIN purchases pur
    ON pur.book_id = b.book_id
    """

    with Database() as db:
        return db.query(query)

def all_purchases():
    query = """
    SELECT
        p.book_id,
        b.title,
        p.source,
        p.price,
        p.purchase_date
    FROM purchases p
    INNER JOIN books b
    ON p.book_id = b.book_id
    """
    query = "select * from v_completed_read_stats"
    with Database() as db:
        return db.query(query)

def unfinished_reads():
    query = """
    select
        b.book_id,
        b.title,
        s.started
    from books b
    inner join starts s
    on b.book_id = s.book_id
    where s.added_to_reads = 0
    """
    with Database() as db:
        return db.query(query)

def test_thing():
    return "wowwow"
