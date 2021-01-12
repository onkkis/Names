package com.example.names;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.ViewHolder>{

    private Context context;
    private JSONArray jsonArray;

    public NameAdapter(Context context,JSONArray jsonArray){
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public NameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NameAdapter.ViewHolder holder, int position) {
        if(jsonArray != null){
            try {
                holder.name.setText(jsonArray.getJSONObject(position).getString("name"));
                holder.amount.setText(jsonArray.getJSONObject(position).getString("amount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            String placeholder = "No data available";
            holder.name.setText(placeholder);
        }
    }

    @Override
    public int getItemCount() {
        if (jsonArray != null)
            return jsonArray.length();
        else return 0;
    }

    public void resetValues(JSONArray jsonArray){
        this.jsonArray = jsonArray;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textview_name);
            amount = itemView.findViewById(R.id.textview_amount);
        }
    }
}
