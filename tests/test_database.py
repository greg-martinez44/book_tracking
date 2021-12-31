import pandas as pd
import pytest
from BooksSQL import Database
import BooksSQL.common_queries as cq

@pytest.fixture
def db_test():
    with Database(test=True) as db:
        yield db

def test_all_books_query():
    df = cq.all_books()
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

def test_completed_books_query():
    df = cq.completed_books()
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

def test_completed_books_calculates_datepart_correctly():
    df = cq.completed_books()
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

def test_days_to_read_is_not_negative():
    df = cq.completed_books()
    df["days_to_finish"].fillna(float("inf"), inplace=True)
    actual_days_to_finish = df["days_to_finish"].unique().tolist()

    for value in actual_days_to_finish:
        assert value > 0

def test_f_nf_tags_correct_rows():
    df = cq.completed_books()
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

def test_new_books_are_added_to_books_table(test_book_table, db_test):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "imprint": "Tor",
        "author": "Brandon Sanderson",
        "source": "bookstore",
        "price": 0,
        "purchase_date": "2021-12-30",
    }
    db_test.add_book(**book_data)

    test_query = f"""
    select *
    from test_books
    where title = 'testbook_1'
    """
    df = pd.read_sql(test_query, db_test.cnx)

    test_query_purchases = f"""
    select *
    from test_purchases
    where source = 'bookstore'
    """
    df_purchases = pd.read_sql(test_query_purchases, db_test.cnx)

    expected_title = "testbook_1"
    expected_imprint_id = 15
    expected_author_id = 64
    expected_price = 0
    expected_purchase_date = '2021-12-30'

    actual_title = df["title"].values
    actual_imprint_id = df["imprint_id"].values
    actual_author_id = df["author_id"].values
    actual_price = df_purchases["price"].values
    actual_purchase_date = df_purchases['purchase_date'].values

    assert df["duration"].isna().all()
    assert df["translator_id"].isna().all()
    assert expected_title == actual_title
    assert expected_imprint_id == actual_imprint_id
    assert expected_author_id == actual_author_id
    assert expected_price == actual_price
    assert expected_purchase_date == actual_purchase_date

def test_add_books_fails_if_no_author(test_book_table, db_test):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "imprint": "Tor",
        "source": "bookstore",
        "price": 0,
        "purchase_date": "2021-12-30",
    }
    with pytest.raises(ValueError, match="Must include author"):
        db_test.add_book(**book_data)

def test_add_books_fails_if_no_imprint(test_book_table, db_test):
    book_data = {
        "title": "testbook_1",
        "pages": 4,
        "year": 3032,
        "format": "hardcover",
        "genre": "fiction",
        "author": "Brandon Sanderson",
        "source": "bookstore",
        "price": 0,
        "purchase_date": "2021-12-30",
    }
    with pytest.raises(ValueError, match="Must include imprint"):
        db_test.add_book(**book_data)

def test_new_books_are_added_to_books_table_with_optional_columns(test_book_table, db_test):
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
        "illustrator": "Keiichi Arawi",
        "source": "bookstore",
        "price": 0,
        "purchase_date": "2021-12-30",
    }
    db_test.add_book(**book_data)
    df = pd.read_sql("select * from test_books where title = 'testbook_1'", db_test.cnx)

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
        "author": "testauthor_1",
        "source": "bookstore",
        "price": 0,
        "purchase_date": "2021-12-30",
    }
    db_test.add_book(**book_data)
    df = pd.read_sql("select * from test_books where title = 'testbook_1'", db_test.cnx)

    expected_author_id = 0
    expected_imprint_id = 0

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
        "other_authors": "test_other_authors",
        "source": "bookstore",
        "price": 0,
        "purchase_date": "2021-12-30",
    }
    db_test.add_book(**book_data)
    df = pd.read_sql("select * from test_authors where author = 'testauthor_1'", db_test.cnx)

    expected_author_id = 0

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
        "other_authors": "test_other_authors",
        "source": "bookstore",
        "price": 0,
        "purchase_date": "2021-12-30",
    }
    db_test.add_book(**book_data)
    df = pd.read_sql("select * from test_publishers where imprint = 'testimprint_1'", db_test.cnx)

    expected_imprint_id = 0

    actual_imprint_id = df["imprint_id"].values
    actual_publishing_house = df["publishing_house"].values

    assert expected_imprint_id == actual_imprint_id
    assert "NewHouse" == actual_publishing_house
