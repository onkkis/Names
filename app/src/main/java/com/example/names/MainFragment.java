package com.example.names;


import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private NameAdapter nameAdapter;
    private JsonFromUrl getJ;
    private JSONArray jsonArray;

    private Button search_btn;
    private EditText search_text;

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

        search_btn = v.findViewById(R.id.button_search);
        search_text = v.findViewById(R.id.edittext_search);

        //Set recyclerview and adapter with jsonarray values
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        nameAdapter = new NameAdapter(inflater.getContext(),jsonArray);
        recyclerView.setAdapter(nameAdapter);

        return v;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search_btn.setOnClickListener(v -> {
            if(search_text.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "Please fill out search field.",
                        Toast.LENGTH_LONG).show();
            }else {
                try {
                    searchUser(jsonArray,search_text.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_alphabetically) {
            try {
                jsonArray = sortAlphabetically(jsonArray);
                nameAdapter.resetValues(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        if (id == R.id.action_numerically) {
            try {
                jsonArray = sortNumerically(jsonArray);
                nameAdapter.resetValues(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        if (id == R.id.action_total) {
            try {
                Toast.makeText(getActivity(),
                        "Total amount of all names: "+ getTotalAmount(jsonArray),
                        Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private JSONArray sortAlphabetically(JSONArray jsonArray) throws JSONException {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonList.add(jsonArray.getJSONObject(i));
        }

        Collections.sort( jsonList, (a, b) -> {
            String valA = new String();
            String valB = new String();

            try {
                valA = (String) a.get("name");
                valB = (String) b.get("name");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return valA.compareTo(valB);
        });

        for (int i = 0; i < jsonArray.length(); i++) {
            sortedJsonArray.put(jsonList.get(i));
        }
        return sortedJsonArray;
    }

    private JSONArray sortNumerically(JSONArray jsonArray) throws JSONException {
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonList.add(jsonArray.getJSONObject(i));
        }

        Collections.sort( jsonList, (a, b) -> {
            int valA = 0;
            int valB = 0;
            try {
                valA = (int) a.get("amount");
                valB = (int) b.get("amount");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return valB-valA;
        });

        for (int i = 0; i < jsonArray.length(); i++) {
            sortedJsonArray.put(jsonList.get(i));
        }
        return sortedJsonArray;
    }

    private void searchUser(JSONArray jsonArray, String search_text)throws JSONException{
        boolean found = false;
        for(int i = 0;i<jsonArray.length();i++){
            if(!found){
                String temp = jsonArray.getJSONObject(i).getString("name");
                //If user is found set found == true and show user the information that was found.
                if(search_text.equals(temp)){
                    String amount = jsonArray.getJSONObject(i).getString("amount");
                    Toast.makeText(getActivity(), "Name "+ search_text + " found. Amount: " + amount,
                            Toast.LENGTH_LONG).show();
                    found = true;
                }
            }
        }

        //If user was not found print search text and tell user that it was not found.
        if(!found){
            Toast.makeText(getActivity(),
                    "Name "+ search_text + " not found.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private int getTotalAmount(JSONArray jsonArray) throws JSONException {
        int total = 0;
        for(int i = 0;i<jsonArray.length();i++){
            total += Integer.parseInt(jsonArray.getJSONObject(i).getString("amount"));
        }
        return total;
    }

}