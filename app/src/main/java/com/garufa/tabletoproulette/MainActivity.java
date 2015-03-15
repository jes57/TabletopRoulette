package com.garufa.tabletoproulette;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Intent intent;
    private TextView idView;
    EditText nameEditText, descriptionEditText;
    private String name;
//    DBHandler dbHandler;

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
//        dbHandler = new DBHandler(this, null, null, 1);
    }

    public void newGame (View view) {

//        Game game = new Game(nameEditText.getText().toString(),
//                descriptionEditText.getText().toString());
//
//        dbHandler.addGame(game);
//        nameEditText.setText("");
//        descriptionEditText.setText("");
    }
    public void lookupGame (View view) {


        Intent gameIntent = new Intent(MainActivity.this, SearchListView.class);
        name = nameEditText.getText().toString();
        gameIntent.putExtra(Constants.EXTRAS_NAME, name);
        startActivity(gameIntent);
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
                intent = new Intent(MainActivity.this, CollectionListView.class);
                startActivity(intent); break;
            case R.id.action_search:
                intent = new Intent(MainActivity.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_addGame:
                intent = new Intent(MainActivity.this, AddGame.class);
                startActivity(intent); break;
            case R.id.action_mainActivity:
                intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }

}
