package com.garufa.tabletoproulette;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Jason on 3/7/2015.
 */
public class QueryGames extends ActionBarActivity {

    private Intent intent;
    private TextView idView;
    EditText playersEditText, timeEditText, ratingEditText;
    Spinner game_mechanic_spinner;
    Button searchButton, luckyButton;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_game_layout);

        initialize();
    }

    private void initialize() {
        playersEditText         = (EditText) findViewById(R.id.query_players);
        timeEditText            = (EditText) findViewById(R.id.query_editText_time);
        ratingEditText          = (EditText) findViewById(R.id.query_rating);
        game_mechanic_spinner   = (Spinner)  findViewById(R.id.query_spinner_game_mechanic);
        searchButton            = (Button)   findViewById(R.id.query_buttonSearch);
        luckyButton             = (Button)   findViewById(R.id.query_buttonLucky);
        dbHandler               = new DBHandler(this, null, null, 1);
    }

    public void search (View view) {
        intent = new Intent(QueryGames.this, QueryListview.class);
        intent.putExtra(Constants.EXTRAS_PLAYERS, playersEditText.getText().toString());
        intent.putExtra(Constants.EXTRAS_TIME, timeEditText.getText().toString());
        intent.putExtra(Constants.EXTRAS_RATING, ratingEditText.getText().toString());
        intent.putExtra(Constants.EXTRAS_MECHANIC, "");

        startActivity(intent);
    }
    public void randomGame (View view) {
        intent = new Intent(QueryGames.this, QueryRandom.class);
        intent.putExtra(Constants.EXTRAS_PLAYERS, playersEditText.getText().toString());
        intent.putExtra(Constants.EXTRAS_TIME, timeEditText.getText().toString());
        intent.putExtra(Constants.EXTRAS_RATING, ratingEditText.getText().toString());
        intent.putExtra(Constants.EXTRAS_MECHANIC, "");

        startActivity(intent);
    }
    public void removeGame (View view) {

//        boolean result = dbHandler.deleteGame(nameEditText.getText().toString());
//
//        if (result){
//            idView.setText("Record Deleted");
//            nameEditText.setText("");
//            descriptionEditText.setText("");
//        } else {
//            idView.setText("No Match Found");
//        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (id){
            case R.id.action_settings: return true;
            case R.id.action_collection:
                intent = new Intent(QueryGames.this, CollectionListView.class);
                startActivity(intent); break;
            case R.id.action_new_game:
                intent = new Intent(QueryGames.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_query:
                intent = new Intent(QueryGames.this, QueryGames.class);
                startActivity(intent); break;
            case R.id.action_mainActivity:
                intent = new Intent(QueryGames.this, MainActivity.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }
}
