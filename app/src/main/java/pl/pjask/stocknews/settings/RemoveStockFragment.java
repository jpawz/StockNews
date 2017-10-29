package pl.pjask.stocknews.settings;

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

import pl.pjask.stocknews.Menu;
import pl.pjask.stocknews.R;

public class RemoveStockFragment extends DialogFragment {

    private ListView listView;
    private List<String> items;
    private boolean[] itemsCheckStatus;

    private Menu menu;
    private ArrayAdapter<String> itemsAdapter;


    private AdapterView.OnItemClickListener mOnListClickListener;
    private View.OnClickListener mButtonOnClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        menu = Menu.getInstance(getContext());


        View view = inflater.inflate(R.layout.fragment_remove_stock, container, false);

        itemsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_checked);
        prepareListViewData();

        prepareOnItemClickListener();
        listView = view.findViewById(R.id.remove_stock_list_view);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(mOnListClickListener);

        prepareOnClickListener();
        Button removeItemsButton = view.findViewById(R.id.remove_selected_stocks);
        removeItemsButton.setOnClickListener(mButtonOnClickListener);

        return view;
    }

    private void prepareListViewData() {
        Set<String> menuItems = menu.getSymbolNames();
        items = new ArrayList<>(menuItems);
        itemsCheckStatus = new boolean[items.size()];
        itemsAdapter.addAll(menuItems);
    }

    private void prepareOnClickListener() {
        mButtonOnClickListener = view -> {
            if (listView.getCheckedItemCount() > 0) {
                int numberOfItems = items.size();
                for (int i = 0; i < numberOfItems; i++) {
                    if (itemsCheckStatus[i]) {
                        menu.removeSymbol(items.get(i));
                    }
                }
                itemsAdapter.clear();
                prepareListViewData();
                itemsAdapter.notifyDataSetChanged();
                listView.clearChoices();

            }
        };
    }

    private void prepareOnItemClickListener() {
        mOnListClickListener = (adapterView, view, i, l) -> itemsCheckStatus[i] = !itemsCheckStatus[i];
    }
}