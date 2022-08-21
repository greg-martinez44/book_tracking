from __future__ import print_function

from GoogleConnection import GoogleConnection
from googleapiclient.errors import HttpError

import pandas as pd

# If modifying these scopes, delete the file token.json.
SCOPES = ['https://www.googleapis.com/auth/spreadsheets.readonly']

# The ID and range of a sample spreadsheet.
SAMPLE_SPREADSHEET_ID = '1zE89OFtETLOf8LFwYjcMrG0kRZ4I8WnSIgDgFT5nMbE'
SAMPLE_RANGE_NAME = 'A1:Z999'


def main():
    google_connection = GoogleConnection()

    try:
        service = google_connection.get_service()

        # Call the Sheets API
        sheet = service.spreadsheets()

        result = sheet.values().get(spreadsheetId=SAMPLE_SPREADSHEET_ID,
                                    range=SAMPLE_RANGE_NAME).execute()
        values = result.get('values', [])

        print(len(values))
        if not values:
            print('No data found.')
            return

        columns = []
        data = []
        for row in values:
            if row[0] == "book_id":
                for column in row:
                    columns.append(column)
            else:
                data.append(row)

        df = pd.DataFrame(data, columns=columns)
        df.to_csv(".\\test_data_output.csv", index=False)
    except HttpError as err:
        print(err)


if __name__ == '__main__':
    main()
