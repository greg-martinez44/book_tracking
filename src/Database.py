import pandas as pd
import mysql.connector

from .config import MYSQL_PASS

class Database:
    CONFIG = {
        'user': 'jbooks',
        'password': MYSQL_PASS,
        'host': '127.0.0.1',
        'database': 'Library',
        'raise_on_warnings': True
    }

    IS_CONNECTED = False

    QUERY_CONFIG = {
        "publishers": ["imprint", "publishing_house"],
        "authors": ["author", "other_authors"],
        "translators": "translator",
        "narrators": "narrator",
        "illustrators": "illustrator",
        "test_authors": ["author", "other_authors"],
        "test_publishers": ["imprint", "publishing_house"]
    }

    def __init__(self, test=False):
        if Database.IS_CONNECTED:
            raise ValueError("Database already connected")
        self.test = test

    def __enter__(self):
        self.cnx = mysql.connector.connect(**Database.CONFIG)
        self.cursor = self.cnx.cursor(buffered=True)
        Database.IS_CONNECTED = True
        return self

    def __exit__(self, *args):
        self.close()

    def custom_query(self, query):
        return pd.read_sql(query, self.cnx)

    def all_books(self):
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
        return pd.read_sql(
            query,
            self.cnx
        )

    def completed_books(self):
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
                WHEN genre IN (
                    'horror',
                    'fantasy',
                    'general fiction',
                    'comedy',
                    'sci-fi',
                    'poems',
                    'romance',
                    'young adult',
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
        return pd.read_sql(query, self.cnx)

    def all_purchases(self):
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
        return pd.read_sql(query, self.cnx)

    def add_book(self, table="books", **kwargs):
        if not kwargs.get("author"):
            raise ValueError("Must include author")
        if not kwargs.get("imprint"):
            raise ValueError("Must include imprint")

        if self.test:
            table = f"test_{table}"

        imprint_id = self.get_id(
            "publishers",
            kwargs.get("imprint"),
            kwargs.get("publishing_house")
        )
        author_id = self.get_id(
            "authors",
            kwargs.get("author"),
            kwargs.get("other_authors")
        )
        translator_id = self.get_id(
            "translators",
            kwargs.get("translator")
        )
        narrator_id = self.get_id(
            "narrators",
            kwargs.get("narrator")
        )
        illustrator_id = self.get_id(
            "illustrators",
            kwargs.get("illustrator")
        )

        insert_query = f"""
        INSERT INTO {table} (
            title,
            pages,
            duration,
            year,
            format,
            genre,
            imprint_id,
            author_id,
            translator_id,
            narrator_id,
            illustrator_id
        )
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        values = (
            kwargs.get("title"),
            kwargs.get("pages"),
            kwargs.get("duration"),
            kwargs.get("year"),
            kwargs.get("format"),
            kwargs.get("genre"),
            imprint_id,
            author_id,
            translator_id,
            narrator_id,
            illustrator_id
        )
        self.cursor.execute(insert_query, values)
        self.cnx.commit()

    def close(self):
        self.cursor.close()
        self.cnx.close()
        Database.IS_CONNECTED = False

    def get_id(self, table, name, *args):
        if not name:
            return None
        if self.test and not table.startswith("test"):
            table = f"test_{table}"

        column = Database.QUERY_CONFIG[table]
        where = f"{column} = '{name}'"

        if table in ('authors', 'publishers', 'test_authors', 'test_publishers'):
            column, column2 = column
            where = f"{column} = '{name}'"
            if args[0] is not None:
                where = where + f" AND {column2} = '{args[0]}'"

        self.cursor.execute(
            f"SELECT {column}_id from {table} WHERE {where}"
        )
        if self.cursor.rowcount == 0:
            self.add_id(table, name, *args)
            return self.get_id(table, name, *args)
        return self.cursor.fetchone()[0]

    def add_id(self, table, name, *args):
        columns = Database.QUERY_CONFIG[table]
        values = "%s"

        if table in ('authors', 'publishers', 'test_authors', 'test_publishers'):
            values = ", ".join(["%s"]*len(columns))
            columns = ", ".join(columns)

        insert_query = f"""
        INSERT INTO {table} (
            {columns}
        )
        VALUES ({values})
        """
        self.cursor.execute(insert_query, (name, *args))
        self.cnx.commit()

if __name__ == "__main__":
    db = Database()
    print("Connection successful")
    db.close()
