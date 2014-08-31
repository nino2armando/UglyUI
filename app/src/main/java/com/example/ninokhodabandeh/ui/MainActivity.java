package com.example.ninokhodabandeh.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ninokhodabandeh.ui.Models.UserParameters;
import com.example.ninokhodabandeh.ui.Services.ApiServices;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    Context _context;

    // UI elements
    Spinner spinner;
    SeekBar seekBar;
    EditText editText;

    String _dropDownSelected;
    String _userInput;
    String _radius;

    boolean userInputExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _context = this;

        // load UI
        loadSpinner();
        activateSeekbarValue();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadSpinner(){
        spinner = (Spinner) findViewById(R.id.spinner_premisesArray);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(_context, R.array.premises_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getSelectedItem().toString();
                LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(selection.equals(getString(R.string.letMeChoose))){
                    activateUserInputIfRequested(layout);

                    // show the keyboard and put focus on the editText view
                    if(editText.requestFocus()){
                        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }

                    userInputExist = true;
                    return;
                }if(userInputExist){

                    layout.removeView(editText);
                    // hide the keyboard upon removing the editText
                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                    userInputExist = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void activateUserInputIfRequested(LinearLayout layout){

        editText = new EditText(_context);
        editText.setId(R.id.editText_userInput);
        editText.setHint(getString(R.string.typeOfBusiness));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 0);
        editText.setLayoutParams(params);
        int i = ((ViewGroup)spinner.getParent()).indexOfChild(spinner);
        layout.addView(editText, i+1);
    }

    private void activateSeekbarValue(){
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        final TextView seekBarValue = (TextView) findViewById(R.id.seekBar_value);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                String formattedValue = String.format("%1$s/100", String.valueOf(progress));
                seekBarValue.setText(formattedValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onClickGoButton(View view) {
        String locationType;
        _dropDownSelected = spinner.getSelectedItem().toString();
        _radius = String.valueOf(seekBar.getProgress());

        if (userInputExist) {
            String userInput = editText.getText().toString();
            if (userInput != null || !userInput.equals("")) {
                _userInput = userInput;
            }
        }

        if (_dropDownSelected.equals(UiUtils.DEFAULT_USER_SELECTION)) {

            displayDialog(getString(R.string.mustSelect));

        } else if (userInputExist && (_userInput == null || _userInput.equals(""))) {

            displayDialog(getString(R.string.mustEnter));

        } else {
            // todo: if we have all the args call the api and get results
            locationType = userInputExist ? _userInput : _dropDownSelected;
            UserParameters parameters = new UserParameters(locationType, Integer.parseInt(_radius));
            AsyncTask<UserParameters, Void, Void> task = new CallApiWithPrgressBarAsync();
            task.execute(parameters);
        }
    }

    private void displayDialog(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(R.string.mustSelectHeading).setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private class CallApiWithPrgressBarAsync extends AsyncTask<UserParameters, Void, Void>{

        private ProgressDialog _progressDialog;
        ArrayList<ApiServices.ApiResultModel> apiResult;
        @Override
        protected void onPreExecute() {
            _progressDialog = new ProgressDialog(_context);
            _progressDialog.setTitle("processing");
            _progressDialog.setCancelable(false);
            _progressDialog.setIndeterminate(true);
            _progressDialog.show();
        }

        @Override
        protected Void doInBackground(UserParameters... params) {
            try{
                // todo: we should remove the tread and call the ApiService
                UserParameters userInput = params[0];

                // todo: remove the thread sleep
                //Thread.sleep(2000);
                ApiServices apiServices = new ApiServices();
                apiResult = apiServices.getResultFromApi(userInput);

            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(_progressDialog != null){
                _progressDialog.dismiss();
            }
            Intent intent = new Intent(_context, ResultActivity.class);
            intent.putParcelableArrayListExtra(UiUtils.API_RESULT_FOR_LOCATIONS, apiResult);
            startActivity(intent);
            super.onPostExecute(aVoid);
        }
    }
}
