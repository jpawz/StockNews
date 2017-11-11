package pl.pjask.stocknews.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pl.pjask.stocknews.db.DBHelper;
import pl.pjask.stocknews.db.DBSchema.MenuTable;
import pl.pjask.stocknews.db.StockCursorWrapper;
import pl.pjask.stocknews.models.Stock;

public class MenuUtils {
    private static MenuUtils menuUtils;
    private final SQLiteDatabase mDatabase;
    private MenuChangeListener mMenuChangeListener;

    private MenuUtils(Context context) {
        mDatabase = DBHelper.getInstance(context)
                .getWritableDatabase();
    }

    public static synchronized MenuUtils getInstance(Context context) {
        if (menuUtils == null) {
            menuUtils = new MenuUtils(context);
        }
        return menuUtils;
    }

    public void setMenuChangeListener(MenuChangeListener listener) {
        this.mMenuChangeListener = listener;
    }

    public void addSymbol(Stock symbol) {
        ContentValues values = getContentValues(symbol);
        mDatabase.insertWithOnConflict(MenuTable.NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
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

    public List<Stock> getStocks() {
        List<Stock> menuSet = new ArrayList<>();

        StockCursorWrapper cursor = queryStocks();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            menuSet.add(cursor.getStock());
            cursor.moveToNext();
        }
        return menuSet;
    }

    public Stock getStock(String symbol) {
        Cursor cursor = mDatabase.query(
                MenuTable.NAME,
                new String[]{MenuTable.Cols.SYMBOL_NAME,
                        MenuTable.Cols.FETCH_NEWS,
                        MenuTable.Cols.FETCH_ESPI},
                MenuTable.Cols.SYMBOL_NAME + " = ?",
                new String[]{symbol},
                null,
                null,
                null);
        StockCursorWrapper wrapper = new StockCursorWrapper(cursor);
        wrapper.moveToFirst();
        return wrapper.getStock();
    }

    public List<String> getSymbolNames() {
        List<Stock> stocks = getStocks();
        List<String> symbolNames = new ArrayList<>();
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
                        MenuTable.Cols.FETCH_ESPI},
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

        return values;
    }

    public interface MenuChangeListener {
        void onMenuChanged();
    }
}
