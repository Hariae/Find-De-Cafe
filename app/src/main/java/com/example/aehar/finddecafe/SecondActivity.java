package com.example.aehar.finddecafe;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.util.Log;
import java.util.*;

import java.util.ArrayList;
import java.util.List;
public class SecondActivity extends AppCompatActivity {
    private String URL_DATA="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.421998333333335,-122.08400000000002&radius=10000&type=cafe&sensor=true&key=AIzaSyCE2dQSvyWLtDQQYkOfBCY2cPRhUQQbxRI";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        adapter = new MyAdapter(listItems, this);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        String latitude = intent.getStringExtra("latitude");
        String longitude = intent.getStringExtra("longitude");

        URL_DATA="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+ latitude+ "," + longitude+"&radius=10000&type=restaurant&sensor=true&key=AIzaSyCE2dQSvyWLtDQQYkOfBCY2cPRhUQQbxRI";
        //URL_DATA="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.3258719,-121.8770076&radius=10000&type=restaurant&sensor=true&key=AIzaSyCE2dQSvyWLtDQQYkOfBCY2cPRhUQQbxRI";
        loadRecyclerViewData();

    }

    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.v("places", response);

                    JSONArray array = jsonObject.getJSONArray("results");

                    for(int i=0;i<array.length();i++)
                    {
                        JSONObject o= array.getJSONObject(i);
                        ListItem item = new ListItem(
                                o.getString("name"),
                                o.getString("vicinity"),
                                o.getDouble("rating")
                        );
                        Log.v("placeName: " , item.getName());
                        listItems.add(item);
                    }

                    //Sort based on reviews. Collections.swap(words, 0, words.size() - 1);
                    for(int i=0;i<listItems.size();i++){
                        for(int j=i+1;j<listItems.size();j++){
                            if(listItems.get(i).getRating() < listItems.get(j).getRating()){
                                Collections.swap(listItems, i, j);
                            }
                        }
                    }

                    adapter = new MyAdapter(listItems,getApplicationContext());
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
