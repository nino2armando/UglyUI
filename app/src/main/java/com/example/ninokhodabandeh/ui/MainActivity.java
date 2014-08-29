package com.example.ninokhodabandeh.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


public class MainActivity extends ActionBarActivity {

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.premises_array, android.R.layout.simple_spinner_item);
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

        editText = new EditText(getApplicationContext());
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

        _dropDownSelected = spinner.getSelectedItem().toString();
        _radius = String.valueOf(seekBar.getProgress());

        if (userInputExist) {
            String userInput = editText.getText().toString();
            if (userInput != null || userInput != "") {
                _userInput = userInput;
            }
        }

        if (_dropDownSelected.equals(UiUtils.DEFAULT_USER_SELECTION)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.mustSelectHeading).setMessage(R.string.mustSelect);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            // todo: if we have all the args call the api and get results

            Intent resultActivity = new Intent(this, ResultActivity.class);
            startActivity(resultActivity);
        }


    }
}
