import pandas as pd
import pytest
from src.Database import Database

@pytest.fixture
def db():
    with Database() as db:
        yield db

@pytest.fixture
def db_test():
    with Database(test=True) as db:
        yield db

def test_connection(db):
    assert True

def test_all_books_query(db):
    df = db.all_books()
    expected_columns = [
        "title",
        "pages",
        "duration",
        "year",
        "format",
        "genre",
        "imprint",
        "author",
        "other_authors",
        "translator",
        "narrator",
        "illustrator"
    ]
    actual_columns = df.columns.tolist()
    assert set(expected_columns) == set(actual_columns)

def test_completed_books_query(db):
    df = db.completed_books()
    expected_columns = [
        "book_id",
        "title",
        "genre",
        "pages",
        "duration",
        "format",
        "year",
        "started",
        "finished",
        "rating",
        "source",
        "imprint",
        "f_nf",
        "year_read",
        "month_read",
        "days_to_finish"
    ]
    actual_columns = df.columns.tolist()
    assert set(expected_columns)  == set(actual_columns)

def test_completed_books_calculates_datepart_correctly(db):
    df = db.completed_books()
    expected_month_names = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    ]
    expected_year_values = [
        2021,
        2020,
        2019,
        2018
    ]
    actual_month_names = df["month_read"].values.tolist()
    actual_year_values = df["year_read"].unique().tolist()

    assert set(expected_month_names) == set(actual_month_names)
    for year in expected_year_values:
        assert year in actual_year_values

def test_days_to_read_is_not_negative(db):
    df = db.completed_books()
    df["days_to_finish"].fillna(float("inf"), inplace=True)
    actual_days_to_finish = df["days_to_finish"].unique().tolist()

    for value in actual_days_to_finish:
        assert value > 0

def test_f_nf_tags_correct_rows(db):
    df = db.completed_books()
    expected_genres = [
        "horror",
        "fantasy",
        "general fiction",
        "comedy",
        "sci-fi",
        "poems",
        "romance",
        "young adult",
        "mystery"
    ]
    for idx, row in df.iterrows():
        if row["genre"] in expected_genres:
            assert row["f_nf"] == "f"
        else:
            assert row["f_nf"] == "nf"

def test_new_books_are_added_to_books_table(test_book_table, db):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "imprint": "Tor",
        "author": "Brandon Sanderson"
    }
    db.add_book(table="test_books", **book_data)
    df = pd.read_sql("select * from test_books", db.cnx)

    expected_title = "testbook_1"
    expected_imprint_id = 15
    expected_author_id = 64

    actual_title = df["title"].values
    actual_imprint_id = df["imprint_id"].values
    actual_author_id = df["author_id"].values

    assert df["duration"].isna().all()
    assert df["translator_id"].isna().all()
    assert expected_title == actual_title
    assert expected_imprint_id == actual_imprint_id
    assert expected_author_id == actual_author_id

def test_add_books_fails_if_no_author(test_book_table, db_test):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "imprint": "Tor",
    }
    with pytest.raises(ValueError, match="Must include author"):
        db_test.add_book(table="books", **book_data)

def test_add_books_fails_if_no_imprint(test_book_table, db_test):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "author": "Brandon Sanderson"
    }
    with pytest.raises(ValueError, match="Must include imprint"):
        db_test.add_book(table="books", **book_data)

def test_new_books_are_added_to_books_table_with_optional_columns(test_book_table, db):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "imprint": "Tor",
        "author": "Brandon Sanderson",
        "translator": "John Werry",
        "narrator": "Barack Obama",
        "illustrator": "Keiichi Arawi"
    }
    db.add_book(table="test_books", **book_data)
    df = pd.read_sql("select * from test_books", db.cnx)

    expected_translator_id = 6
    expected_narrator_id = 8
    expected_illustrator_id = 1

    actual_translator_id = df["translator_id"].values
    actual_narrator_id = df["narrator_id"].values
    actual_illustrator_id = df["illustrator_id"].values

    assert expected_translator_id == actual_translator_id
    assert expected_narrator_id == actual_narrator_id
    assert expected_illustrator_id == actual_illustrator_id


@pytest.mark.skip("Succeeded to insert into production table")
def test_live_test_delete_after_running_once(db):
    book_data = {
        "title": "Beautiful Country",
        "pages": 297,
        "year": 2021,
        "format": "hardcover",
        "genre": "memoir",
        "author": "Qian Julie Wang",
        "imprint": "Doubleday"
    }
    db.add_book(**book_data)

def test_make_new_values_in_parent_table(test_book_table, db_test):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "imprint": "testimprint_1",
        "author": "testauthor_1"
    }
    db_test.add_book(**book_data)
    df = pd.read_sql("select * from test_books", db_test.cnx)

    expected_author_id = 1
    expected_imprint_id = 1

    actual_author_id = df["author_id"].values
    actual_imprint_id = df["imprint_id"].values

    assert expected_author_id == actual_author_id
    assert expected_imprint_id == actual_imprint_id


def test_multiple_authors_added_to_author_table(test_book_table, db_test):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "imprint": "testimprint_1",
        "author": "testauthor_1",
        "other_authors": "test_other_authors"
    }
    db_test.add_book(table="books", **book_data)
    df = pd.read_sql("select * from test_authors", db_test.cnx)

    expected_author_id = 1

    actual_author_id = df["author_id"].values
    actual_other_authors = df["other_authors"].values

    assert expected_author_id == actual_author_id
    assert "test_other_authors" == actual_other_authors

def test_imprint_and_publishing_house_added_to_publisher_table(test_book_table, db_test):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "imprint": "testimprint_1",
        "publishing_house": "NewHouse",
        "author": "testauthor_1",
        "other_authors": "test_other_authors"
    }
    db_test.add_book(table="books", **book_data)
    df = pd.read_sql("select * from test_publishers", db_test.cnx)

    expected_imprint_id = 1

    actual_imprint_id = df["imprint_id"].values
    actual_publishing_house = df["publishing_house"].values

    assert expected_imprint_id == actual_imprint_id
    assert "NewHouse" == actual_publishing_house
