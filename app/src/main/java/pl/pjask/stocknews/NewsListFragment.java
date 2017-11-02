package pl.pjask.stocknews;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;

import pl.pjask.stocknews.db.DBSchema;
import pl.pjask.stocknews.utils.ItemClickSupport;
import pl.pjask.stocknews.utils.NewsProvider;

public class NewsListFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private ArrayList<String> stockSymbols;
    private Cursor cursor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsProvider.getInstance(getContext()).setDataChangeListener(this::fetchNews);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.news_list_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener((recyclerView, position, view) -> {
            cursor.moveToPosition(position);
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.NewsTable.Cols.URL)))));
        });


        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("symbols")) {
            stockSymbols = bundle.getStringArrayList("symbols");
        }

        return rootView;
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
            cursor = NewsProvider.getInstance(context).queryNewsFor(stockSymbols);
            return cursor.getCount() > 0;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                mRecyclerView.setAdapter(new NewsListAdapter(context, cursor));
            }
        }
    }
}
