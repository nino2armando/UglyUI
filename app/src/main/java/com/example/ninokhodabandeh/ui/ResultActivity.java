package com.example.ninokhodabandeh.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ninokhodabandeh.ui.Services.ApiServices;

import java.util.ArrayList;

/**
 * Created by nino.khodabandeh on 8/28/2014.
 */
public class ResultActivity extends ActionBarActivity {

    ArrayList<ApiServices.ApiResultModel> _resultSet;
    Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        _context = this;

        if(savedInstanceState != null){
            String a = savedInstanceState.getString("nino");
        }
        // load UI
        loadListView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("nino", _resultSet);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void loadListView(){
        ListView listView = (ListView) findViewById(R.id.listView_apiResult);

        Intent mainActivityIntent = getIntent();
        _resultSet = mainActivityIntent.getParcelableArrayListExtra(UiUtils.API_RESULT_FOR_LOCATIONS);
        CustomAdapter adapter = new CustomAdapter(this, _resultSet);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mapActivity = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(mapActivity);
            }
        });
    }


}
