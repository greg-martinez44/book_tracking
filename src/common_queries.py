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
    query = "select * from v_completed_read_stats"

    with Database() as db:
        return db.query(query)

def all_purchases():
    query = "select * from v_purchase_stats"
    with Database() as db:
        return db.query(query)

def unfinished_reads():
    query = """
    select * from v_dnf_stats
    """
    with Database() as db:
        return db.query(query)
