package pl.pjask.stocknews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RemoveStockFragment extends DialogFragment {

    private ListView listView;
    private List<String> items;
    private boolean[] itemsCheckStatus;
    private MenuPreferences mMenuPreferences;

    private ArrayAdapter<String> itemsAdapter;

    private AdapterView.OnItemClickListener mOnItemClickListener;
    private View.OnClickListener mOnClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMenuPreferences = MenuPreferences.newInstance(getContext());
        Set<String> menuItems = mMenuPreferences.getMenuItems();
        items = new ArrayList<>(menuItems);
        itemsCheckStatus = new boolean[items.size()];

        View view = inflater.inflate(R.layout.fragment_remove_stock, container, false);

        prepareOnItemClickListener();
        itemsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked, items);
        listView = (ListView) view.findViewById(R.id.remove_stock_list_view);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(mOnItemClickListener);

        prepareOnClickListener();
        Button removeItemsButton = (Button) view.findViewById(R.id.remove_selected_stocks);
        removeItemsButton.setOnClickListener(mOnClickListener);

        return view;
    }

    private void prepareOnClickListener() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listView.getCheckedItemCount() > 0) {
                    List<String> newItems = new ArrayList<>();
                    int numberOfItems = items.size();
                    for (int i = 0; i < numberOfItems; i++) {
                        if (itemsCheckStatus[i]) {
                            newItems.add(items.get(i));
                        }
                    }
                    itemsCheckStatus = new boolean[items.size()];
                    items.removeAll(newItems);
                    itemsAdapter.notifyDataSetChanged();
                    mMenuPreferences.setSymbols(new TreeSet<>(items));
                    listView.clearChoices();
                }
            }
        };
    }

    private void prepareOnItemClickListener() {
        mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemsCheckStatus[i] = !itemsCheckStatus[i];
            }
        };
    }
}