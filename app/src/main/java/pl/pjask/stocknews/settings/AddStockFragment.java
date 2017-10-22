package pl.pjask.stocknews.settings;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.Toast;

import pl.pjask.stocknews.Menu;
import pl.pjask.stocknews.R;
import pl.pjask.stocknews.models.Stock;

import static pl.pjask.stocknews.db.DBSchema.SymbolHintTable;


public class AddStockFragment extends DialogFragment implements View.OnClickListener {

    private AutoCompleteTextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_stock, container, false);

        Button addButton = view.findViewById(R.id.button_add);

        prepareAutocompleteTextView(view);

        addButton.setOnClickListener(this);

        return view;
    }

    private void prepareAutocompleteTextView(View view) {
        mTextView = view.findViewById(R.id.stock_symbol);
        mTextView.setThreshold(1);
        final Hints hints = Hints.getInstance(getContext());

        final int[] to = new int[]{android.R.id.text1, android.R.id.text2};
        final String[] from = new String[]{SymbolHintTable.Cols.SYMBOL_NAME, SymbolHintTable.Cols.FULL_NAME};

        SimpleCursorAdapter symbolNameAdapter = new SimpleCursorAdapter(
                getContext(),
                android.R.layout.simple_list_item_2,
                null,
                from,
                to,
                0
        );

        symbolNameAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                final int colIndex = cursor.getColumnIndex(SymbolHintTable.Cols.SYMBOL_NAME);
                return cursor.getString(colIndex);
            }
        });

        symbolNameAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return hints.getSymbolFor(charSequence.toString());
            }
        });

        mTextView.setAdapter(symbolNameAdapter);
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
