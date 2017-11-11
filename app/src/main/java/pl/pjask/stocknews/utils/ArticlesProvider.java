package pl.pjask.stocknews.utils;


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
import pl.pjask.stocknews.models.ArticleModel;
import pl.pjask.stocknews.models.Stock;

import static pl.pjask.stocknews.db.DBSchema.ArticlesTable;

public class ArticlesProvider {
    private static ArticlesProvider mArticlesProvider;
    private final SQLiteDatabase db;
    private DataChangeListener mDataChangeListener;

    private ArticlesProvider(Context context) {
        db = DBHelper.getInstance(context)
                .getWritableDatabase();
    }

    public static synchronized ArticlesProvider getInstance(Context context) {
        if (mArticlesProvider == null) {
            mArticlesProvider = new ArticlesProvider(context);
        }
        return mArticlesProvider;
    }

    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        mDataChangeListener = dataChangeListener;
    }

    /**
     * Inserts {@link ArticleModel} into ArticlesTable in database.
     *
     * @param articleModel {@link ArticleModel} to insert.
     */
    private void addNews(ArticleModel articleModel) {
        ContentValues values = getContentValues(articleModel);
        db.insertWithOnConflict(ArticlesTable.NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }


    /**
     * Returns cursor over result set for stock symbols.
     *
     * @param stockSymbols name of stock ("KGHM", "PGE" etc.).
     * @return cursor for database query, positioned before the first entry.
     */
    public Cursor queryNewsFor(ArrayList<String> stockSymbols) {
        String tableName = ArticlesTable.NAME;
        String[] columns = new String[]{ArticlesTable.Cols.ID,
                ArticlesTable.Cols.TITLE,
                ArticlesTable.Cols.SYMBOL,
                ArticlesTable.Cols.URL,
                ArticlesTable.Cols.DATE};
        String questionMarks = (new String(new char[stockSymbols.size()]).replace("\0", "?, "));
        questionMarks = questionMarks.substring(0, questionMarks.length() - 2);
        String selection = ArticlesTable.Cols.SYMBOL + " IN (" + questionMarks + ")";
        String[] selectionArgs = stockSymbols.toArray(new String[0]);
        String orderBy = ArticlesTable.Cols.DATE + " DESC";

        return db.query(
                tableName,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );
    }

    /**
     * Updates articles from website. Inserts data to database.
     *
     * @param stock symbol to update.
     */
    public void updateArticles(Stock stock) {
        new UpdateTask().execute(stock);
    }


    private ContentValues getContentValues(ArticleModel articleModel) {
        ContentValues values = new ContentValues();
        values.put(ArticlesTable.Cols.TITLE, articleModel.getTitle());
        values.put(ArticlesTable.Cols.SYMBOL, articleModel.getStockSymbol());
        values.put(ArticlesTable.Cols.URL, articleModel.getUrl());
        values.put(ArticlesTable.Cols.DATE, articleModel.getDate());

        return values;
    }

    public interface DataChangeListener {
        void onDataChanged();
    }

    private class UpdateTask extends AsyncTask<Stock, Void, Boolean> {
        private BankierParser mBankierParser;
        private List<ArticleModel> mArticleModels;

        @Override
        protected Boolean doInBackground(Stock... stocks) {
            mBankierParser = new BankierParser();
            try {
                mArticleModels = new ArrayList<>();
                for (Stock symbol : stocks) {
                    mArticleModels.addAll(mBankierParser.getArticles(symbol));
                }
            } catch (IOException e) {
                Log.e("ArticlesProvider", e.getMessage());
                return false;
            }
            return mArticleModels != null;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                for (ArticleModel model : mArticleModels) {
                    addNews(model);
                }
                if (mDataChangeListener != null) {
                    mDataChangeListener.onDataChanged();
                }
            }
        }
    }
}
