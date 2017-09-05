package pl.pjask.stocknews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private final List<NewsModel> mNewsModels;

    public NewsListAdapter(List<NewsModel> newsList) {
        this.mNewsModels = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NewsModel newsModel = mNewsModels.get(position);
        ((TextView) holder.newsView.findViewById(R.id.item_title)).setText(newsModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return mNewsModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final View newsView;

        public ViewHolder(View itemView) {
            super(itemView);
            newsView = itemView;
        }
    }
}
