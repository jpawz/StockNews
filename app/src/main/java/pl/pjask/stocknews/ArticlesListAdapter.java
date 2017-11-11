package pl.pjask.stocknews;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.pjask.stocknews.db.DBSchema;

public class ArticlesListAdapter extends RecyclerView.Adapter<ArticlesListAdapter.ViewHolder> {

    private final CursorAdapter mCursorAdapter;
    private final Context mContext;

    public ArticlesListAdapter(Context context, Cursor cursor) {
        mContext = context;

        mCursorAdapter = new NewsCursorAdapter(context, cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);

        return new ViewHolder(mContext, v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.newsView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View newsView;
        final Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            newsView = itemView;
            this.context = context;
        }

    }

    private class NewsCursorAdapter extends CursorAdapter {
        public NewsCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView itemTitle = view.findViewById(R.id.item_title);

            String title = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.ArticlesTable.Cols.TITLE));
            itemTitle.setText(title);
        }
    }
}
