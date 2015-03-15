package com.garufa.tabletoproulette;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Jason on 3/15/2015.
 */
public class SearchDetails extends ActionBarActivity {
    private static final String
            TAG = "Search Details...",
            GAME_ID = "148228";
//            QUERY_URL = Constants.URL_BGG_ID_SEARCH + GAME_ID + Constants.URL_STATS;

    private String game_id, game_name, query_url;
    private Intent intent, intent_extras;
    TextView textView_description, textView_title, textView_details, textView_ratings;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        // Get the game ID
        intent_extras = getIntent();
        game_id = intent_extras.getExtras().getString(Constants.EXTRAS_ID);
        query_url = Constants.URL_BGG_ID_SEARCH + game_id + Constants.URL_STATS;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPage();
    }

    private void loadPage() {
        new DownLoadXmlTask().execute(query_url);
    }

    private class DownLoadXmlTask extends AsyncTask<String, Void, Game> {
        @Override
        protected Game doInBackground(String... urls) {
            try {
                Log.i("AsyncTask", "Right before loadXmlFromUrl");
                return loadXmlFromUrl(urls[0]);
            } catch (IOException e) {
                return new Game("N/A", "Unable to load data: IOException");
            } catch (XmlPullParserException e) {
                return new Game("N/A", "Unable to load data: XmlPullParserException");
            }
        }

        @Override
        protected void onPostExecute(Game game) {
            setContentView(R.layout.game_info_layout);
            textView_description = (TextView) findViewById(R.id.info_textView_description);
            textView_title = (TextView) findViewById(R.id.info_textView_title);
            textView_details = (TextView) findViewById(R.id.info_textView_addition_details);
            textView_ratings = (TextView) findViewById(R.id.info_textView_rating);
            imageView = (ImageView) findViewById(R.id.info_imageView_game_artwork);
            new ImageLoadTask(game.get_image_url(), imageView).execute();
            textView_title.setText(game.get_name());
            textView_ratings.setText(String.valueOf(game.get_rating()));

            // Expand
            textView_description.setText(Html.fromHtml(game.get_description()));
        }
    }

    private Game loadXmlFromUrl(String url_string) throws XmlPullParserException, IOException {
        Log.i("loadXmlFromUrl...", "Start of loadXmlFromUrl");
        InputStream stream = null;
        BoardGameGeekXmlParser boardGameGeekXmlParser = new BoardGameGeekXmlParser();
        List<Game> games = null;

        try {
            stream = downloadUrl(url_string);
            games = boardGameGeekXmlParser.parse(stream);
        } finally {
            if (stream != null){
                stream.close();
            }
        }
        return games.get(0);
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

        switch (id){
            case R.id.action_settings: return true;
            case R.id.action_collection:
                intent = new Intent(SearchDetails.this, CollectionListView.class);
                startActivity(intent); break;
            case R.id.action_search:
                intent = new Intent(SearchDetails.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_addGame:
                intent = new Intent(SearchDetails.this, AddGame.class);
                startActivity(intent); break;
            case R.id.action_mainActivity:
                intent = new Intent(SearchDetails.this, MainActivity.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }
}

