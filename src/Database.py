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
        "test_books": "title",
        "test_authors": ["author", "other_authors"],
        "test_publishers": ["imprint", "publishing_house"],
        "test_translators": "translator",
        "test_narrators": "narrator",
        "test_illustrators": "illustrator",
    }

    def __init__(self, test=False):
        if Database.IS_CONNECTED:
            raise ValueError("Database already connected")
        Database.IS_CONNECTED = True
        self.test = test
        self.cnx = mysql.connector.connect(**Database.CONFIG)
        self.cursor = self.cnx.cursor(buffered=True)

    def __enter__(self):
        return self

    def __exit__(self, *args):
        self.close()

    def query(self, query):
        return pd.read_sql(query, self.cnx)

    def add_book(self, table="books", purchase_table='purchases', **kwargs):
        for required_data in [
            "author",
            "imprint",
            "source",
            "price",
            "purchase_date",
        ]:
            if kwargs.get(required_data) is None:
                raise ValueError(f"Must include {required_data}")

        if self.test:
            table = f"test_{table}"
            purchase_table = "test_purchases"

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

        book_insert_query = f"""
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
        book_values = (
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

        self.insert_query(book_insert_query, book_values)

        book_id = self.get_book_id(**kwargs)
        purchases_insert_query = f"""
        insert into {purchase_table} (
            book_id,
            source,
            price,
            purchase_date
        )
        values (%s, %s, %s, %s)
        """
        purchases_values = (
            book_id,
            kwargs.get("source"),
            kwargs.get("price"),
            kwargs.get("purchase_date")
        )
        self.insert_query(purchases_insert_query, purchases_values)

    def insert_query(self, query, values):
        self.cursor.execute(query, values)
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

    def get_book_id(self, **kwargs):
        table = 'books'
        if self.test and not table.startswith("test"):
            table = f"test_books"

        where_clause = []
        for column, value in kwargs.items():
            if column in [
                'title',
                'pages',
                'duration',
                'year',
                'format',
                'genre'
            ]:
                where_clause.append(f"{column} = '{value}'")
        where_clause = " and ".join(where_clause)
        query = f"""
        select book_id
        from {table}
        where {where_clause}
        """

        self.cursor.execute(query)
        return self.cursor.fetchone()[0]

if __name__ == "__main__":
    db = Database()
    print("Connection successful")
    db.close()
