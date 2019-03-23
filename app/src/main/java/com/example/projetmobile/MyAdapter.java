package com.example.projetmobile;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ViewH extends RecyclerView.ViewHolder{
    ConstraintLayout v;
    public  ViewH(ConstraintLayout v){
        super(v);
        this.v=v;
    }
}
class MyAdapter extends RecyclerView.Adapter<ViewH>{
    ArrayList<HashMap<String, String>> data;
    String from;
    public  MyAdapter( ArrayList<HashMap<String, String>>  data, String from){
        this.data=data;
        this.from=from;
    }
    @Override
    public ViewH onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout cl = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_depense, parent,false);
        ViewH vh = new ViewH(cl);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewH holder, int position) {
        final int p = position;
        TextView somme =holder.v.findViewById(R.id.somme);
        somme.setText(data.get(position).get("somme"));
        TextView detail =holder.v.findViewById(R.id.detail);
        detail.setText(data.get(position).get("detail"));
        final Button b = holder.v.findViewById(R.id.button);
        final TextView statut =holder.v.findViewById(R.id.paid);
        if(data.get(position).get("idto").equals(from)){
            b.setVisibility(View.INVISIBLE);
        }else{
            if(data.get(position).get("statut").equals("paid")) {
                b.setVisibility(View.INVISIBLE);
                statut.setText("you have "+data.get(position).get("statut"));
            }else{
                b.setText(data.get(position).get("statut"));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.setVisibility(View.INVISIBLE);
                        statut.setText("you have paid");
                        HttpJsonParser httpJsonParser = new HttpJsonParser();
                        Map<String, String> httpParams = new HashMap<>();
                        httpParams.put("did",data.get(p).get("did"));
                        JSONObject jsonObject = httpJsonParser.makeHttpRequest(MainActivity.BASE_URL+"update_depense.php", "POST", httpParams);
                        try {
                            int success = jsonObject.getInt("success");
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
