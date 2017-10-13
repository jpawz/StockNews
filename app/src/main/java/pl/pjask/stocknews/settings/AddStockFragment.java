package pl.pjask.stocknews.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

import pl.pjask.stocknews.Menu;
import pl.pjask.stocknews.MenuPreferences;
import pl.pjask.stocknews.R;
import pl.pjask.stocknews.models.Stock;


public class AddStockFragment extends DialogFragment implements View.OnClickListener {

    private MenuPreferences mMenuPreferences;
    private AutoCompleteTextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMenuPreferences = MenuPreferences.newInstance(getContext());

        View view = inflater.inflate(R.layout.fragment_add_stock, container, false);

        Button addButton = view.findViewById(R.id.button_add);

        prepareAutocompleteTextView(view);

        addButton.setOnClickListener(this);

        return view;
    }

    private void prepareAutocompleteTextView(View view) {
        mTextView = view.findViewById(R.id.stock_symbol);
        mTextView.setThreshold(1);

        Set<String> symbolsSet = mMenuPreferences.getSymbols();
        String[] symbols = symbolsSet.toArray(new String[symbolsSet.size()]);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, symbols);
        mTextView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        String stockSymbol = mTextView.getText().toString();
        if (stockSymbol.isEmpty()) {
            return;
        }

        Stock stock = new Stock(stockSymbol);

        Menu menu = Menu.getInstance(getContext());
        menu.addSymbol(stock);

        mTextView.setText("");
        Toast.makeText(getContext(), stockSymbol + " added!", Toast.LENGTH_SHORT)
                .show();
    }
}
