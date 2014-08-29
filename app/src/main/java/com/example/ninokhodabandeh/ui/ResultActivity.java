package com.example.ninokhodabandeh.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ninokhodabandeh.ui.Services.ApiServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nino.khodabandeh on 8/28/2014.
 */
public class ResultActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // load UI
        loadListView();
    }

    private void loadListView(){
        ListView listView = (ListView) findViewById(R.id.listView_apiResult);

        CustomAdapter adapter = new CustomAdapter(this, ApiServices.getFakeApiContent());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


}
