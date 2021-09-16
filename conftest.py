import pytest
import mysql.connector as mysql_conn

CONFIG = {
    'user': 'jbooks',
    'password': 'jbooks',
    'host': '127.0.0.1',
    'database': 'Library',
    'raise_on_warnings': True
}

TEST_BOOK_QUERY = """
CREATE TABLE test_books (
    book_id int primary key auto_increment,
    title varchar(500),
    pages int,
    duration float,
    year int,
    format varchar(500),
    genre varchar(500),
    imprint_id int,
    author_id int,
    translator_id int,
    narrator_id int,
    illustrator_id int
)
"""
TEST_AUTHOR_QUERY = """
CREATE TABLE test_authors (
    author_id int primary key auto_increment,
    author varchar(500),
    other_authors varchar(500)
)
"""

TEST_PUBLISHER_QUERY = """
CREATE TABLE test_publishers (
    imprint_id int primary key auto_increment,
    imprint varchar(500),
    publishing_house varchar(500)
)
"""

TEST_NARRATOR_QUERY = """
"""

def create_test_table(cnx, cursor, query):
    cursor.execute(query)
    cnx.commit()

def drop_test_table(cnx, cursor, table):
    cursor.execute("DROP TABLE " + table)
    cnx.commit()

@pytest.fixture
def test_book_table():
    cnx = mysql_conn.connect(**CONFIG)
    cursor = cnx.cursor(buffered=True)
    create_test_table(cnx, cursor, TEST_BOOK_QUERY)
    create_test_table(cnx, cursor, TEST_AUTHOR_QUERY)
    create_test_table(cnx, cursor, TEST_PUBLISHER_QUERY)
    yield
    drop_test_table(cnx, cursor, "test_books")
    drop_test_table(cnx, cursor, "test_authors")
    drop_test_table(cnx, cursor, "test_publishers")
    cursor.close()
    cnx.close()
