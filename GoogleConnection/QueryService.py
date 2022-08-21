import GoogleConnection as gc
from googleapiclient.errors import HttpError


class queryService:

    FULL_SHEET_RANGE = "A1:Z400000"

    def __init__(self, sheet_id='1zE89OFtETLOf8LFwYjcMrG0kRZ4I8WnSIgDgFT5nMbE', google_connection=None):
        self.conn = google_connection or gc.GoogleConnection.googleConnection()
        self.sheet_id = sheet_id

    def query_sheet(self):
        try:
            service = self.conn.get_service()

            # Call the Sheets API
            sheet = service.spreadsheets()

            result = sheet.values().get(spreadsheetId=self.sheet_id,
                                        range=queryService.FULL_SHEET_RANGE).execute()
            values = result.get('values', [])

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
            return {
                "columns": columns,
                "data": data
            }
        except HttpError as err:
            print(err)
