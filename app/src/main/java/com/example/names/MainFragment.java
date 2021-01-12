package com.example.names;


import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private NameAdapter nameAdapter;
    private JsonFromUrl getJ;

    private JSONObject json;
    private JSONArray jsonArray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getJ = new JsonFromUrl("https://raw.githubusercontent.com/solita/dev-academy-2021/main/names.json");
        getJ.start();
        try {
            getJ.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            jsonArray = getJ.getJsonArray();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //Set recyclerview and adapter with jsonarray values
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        nameAdapter = new NameAdapter(inflater.getContext(),jsonArray);
        recyclerView.setAdapter(nameAdapter);

        /*
        for(int i=0;i<jsonArray.length();i++){
            try {
                String name = jsonArray.getJSONObject(i).getString("name");
                String amount = jsonArray.getJSONObject(i).getString("amount");

                Log.d("Name", name + " amount: " + amount);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

         */

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_options) {
            NavHostFragment.findNavController(MainFragment.this)
                    .navigate(R.id.action_MainFragment_to_OptionsFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}