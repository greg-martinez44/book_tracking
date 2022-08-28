import pandas as pd


def mysql_connection(view_name):
    from python_mysql_connector.python_mysql_connector import Database
    with Database() as db:
        return db.query(f"select * from {view_name}")


def google_connection(sheet_id, sheet_name):
    from gpysheets.gpysheets.QuerySheet import QuerySheet
    query_result = QuerySheet.query_sheet(sheet_id, sheet_name)
    return pd.DataFrame(query_result["data"], columns=query_result["columns"])


if __name__ == "__main__":
    conns = google_connection(
        "1zE89OFtETLOf8LFwYjcMrG0kRZ4I8WnSIgDgFT5nMbE", "f_books")
    print(conns.columns)

    # conns = mysql_connection("f_books")
    # print(conns.columns)
