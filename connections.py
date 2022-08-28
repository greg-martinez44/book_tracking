import pandas as pd


def mysql_connection():
    from python_mysql_connector.python_mysql_connector import common_queries as cq
    return {
        "all_books": cq.all_books(),
        "completed_books": cq.completed_books(),
        "all_purchases": cq.all_purchases(),
        "unfinished_reads": cq.unfinished_reads(),
        "started_reads": cq.started_reads()
    }


def google_connection(sheet_id, sheet_name):
    from gpysheets.gpysheets.QuerySheet import QuerySheet
    return QuerySheet.query_sheet(sheet_id, sheet_name)


if __name__ == "__main__":
    # conns = google_connection(
    #     "1zE89OFtETLOf8LFwYjcMrG0kRZ4I8WnSIgDgFT5nMbE", "f_books")
    # print(conns["columns"])

    conns = mysql_connection()
    print(conns["all_books"].columns)
