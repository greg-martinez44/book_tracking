import numpy as np
import pandas as pd

def get_clean_f_books(f_books_df: pd.DataFrame) -> pd.DataFrame:
    f_books_types = {
        "book_id": "int32",
        "title": "string",
        "pages": "int32",
        "duration": "int32",
        "year": "int16",
        "format": "string",
        "genre": "string",
        "f_nf": "string",
        "author": "string",
        "other_authors": "string",
        "imprint": "string",
        "publishing_house": "string",
        "illustrator": "string",
        "narrator": "string",
        "translator": "string",
        "source": "string",
        "price": "float64"
    }
    f_books_df = f_books_df.astype(f_books_types, errors="ignore")
    f_books_df["purchase_date"] = pd.to_datetime(f_books_df["purchase_date"])
    f_books_df["price"] = np.around(f_books_df["price"], 2)
    return f_books_df

def get_clean_f_read_stats(f_read_stats_df: pd.DataFrame) -> pd.DataFrame:
    f_read_stats_types = {
        "book_id": "int32",
        "rating": "int8"
    }
    f_read_stats_df = f_read_stats_df.astype(f_read_stats_types, errors="ignore")
    f_read_stats_df["started"] = pd.to_datetime(f_read_stats_df["started"])
    f_read_stats_df["finished"] = pd.to_datetime(f_read_stats_df["finished"])
    return f_read_stats_df