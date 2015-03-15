package com.garufa.tabletoproulette;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 3/14/2015.
 */
public class SearchListView extends ActionBarActivity {

    private Intent intent, intent_extras;

    String GAME_ID = "148228",
            GAME_NAME = "Splendor",
            name, game_id, query_url;

    String[] gamesArray;
    ListView collectionListView;
    ArrayList<String> gamesArrayList;
    List<Game> gameObjectsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {
        intent_extras = getIntent();
        name = intent_extras.getExtras().getString(Constants.EXTRAS_NAME);
        query_url = Constants.URL_BGG_NAME_SEARCH + name;

        loadPage();
//        gameObjectsArrayList.add(new Game("Splendor", "Super fun"));
//        gameObjectsArrayList.add(new Game("Splendorifous", "Not fun"));


//        dbHandler = new DBHandler(this, null, null, 1);
//        cursor = dbHandler.getAllGames();
//
//        // Set the ListView
//        collectionListView = (ListView) findViewById(R.id.collectionListView);
//        GameCursorAdapter cursorAdapter = new GameCursorAdapter(this, cursor);
//        collectionListView.setAdapter(cursorAdapter);
//
//        // Set the onClick event
//        collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (cursor.moveToPosition(position)) {
//                    name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));
//                    game_id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_BGG_ID));
//                }
//                String gamePicked = "You selected " + name;
//
//                Toast.makeText(SearchListView.this, gamePicked, Toast.LENGTH_SHORT).show();
//
//                Intent gameIntent = new Intent(SearchListView.this, GameInfo.class);
//
//                gameIntent.putExtra(Constants.EXTRAS_ID, GAME_ID);
//                gameIntent.putExtra(Constants.EXTRAS_NAME, name);
//                startActivity(gameIntent);
//            }
//        });
    }

    // Call the DownLoadXmlTask to populate ArrayList
    private void loadPage() {
        new DownLoadXmlTask().execute(query_url);
    }

    private class DownLoadXmlTask extends AsyncTask<String, Void, List<Game>> {
        @Override
        protected List<Game> doInBackground(String... urls) {
            try {
                Log.i("AsyncTask", "Right before loadXmlFromUrl");
                return loadXmlFromUrl(urls[0]);
            } catch (IOException e) {
                List<Game> games = new ArrayList<Game>();
                games.add(new Game("N/A", "Unable to load data: IOException"));
                return games;
            } catch (XmlPullParserException e) {
                List<Game> games = new ArrayList<Game>();
                games.add(new Game("N/A", "Unable to load data: XmlPullParserException"));
                return games;
            }
        }

        @Override
        protected void onPostExecute(List<Game> games) {
            setContentView(R.layout.collection_layout);
            gameObjectsArrayList = games;
            GameArrayAdapter adapter = new GameArrayAdapter(SearchListView.this, gameObjectsArrayList);

            // Set the ListView
            collectionListView = (ListView) findViewById(R.id.collectionListView);
            collectionListView.setAdapter(adapter);
        }
    }

    private List<Game> loadXmlFromUrl(String url_string) throws XmlPullParserException, IOException {
        Log.i("loadXmlFromUrl...", "Start of loadXmlFromUrl");
        InputStream stream = null;
        BoardGameGeekXmlParser boardGameGeekXmlParser = new BoardGameGeekXmlParser();
        List<Game> games = null;

        try {
            stream = downloadUrl(url_string);
            games = boardGameGeekXmlParser.parse_by_name(stream);
        } finally {
            if (stream != null){
                stream.close();
            }
        }
        return games;
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    private InputStream downloadUrl(String url_string) throws IOException {
        URL url = new URL(url_string);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
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

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_collection:
                intent = new Intent(SearchListView.this, CollectionListView.class);
                startActivity(intent);
                break;
            case R.id.action_addGame:
                intent = new Intent(SearchListView.this, AddGame.class);
                startActivity(intent);
                break;
            case R.id.action_mainActivity:
                intent = new Intent(SearchListView.this, MainActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}