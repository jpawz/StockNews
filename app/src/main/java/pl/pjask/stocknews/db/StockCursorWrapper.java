package pl.pjask.stocknews.db;


import android.database.Cursor;
import android.database.CursorWrapper;

import pl.pjask.stocknews.models.Stock;

import static pl.pjask.stocknews.db.DBSchema.MenuTable;

public class StockCursorWrapper extends CursorWrapper {
    public StockCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Stock getStock() {
        String stockSymbol = getString(getColumnIndex(MenuTable.Cols.SYMBOL_NAME));
        int fetchNews = getInt(getColumnIndex(MenuTable.Cols.FETCH_NEWS));
        int fetchEspi = getInt(getColumnIndex(MenuTable.Cols.FETCH_ESPI));
        int fetchForum = getInt(getColumnIndex(MenuTable.Cols.FETCH_FORUM));


        Stock stock = new Stock(stockSymbol);
        stock.setFetchNews(fetchNews == 1);
        stock.setFetchEspi(fetchEspi == 1);
        stock.setFetchForum(fetchForum == 1);
        return stock;
    }
}
