package pl.pjask.stocknews;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pl.pjask.stocknews.db.DBHelper;
import pl.pjask.stocknews.models.NewsModel;

import static pl.pjask.stocknews.db.DBSchema.NewsTable;

public class NewsProvider {
    private static NewsProvider mNewsProvider;
    private final SQLiteDatabase db;

    private NewsProvider(Context context) {
        db = DBHelper.getInstance(context)
                .getWritableDatabase();
    }

    public static synchronized NewsProvider getInstance(Context context) {
        if (mNewsProvider == null) {
            mNewsProvider = new NewsProvider(context);
        }
        return mNewsProvider;
    }

    /**
     * Inserts NewsModel into NewsTable in database.
     *
     * @param newsModel NewsModel to insert.
     */
    public void addNews(NewsModel newsModel) {
        ContentValues values = getContentValues(newsModel);
        db.insertWithOnConflict(NewsTable.NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }


    /**
     * Returns cursor over result set for stock symbol.
     *
     * @param stockSymbol name of stock ("KGHM", "PGE" etc.).
     * @return cursor for database query, positioned before the first entry.
     */
    public Cursor queryNewsFor(String stockSymbol) {
        String tableName = NewsTable.NAME;
        String[] columns = new String[]{NewsTable.Cols.ID, NewsTable.Cols.TITLE, NewsTable.Cols.SYMBOL};
        String selection = NewsTable.Cols.SYMBOL + " = ?";
        String[] selectionArgs = new String[]{stockSymbol};

        return db.query(
                tableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    private ContentValues getContentValues(NewsModel newsModel) {
        ContentValues values = new ContentValues();
        values.put(NewsTable.Cols.TITLE, newsModel.getTitle());
        values.put(NewsTable.Cols.SYMBOL, newsModel.getStockSymbol());

        return values;
    }
}
