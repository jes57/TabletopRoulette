package com.garufa.tabletoproulette;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Jason on 3/15/2015.
 */
public class SearchDetails extends ActionBarActivity{
    private static final String
            TAG = "Search Details...",
            GAME_ID = "148228";
//            QUERY_URL = Constants.URL_BGG_ID_SEARCH + GAME_ID + Constants.URL_STATS;

    private String game_id, game_name, query_url;
    private Intent intent, intent_extras;
    TextView textView_description, textView_title, textView_details, textView_rating,
            textView_players, textView_playtime, textView_mechanic;
    ImageView imageView;
    Button button_add;
    DBHandler dbHandler;
    Game game_to_add;
    Bitmap image;

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
        dbHandler = new DBHandler(SearchDetails.this, null, null, 1);

        setContentView(R.layout.game_info_layout);
        textView_description = (TextView) findViewById(R.id.info_textView_description);
        textView_title       = (TextView) findViewById(R.id.info_textView_title);
        textView_details     = (TextView) findViewById(R.id.info_textView_addition_details);
        textView_rating      = (TextView) findViewById(R.id.info_textView_rating);
        textView_players     = (TextView) findViewById(R.id.info_textView_players);
        textView_playtime    = (TextView) findViewById(R.id.info_textView_playtime);
        textView_mechanic    = (TextView) findViewById(R.id.info_textView_mechanic);
        imageView            = (ImageView) findViewById(R.id.info_imageView_game_artwork);
        button_add           = (Button) findViewById(R.id.info_button_add);
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPage();
    }

    public void add_game(View v) {
        if (dbHandler.addGame(game_to_add)){
            if (image != null){
                saveImageInternal(image);
                String message = game_to_add.get_name() + " image added.";
                Toast.makeText(SearchDetails.this, message, Toast.LENGTH_SHORT).show();
            }
//            String message = game_to_add.get_name() + " added to collection.";
//            Toast.makeText(SearchDetails.this, message, Toast.LENGTH_SHORT).show();
        } else {
            String message = "Could not add. Game may already exist in database.";
            Toast.makeText(SearchDetails.this, message, Toast.LENGTH_SHORT).show();
        }

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
        protected void onPostExecute(Game g) {
            SearchDetails.this.game_to_add = g;

            // Set player text
            String players, time;
            if (game_to_add.get_min_players() == game_to_add.get_max_players()){
                players = String.valueOf(game_to_add.get_min_players());
            } else {
                players = game_to_add.get_min_players() + " - " + game_to_add.get_max_players();
            }
            if (game_to_add.get_min_play_time() == game_to_add.get_max_play_time()){
                time = String.valueOf(game_to_add.get_min_play_time());
            } else {
                time    = game_to_add.get_min_play_time() + " - " + game_to_add.get_max_play_time();
            }

            // Set page content
            new ImageLoadTask(game_to_add.get_image_url(), new AsyncResponse() {
                @Override
                public void processFinish(Bitmap output) {
                    image = output;
                    imageView.setImageBitmap(image);
                }
            }).execute();
            textView_title.setText(game_to_add.get_name());
            textView_description.setText(Html.fromHtml(game_to_add.get_description()));
            textView_rating.setText(String.valueOf(game_to_add.get_rating()));
            textView_playtime.setText(time);
            textView_players.setText(players);
            textView_mechanic.setText(game_to_add.get_game_mechanic());
        }
    }

    private boolean saveImageInternal(Bitmap image) {
        String file_name = String.valueOf(game_to_add.get_bgg_id()) + Constants.FILE_TYPE;
        try {
            // Compress the image to write to OutputStream
            FileOutputStream outputStream = openFileOutput(file_name, Context.MODE_PRIVATE);

            // Write the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            return true;
        } catch (Exception e) {
            Log.e("saveImageInteral()", e.getMessage());
            return false;
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
                intent = new Intent(SearchDetails.this, QueryGames.class);
                startActivity(intent); break;
            case R.id.action_mainActivity:
                intent = new Intent(SearchDetails.this, MainActivity.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }
}


