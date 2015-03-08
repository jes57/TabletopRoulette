package com.garufa.tabletoproulette;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private static final String
            TAG = "XML Parser",
            URL_ID = "http://www.boardgamegeek.com/xmlapi/boardgame/",
            URL_NAME = "http://www.boardgamegeek.com/xmlapi/search?search=Splendor",
            GAME_ID = "148228",
            QUERY_URL = URL_ID + GAME_ID;


    private Intent intent;
    TextView textView_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_info_layout);

        Log.i(TAG, "Query XML...");
        textView_description = (TextView) findViewById(R.id.textView_description);
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
