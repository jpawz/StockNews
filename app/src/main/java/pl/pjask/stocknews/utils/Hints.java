package pl.pjask.stocknews.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import pl.pjask.stocknews.db.DBHelper;
import pl.pjask.stocknews.db.DBSchema.SymbolHintTable;
import pl.pjask.stocknews.models.Stock;

/**
 * Serves as utility class delivering hints for AutocompleteTextView with AddStockFragment.
 */
public class Hints {
    private static Hints hints;
    private final SQLiteDatabase mDatabase;

    private Hints(Context context) {
        mDatabase = DBHelper.getInstance(context)
                .getWritableDatabase();
    }

    public static synchronized Hints getInstance(Context context) {
        if (hints == null) {
            hints = new Hints(context);
        }
        return hints;
    }


    private void updateHints(Set<Stock> symbols) {
        clearTable();
        for (Stock stock : symbols) {
            ContentValues values = getContentValues(stock);
            mDatabase.insert(SymbolHintTable.NAME, null, values);
        }
    }

    private void clearTable() {
        mDatabase.delete(SymbolHintTable.NAME, null, null);
    }


    private ContentValues getContentValues(Stock symbol) {
        ContentValues values = new ContentValues();
        values.put(SymbolHintTable.Cols.SYMBOL_NAME, symbol.getStockSymbol());
        values.put(SymbolHintTable.Cols.FULL_NAME, symbol.getStockFullName());

        return values;
    }

    /**
     * Filters symbols for selected substring.
     *
     * @param symbolFragment part of symbol.
     * @return cursor matching pattern: "symbolFragment*".
     */
    public Cursor getSymbolFor(String symbolFragment) {
        String sqlQuery = " SELECT " + SymbolHintTable.Cols.ID +
                ", " + SymbolHintTable.Cols.SYMBOL_NAME +
                ", " + SymbolHintTable.Cols.FULL_NAME +
                " FROM " + SymbolHintTable.NAME +
                " WHERE " + SymbolHintTable.Cols.SYMBOL_NAME +
                " LIKE '" + symbolFragment + "%' " +
                " ORDER BY " + SymbolHintTable.Cols.SYMBOL_NAME;


        return mDatabase.rawQuery(sqlQuery, null);
    }

    /**
     * Updates stored stock symbols with new ones (if website is accessible).
     */
    public void updateSymbolList() {
        (new UpdateSymbolsTask()).execute();
    }

    private class UpdateSymbolsTask extends AsyncTask<Void, Void, Boolean> {
        private Set<Stock> updatedSymbols;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                updatedSymbols = new TreeSet<>((new BankierParser()).getStockMap().values());
            } catch (IOException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                hints.updateHints(updatedSymbols);
            }
        }
    }
}
