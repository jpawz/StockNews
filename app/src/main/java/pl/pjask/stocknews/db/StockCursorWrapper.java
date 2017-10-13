package pl.pjask.stocknews.db;


import android.database.Cursor;
import android.database.CursorWrapper;

import pl.pjask.stocknews.models.Stock;

public class StockCursorWrapper extends CursorWrapper {
    public StockCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Stock getStock() {
        String stockSymbol = getString(getColumnIndex(DBSchema.MenuTable.Cols.SYMBOL_NAME));

        return new Stock(stockSymbol);
    }
}
