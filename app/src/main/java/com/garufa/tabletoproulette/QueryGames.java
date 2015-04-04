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
public class QueryGames extends BaseActivity {

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
}
