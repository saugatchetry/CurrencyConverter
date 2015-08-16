package saugat.chetry.com.currencyconverter;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by a511863 on 14/08/15.
 */
public class CurrencyFragment extends DialogFragment {

    ListView lv;
    ArrayAdapter<String> adapter;
    String[] curr = {"USD","INR","EUR","GBP"};
    Communicator comm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.available_currencies,null);

        comm = (Communicator) getActivity();
        getDialog().setTitle("Choose Currency");

        lv = (ListView) rootView.findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,curr);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Test","Clicked Item = "+curr[i]);
                String tag = getTag();
                Log.d("Test","Tag = "+tag);
                comm.respond(curr[i],tag);
                dismiss();
            }
        });



        return rootView;
    }


}
