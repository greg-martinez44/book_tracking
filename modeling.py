from datetime import datetime
from typing import Any, List, TypeVar
from connections import mysql_connection, google_connection
from cleaner import get_clean_f_books, get_clean_f_read_stats
import pandas as pd
import numpy as np
import seaborn as sns
from matplotlib import pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages

class Model:
    def __init__(self, database: str) -> None:
        if database == "mysql":
            f_books = mysql_connection("f_books")
            f_read_stats = mysql_connection("f_read_stats")
        elif database == "google":
            book_db_sheet_id = "1zE89OFtETLOf8LFwYjcMrG0kRZ4I8WnSIgDgFT5nMbE"
            f_books = google_connection(book_db_sheet_id, "f_books")
            f_read_stats = google_connection(book_db_sheet_id, "f_read_stats")
        else:
            raise ValueError("database must be either 'mysql' or 'google'")

        self.f_books_df = get_clean_f_books(f_books)
        self.f_read_stats_df = get_clean_f_read_stats(f_read_stats)
        self.completed_reads_df = self.__get_completed_reads_df()
        self.dnf_reads_df = self.__get_dnf_reads_df()
        self.purchases_df = self.__get_purchases_df()


    def __get_completed_reads_df(self) -> pd.DataFrame:
        completed_reads = self.f_read_stats_df[self.f_read_stats_df["finished"].notna()].copy()
        completed_reads["month_read"] = completed_reads["finished"].dt.month_name().astype("string")
        completed_reads["year_read"] = completed_reads["finished"].dt.year.astype("int16")
        completed_reads["days_to_finish"] = (completed_reads["finished"] - completed_reads["started"]).dt.days
        return completed_reads
    
    def __get_dnf_reads_df(self) -> pd.DataFrame:
        dnf_reads = self.f_read_stats_df[self.f_read_stats_df["finished"].isna()][["book_id", "started"]].copy()
        dnf_reads["month_started"] = dnf_reads["started"].dt.month_name().astype("string")
        dnf_reads["year_started"] = dnf_reads["started"].dt.year.astype("int16")
        return dnf_reads

    def __get_purchases_df(self) -> pd.DataFrame:
        return self.f_books_df[self.f_books_df["price"].notna()].copy()

    def __make_timeline(self) -> List[str]:
        """Creates a custom axes to order the matplotlib visuals on"""
        month_order = [
            "January", "February", "March",
            "April", "May", "June",
            "July", "August", "September",
            "October", "November", "December"
        ]
        time_order = []
        for year in [str(year) for year in range(2015, datetime.today().year + 1)]:
            for month in month_order:
                time_order.append(month + " " + year)
        return time_order
    
    def __add_timeline(self, df: pd.DataFrame) -> pd.DataFrame:
        """Adds the timeline to df as a categorical variable"""
        timeline = self.__make_timeline()
        df_timeline = df.copy()
        missing = ["August 2019", "September 2019", "November 2019", "November 2017"]
        
        keys = df_timeline["month_read"] + " " + df_timeline["year_read"].astype("str")
        
        df_timeline["timeline"] = pd.Categorical(
            keys,
            [key for key in timeline if key in np.append(keys.values, missing)],
            ordered=True
        )
        return df_timeline.sort_values(by="timeline")
    
    def __format_grouped_by_date(self, df: pd.DataFrame, col: str, estimator: str) -> pd.DataFrame:
        """Creates a grouped df with values aggregated on the passed value 'estimator'"""
        methods = {
            "count": pd.Series.count, 
            "mean": pd.Series.mean, 
            "max": pd.Series.max
        }
        return (
            self.__add_timeline(df)[[col, "timeline"]]
            .groupby("timeline")[col]
            .apply(methods[estimator])
            .reset_index()
        )
    
    def __plot_over_time(self, df: pd.DataFrame, col: str, ax: Any):
        timeline = df['timeline']
        x_vals = [label if label.startswith("January") else "" for label in timeline]
        if ax is None:
            fig, ax = plt.subplots(1,1,figsize=(18,8))
        ax.plot(timeline, df[col], marker='o')

        col_label, aggregator = format_titles(col)

        plt.xticks(range(len(x_vals)), x_vals, rotation=65, ha='right')
        plt.title(f"{aggregator} of {col_label} Over Time")
        plt.xlabel("")
        plt.ylabel(f"{aggregator} of {col_label}")
        plt.axhline(
            np.mean(df[col]),
            label = f"Average {col_label} in a month: " + str(np.ceil(np.mean(df[col]))),
            color="orange"
        )
        plt.axvline("October 2018", label="Move to Nor Cal", color="red", linestyle="--")
        plt.axvline("August 2020", label="Move to Missouri", color="orange", linestyle="--")
        plt.axvline("June 2017", label="Graduated From UCI", color="violet", linestyle="--")
        plt.axvline("July 2021", label="Move Back to California", color="teal", linestyle="--")
        plt.legend(loc=3)

    def books_over_time(self, ax:Any=None, show=bool, pdf:PdfPages=None) -> None:
        """This probably needs to be made to look more like the one in the notebook to make
        sure it shows correctly.
        """
        books_over_time = self.__format_grouped_by_date(df, "title", "count")
        self.__plot_over_time(books_over_time, 'title', ax)