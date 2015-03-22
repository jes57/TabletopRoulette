package com.garufa.tabletoproulette;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Jason on 3/7/2015.
 */
public class QueryGames extends ActionBarActivity {

    private Intent intent;
    private TextView idView;
    EditText nameEditText, descriptionEditText;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_game_layout);

        initialize();
    }

    private void initialize() {
        idView = (TextView) findViewById(R.id.tvGameID);
        nameEditText = (EditText) findViewById(R.id.etGameName);
        descriptionEditText = (EditText) findViewById(R.id.etGameDesc);
        dbHandler = new DBHandler(this, null, null, 1);
    }

    public void newGame (View view) {

        Game game = new Game(nameEditText.getText().toString(),
                descriptionEditText.getText().toString());

        dbHandler.addGame(game);
        nameEditText.setText("");
        descriptionEditText.setText("");
    }
    public void lookupGame (View view) {

        Game game = dbHandler.findGame(nameEditText.getText().toString());

        if (game != null) {
            idView.setText(String.valueOf(game.get_id()));
            descriptionEditText.setText(game.get_description());
        } else {
            idView.setText("No Match Found");
        }
    }
    public void removeGame (View view) {

        boolean result = dbHandler.deleteGame(nameEditText.getText().toString());

        if (result){
            idView.setText("Record Deleted");
            nameEditText.setText("");
            descriptionEditText.setText("");
        } else {
            idView.setText("No Match Found");
        }

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
            case R.id.action_search:
                intent = new Intent(QueryGames.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_addGame:
                intent = new Intent(QueryGames.this, QueryGames.class);
                startActivity(intent); break;
            case R.id.action_mainActivity:
                intent = new Intent(QueryGames.this, MainActivity.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }
}
