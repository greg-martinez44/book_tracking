import sys
from datetime import datetime

import pandas as pd
import numpy as np
import seaborn as sns
from matplotlib import pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages

import BooksSQL.common_queries as cq

YEAR=None
ARGS = sys.argv
if len(ARGS) > 2:
    raise ValueError("Too many arguments - should be one or zero")
if len(ARGS) == 2:
    if ARGS[1].isdigit() and int(ARGS[1]) >= 2018:
        YEAR = int(ARGS[1])
    else:
        raise ValueError("Argument not valid year value")


def main():
    all_books, completed_books, purchases = get_data_tables(YEAR)

    with PdfPages('test.pdf') as pdf:
        if YEAR is None:
            books_over_time(completed_books, pdf)
            pages_over_time(completed_books, pdf)
            purchases_over_time(purchases, pdf)
        book_length_hist(completed_books, pdf)
        books_by_season(completed_books, pdf)
        pages_by_season(completed_books, pdf)
        purchases_by_season(purchases, pdf)


def get_data_tables(year):
    all_books = cq.all_books()
    completed_books = cq.completed_books()
    all_purchases = cq.all_purchases()

    all_purchases['purchase_date'] = pd.to_datetime(all_purchases['purchase_date'])
    all_purchases['year_purchased'] = all_purchases['year_purchased'].astype('Int32')
    completed_books['started'] = pd.to_datetime(completed_books['started'])
    completed_books['finished'] = pd.to_datetime(completed_books['finished'])
    completed_books = completed_books[(completed_books['finished'].dt.year >= 2015)].copy()

    if year is not None:
        all_purchases = all_purchases[all_purchases['purchase_date'].dt.year == year]
        completed_books = completed_books[completed_books['finished'].dt.year == year]

    return all_books, completed_books, all_purchases

def books_over_time(df, pdf, ax=None):
    books_over_time = format_grouped_by_date(df, "title", "count")
    plot_over_time(books_over_time, 'title', ax)
    save_pdf(pdf)

def pages_over_time(df, pdf, ax=None):
    pages_over_time = format_grouped_by_date(df, "pages", "mean")
    plot_over_time(pages_over_time, 'pages', ax)
    save_pdf(pdf)

def purchases_over_time(df, pdf, ax=None):
    purchases_over_time = format_grouped_by_date(df, "price", "sum")
    plot_over_time(purchases_over_time, "price", ax)
    save_pdf(pdf)

def book_length_hist(df, pdf, ax=None):
    page_counts = df['pages'].dropna().copy()
    if ax is None:
        fig, ax = plt.subplots(1, 1, figsize=(18,8))
    ax.hist(page_counts, bins=30, alpha=0.7)

    plt.title("Book Lengths")
    plt.ylabel("Number of Books")
    plt.xlabel("Number of Pages")

    mean_pages = np.mean(page_counts)
    median_pages = np.median(page_counts)
    std_pages = np.std(page_counts)

    plt.axvline(mean_pages, color="orange", label=f"Average Number of Pages - {np.ceil(mean_pages)}")
    plt.axvline(median_pages, color="green", label=f"Median Page Count - {np.ceil(median_pages)}")
    plt.axvline(mean_pages + std_pages, linestyle="--", color='red', label=f"1 STD Away From Mean - {np.round(std_pages, 2)}")
    plt.axvline(mean_pages - std_pages, linestyle="--", color="red")
    plt.legend()

    save_pdf(pdf)

def books_by_season(df, pdf, ax=None):
    book_seasons = make_seasons_table(df)[['title', 'season']]
    counts_by_season = book_seasons.groupby("season")['title'].count().reset_index()
    plot_over_seasons(counts_by_season, 'title', ax)
    save_pdf(pdf)

def pages_by_season(df, pdf, ax=None):
    page_seasons = make_seasons_table(df)[['pages', 'season']]
    counts_by_season = page_seasons.groupby('season')['pages'].mean().reset_index()
    plot_over_seasons(counts_by_season, 'pages', ax)
    save_pdf(pdf)

def purchases_by_season(df, pdf, ax=None):
    purchase_seasons = make_seasons_table(df)[['price', 'season']]
    counts_by_season = purchase_seasons.groupby('season')['price'].sum().reset_index()
    plot_over_seasons(counts_by_season, 'price', ax)
    save_pdf(pdf)

def plot_over_seasons(df, col, ax):
    if ax is None:
        fig, ax = plt.subplots(1, 1, figsize=(18,8))
    ax.bar(range(4), df[col])
    ax.set_xticks(range(4))
    ax.set_xticklabels(df['season'], rotation=0)
    col_label, aggregator = format_titles(col)
    ax.set_title(f"{col_label} by Season")
    ax.set_xlabel("")
    ax.set_ylabel(f"{aggregator} of {col_label}")

def plot_over_time(df, col, ax):
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

def format_grouped_by_date(df, col, estimator):
    methods = {
        "count": pd.Series.count,
        "mean": pd.Series.mean,
        "max": pd.Series.max,
        "sum": pd.Series.sum,
    }
    return (
        add_timeline(df)[[col, 'timeline']]
        .groupby('timeline')[col]
        .apply(methods[estimator])
        .reset_index()
    )

def add_timeline(df):
    timeline = make_timeline()
    df_timeline = df.copy()
    missing = [
        "August 2019",
        "September 2019",
        "November 2019",
        "November 2017"
    ]

    try:
        keys = df_timeline['month_read'] + ' ' + df_timeline['year_read'].astype('str')
    except KeyError:
        keys = df_timeline['month_purchased'] + ' ' + df_timeline['year_purchased'].astype('str')

    df_timeline['timeline'] = pd.Categorical(
        keys,
        [key for key in timeline if key in np.append(keys.values, missing)],
        ordered=True
    )
    return df_timeline.sort_values(by="timeline")

def make_timeline():
    month_order = [
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
    time_order = []
    for year in [str(year) for year in range(2015, datetime.today().year+1)]:
        for month in month_order:
            time_order.append(month + " " + year)
    return time_order

def make_seasons_table(df):
    spring = ["March", "April", "May"]
    summer = ['June', 'July', 'August']
    fall = ['September', 'October', 'November']
    winter = ['December', 'January', 'February']

    season_table = df.copy()
    try:
        season_table.loc[season_table["finished"].dt.month_name().isin(spring), "season"] = "spring"
        season_table.loc[season_table["finished"].dt.month_name().isin(summer), "season"] = "summer"
        season_table.loc[season_table["finished"].dt.month_name().isin(fall), "season"] = "fall"
        season_table.loc[season_table["finished"].dt.month_name().isin(winter), "season"] = "winter"
    except KeyError:
        season_table.loc[season_table['purchase_date'].dt.month_name().isin(spring), "season"] = "spring"
        season_table.loc[season_table['purchase_date'].dt.month_name().isin(summer), "season"] = "summer"
        season_table.loc[season_table['purchase_date'].dt.month_name().isin(fall), "season"] = "fall"
        season_table.loc[season_table['purchase_date'].dt.month_name().isin(winter), "season"] = "winter"

    return season_table

def format_titles(col):
    if col == 'pages':
        aggregator = 'Average'
        col_label = col.title()
    elif col == 'title':
        aggregator = 'Number'
        col_label = col.title() + 's'
    elif col == 'price':
        aggregator = 'Total'
        col_label = 'Purchases'
    else:
        aggregator = ""
        col_label = ""

    return col_label, aggregator

def save_pdf(pdf):
    pdf.savefig(bbox_inches="tight")
    plt.close("all")

if __name__ == "__main__":
    main()
