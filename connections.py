import pandas as pd
from python_mysql_connector.python_mysql_connector import Database
from gpysheets.gpysheets.QuerySheet import QuerySheet

def mysql_connection(view_name: str) -> pd.DataFrame:
    with Database() as db:
        return db.query(f"select * from {view_name}")


def google_connection(sheet_id: str, sheet_name:str) -> pd.DataFrame:
    query_result = QuerySheet.query_sheet(sheet_id, sheet_name)
    return pd.DataFrame(query_result["data"], columns=query_result["columns"])


if __name__ == "__main__":
    conns = google_connection(
        "1zE89OFtETLOf8LFwYjcMrG0kRZ4I8WnSIgDgFT5nMbE", "f_books")
    print(conns.columns)

    # conns = mysql_connection("f_books")
    # print(conns.columns)
