package pl.pjask.stocknews;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import pl.pjask.stocknews.db.DBHelper;
import pl.pjask.stocknews.db.DBSchema;
import pl.pjask.stocknews.utils.ArticlesProvider;
import pl.pjask.stocknews.utils.ItemClickSupport;

public class ArticlesListFragment extends Fragment {
    private SQLiteDatabase db;
    private RecyclerView mRecyclerView;
    private ArrayList<String> stockSymbols;
    private Cursor cursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArticlesProvider.getInstance(getContext()).setDataChangeListener(this::fetchNews);
        db = DBHelper.getInstance(getContext()).getWritableDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.news_list_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(this::doClick);


        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("symbols")) {
            stockSymbols = bundle.getStringArrayList("symbols");
        }

        return rootView;
    }

    private void doClick(RecyclerView recyclerView, int position, View view) {
        cursor.moveToPosition(position);

        db.updateWithOnConflict(DBSchema.ArticlesTable.NAME,
                getContentValues(),
                DBSchema.ArticlesTable.Cols.ID + " = ?",
                new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DBSchema.ArticlesTable.Cols.ID)))},
                SQLiteDatabase.CONFLICT_IGNORE);


        TextView itemTitle = view.findViewById(R.id.item_title);
        itemTitle.setTypeface(null, Typeface.NORMAL);

        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(
                        cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.ArticlesTable.Cols.URL)))));

    }

    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DBSchema.ArticlesTable.Cols.VISITED, 1);
        return values;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchNews();
    }

    private void fetchNews() {
        if (stockSymbols != null) {
            new FetchNewsTask(mRecyclerView).execute(stockSymbols.toArray(new String[0]));
        }
    }

    private class FetchNewsTask extends AsyncTask<String, Void, Boolean> {
        final RecyclerView mRecyclerView;
        Context context;

        public FetchNewsTask(RecyclerView recyclerView) {
            this.mRecyclerView = recyclerView;
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            context = getContext();
            cursor = ArticlesProvider.getInstance(context).queryNewsFor(stockSymbols);
            return cursor.getCount() > 0;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                mRecyclerView.setAdapter(new ArticlesListAdapter(context, cursor));
            }
        }
    }
}
