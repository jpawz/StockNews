package pl.pjask.stocknews.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.pjask.stocknews.R;


public class ManageStocksFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentTabHost tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.fragment_manage_stocks);

        tabHost.addTab(tabHost.newTabSpec("add").setIndicator("add"),
                AddStockFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("remove").setIndicator("remove"),
                RemoveStockFragment.class, null);

        return tabHost;
    }
}