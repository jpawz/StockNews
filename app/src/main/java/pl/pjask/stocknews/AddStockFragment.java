package pl.pjask.stocknews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class AddStockFragment extends DialogFragment implements View.OnClickListener {

    private MenuPreferences mMenuPreferences;

    private TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_stock, container, false);

        mTextView = (TextView) view.findViewById(R.id.stock_symbol);
        Button addButton = (Button) view.findViewById(R.id.button_add);

        mMenuPreferences = MenuPreferences.newInstance(getContext());

        addButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        String stockSymbol = mTextView.getText().toString();
        if (stockSymbol.isEmpty()) {
            return;
        }
        mMenuPreferences.addMenuItem(stockSymbol);
        mTextView.setText("");
        Toast.makeText(getContext(), stockSymbol + " added!", Toast.LENGTH_SHORT)
                .show();
    }
}
