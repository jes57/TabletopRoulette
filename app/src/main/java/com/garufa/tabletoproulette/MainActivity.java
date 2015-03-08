package com.garufa.tabletoproulette;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String
            TAG = "XML Parser",
            URL_ID = "http://www.boardgamegeek.com/xmlapi/boardgame/",
            URL_NAME = "http://www.boardgamegeek.com/xmlapi/search?search=Splendor",
            GAME_ID = "148228",
            QUERY_URL = URL_ID + GAME_ID;


    private Intent intent;
    TextView textView_description, textView_title;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Query XML...");
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPage();
    }

    private void loadPage() {
        new DownLoadXmlTask().execute(QUERY_URL);
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
            textView_description = (TextView) findViewById(R.id.textView_description);
            textView_title = (TextView) findViewById(R.id.textView_title);
            imageView = (ImageView) findViewById(R.id.imageView_game_artwork);
            new ImageLoadTask(game.get_image_url(), imageView).execute();
            textView_title.setText(game.get_name());
            textView_description.setText(game.get_description());
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
