package pl.pjask.stocknews.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Set;
import java.util.TreeSet;

import pl.pjask.stocknews.db.DBHelper;
import pl.pjask.stocknews.db.DBSchema.MenuTable;
import pl.pjask.stocknews.db.StockCursorWrapper;
import pl.pjask.stocknews.models.Stock;

public class Menu {
    private static Menu menu;
    private final SQLiteDatabase mDatabase;
    private MenuChangeListener mMenuChangeListener;

    private Menu(Context context) {
        mDatabase = DBHelper.getInstance(context)
                .getWritableDatabase();
    }

    public static synchronized Menu getInstance(Context context) {
        if (menu == null) {
            menu = new Menu(context);
        }
        return menu;
    }

    public void setMenuChangeListener(MenuChangeListener listener) {
        this.mMenuChangeListener = listener;
    }

    public void addSymbol(Stock symbol) {
        ContentValues values = getContentValues(symbol);
        mDatabase.insert(MenuTable.NAME, null, values);
        if (mMenuChangeListener != null) {
            mMenuChangeListener.onMenuChanged();
        }
    }

    public void removeSymbol(String symbolName) {
        mDatabase.execSQL("DELETE FROM " + MenuTable.NAME +
                " WHERE " + MenuTable.Cols.SYMBOL_NAME + " = '" + symbolName + "'");
        if (mMenuChangeListener != null) {
            mMenuChangeListener.onMenuChanged();
        }
    }

    private Set<Stock> getStocks() {
        Set<Stock> menuSet = new TreeSet<>();

        StockCursorWrapper cursor = queryStocks();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            menuSet.add(cursor.getStock());
            cursor.moveToNext();
        }
        return menuSet;
    }

    public Set<String> getSymbolNames() {
        Set<Stock> stocks = getStocks();
        Set<String> symbolNames = new TreeSet<>();
        for (Stock stock : stocks) {
            symbolNames.add(stock.getStockSymbol());
        }

        return symbolNames;
    }

    private StockCursorWrapper queryStocks() {
        Cursor cursor = mDatabase.query(
                MenuTable.NAME,
                new String[]{MenuTable.Cols.SYMBOL_NAME,
                        MenuTable.Cols.FETCH_NEWS,
                        MenuTable.Cols.FETCH_ESPI,
                        MenuTable.Cols.FETCH_FORUM},
                null,
                null,
                null,
                null,
                null
        );

        return new StockCursorWrapper(cursor);
    }

    private ContentValues getContentValues(Stock symbol) {
        ContentValues values = new ContentValues();
        values.put(MenuTable.Cols.SYMBOL_NAME, symbol.getStockSymbol());
        values.put(MenuTable.Cols.FETCH_NEWS, symbol.fetchNews() ? 1 : 0);
        values.put(MenuTable.Cols.FETCH_ESPI, symbol.fetchEspi() ? 1 : 0);
        values.put(MenuTable.Cols.FETCH_FORUM, symbol.fetchForum() ? 1 : 0);

        return values;
    }

    public interface MenuChangeListener {
        void onMenuChanged();
    }
}
