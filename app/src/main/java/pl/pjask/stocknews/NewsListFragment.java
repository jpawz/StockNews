package pl.pjask.stocknews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import pl.pjask.stocknews.NewsProviders.BankierNewsProvider;

public class NewsListFragment extends Fragment {
    private static final String TAG = "NewsListFragment";

    private static final String BANKIER_URL_TEMPLATE = "http://www.bankier.pl/inwestowanie/profile/quote.html?symbol=";
    private RecyclerView mRecyclerView;
    private List<NewsModel> mNewsModels;

    private String stockSymbol;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.news_list_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("symbol")) {
            stockSymbol = bundle.getString("symbol");
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (stockSymbol != null) {
            new FetchNewsTask(mRecyclerView).execute(BANKIER_URL_TEMPLATE + stockSymbol);
        }
    }

    private class FetchNewsTask extends AsyncTask<String, Void, Boolean> {
        final RecyclerView mRecyclerView;
        private BankierNewsProvider mBankierNewsProvider;

        public FetchNewsTask(RecyclerView recyclerView) {
            this.mRecyclerView = recyclerView;
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            mBankierNewsProvider = new BankierNewsProvider();
            try {
                mNewsModels = mBankierNewsProvider.getNews(urls[0]);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return false;
            }
            return mNewsModels != null;

        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                mRecyclerView.setAdapter(new NewsListAdapter(mNewsModels));
            }
        }
    }

}
