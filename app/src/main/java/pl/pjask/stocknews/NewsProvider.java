package pl.pjask.stocknews;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.pjask.stocknews.db.DBHelper;
import pl.pjask.stocknews.models.NewsModel;
import pl.pjask.stocknews.utils.BankierParser;

import static pl.pjask.stocknews.db.DBSchema.NewsTable;

public class NewsProvider {
    private static NewsProvider mNewsProvider;
    private final SQLiteDatabase db;
    private DataChangeListener mDataChangeListener;

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

    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        mDataChangeListener = dataChangeListener;
    }

    /**
     * Inserts NewsModel into NewsTable in database.
     *
     * @param newsModel NewsModel to insert.
     */
    private void addNews(NewsModel newsModel) {
        ContentValues values = getContentValues(newsModel);
        db.insertWithOnConflict(NewsTable.NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }


    /**
     * Returns cursor over result set for stock symbols.
     *
     * @param stockSymbols name of stock ("KGHM", "PGE" etc.).
     * @return cursor for database query, positioned before the first entry.
     */
    public Cursor queryNewsFor(ArrayList<String> stockSymbols) {
        String tableName = NewsTable.NAME;
        String[] columns = new String[]{NewsTable.Cols.ID, NewsTable.Cols.TITLE, NewsTable.Cols.SYMBOL};
        String questionMarks = (new String(new char[stockSymbols.size()]).replace("\0", "?, "));
        questionMarks = questionMarks.substring(0, questionMarks.length() - 2);
        String selection = NewsTable.Cols.SYMBOL + " IN (" + questionMarks + ")";
        String[] selectionArgs = stockSymbols.toArray(new String[0]);

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

    /**
     * Updates news from website. Inserts data to database.
     *
     * @param stockSymbol symbol to update.
     */
    public void updateNews(String stockSymbol) {
        new UpdateNewsTask().execute(stockSymbol);
    }

    private ContentValues getContentValues(NewsModel newsModel) {
        ContentValues values = new ContentValues();
        values.put(NewsTable.Cols.TITLE, newsModel.getTitle());
        values.put(NewsTable.Cols.SYMBOL, newsModel.getStockSymbol());

        return values;
    }

    public interface DataChangeListener {
        void onDataChanged();
    }

    private class UpdateNewsTask extends AsyncTask<String, Void, Boolean> {
        private BankierParser mBankierParser;
        private List<NewsModel> mNewsModels;

        @Override
        protected Boolean doInBackground(String... urls) {
            mBankierParser = new BankierParser();
            try {
                mNewsModels = new ArrayList<>();
                for (String url : urls) {
                    mNewsModels.addAll(mBankierParser.getNews(url));
                }
            } catch (IOException e) {
                Log.e("NewsProvider", e.getMessage());
                return false;
            }
            return mNewsModels != null;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                for (NewsModel model : mNewsModels) {
                    addNews(model);
                }
                if (mDataChangeListener != null) {
                    mDataChangeListener.onDataChanged();
                }
            }
        }
    }

}
