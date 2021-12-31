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
create table test_books
select * from books
"""

# """
# CREATE TABLE test_books (
#     book_id int primary key auto_increment,
#     title varchar(500),
#     pages int,
#     duration float,
#     year int,
#     format varchar(500),
#     genre varchar(500),
#     imprint_id int,
#     author_id int,
#     translator_id int,
#     narrator_id int,
#     illustrator_id int
# )
# """
TEST_AUTHOR_QUERY = """
create table test_authors
select * from authors
"""

# """
# CREATE TABLE test_authors (
#     author_id int primary key auto_increment,
#     author varchar(500),
#     other_authors varchar(500)
# )
# """

TEST_PUBLISHER_QUERY = """
create table test_publishers
select * from publishers
"""

# """
# CREATE TABLE test_publishers (
#     imprint_id int primary key auto_increment,
#     imprint varchar(500),
#     publishing_house varchar(500)
# )
# """

TEST_NARRATOR_QUERY = """
create table test_narrators
select * from narrators
"""

# """
# CREATE TABLE test_narrators (
#     narrator_id int primary key auto_increment,
#     narrator varchar(500) not null
# )
# """

TEST_TRANSLATOR_QUERY = """
CREATE TABLE test_translators
select * from translators
"""

TEST_ILLUSTRATOR_QUERY = """
create table test_illustrators
select * from illustrators
"""

TEST_PURCHASES_QUERY = """
create table test_purchases
select book_id, source, price, '1999-01-01' as purchase_date from purchases
"""

def create_test_table(cnx, cursor, query):
    cursor.execute(query)
    cnx.commit()

def drop_test_table(cnx, cursor, table):
    cursor.execute("DROP TABLE " + table)
    cnx.commit()

@pytest.fixture
def test_book_table():
    tables = {
        "test_books": TEST_BOOK_QUERY,
        "test_authors": TEST_AUTHOR_QUERY,
        "test_publishers": TEST_PUBLISHER_QUERY,
        "test_translators": TEST_TRANSLATOR_QUERY,
        "test_narrators": TEST_NARRATOR_QUERY,
        "test_illustrators": TEST_ILLUSTRATOR_QUERY,
        "test_purchases": TEST_PURCHASES_QUERY,
    }
    cnx = mysql_conn.connect(**CONFIG)
    cursor = cnx.cursor(buffered=True)
    for table in tables.values():
        create_test_table(cnx, cursor, table)
    yield
    for table in tables:
        drop_test_table(cnx, cursor, table)
    cursor.close()
    cnx.close()
