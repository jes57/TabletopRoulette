package com.garufa.tabletoproulette;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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
public class SearchListView extends BaseActivity {

    private Intent intent;

    String GAME_ID = "148228",
            GAME_NAME = "Splendor",
            name, game_id, query_url;

    String[] gamesArray;
    ListView collectionListView;
    ArrayList<String> gamesArrayList;
    List<Game> gameObjectsArrayList;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {
        // Set the AlertDialog to accept the search parameter
        displaySearchDialog();
        setContentView(R.layout.collection_layout);
    }

    private void displaySearchDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(SearchListView.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        final EditText editText = (EditText) promptView.findViewById(R.id.dialogEditText);
        builder = new AlertDialog.Builder(SearchListView.this);
        builder.setView(promptView)
               .setTitle("Search")
               .setIcon(R.drawable.ic_launcher)
               .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = editText.getText().toString();
                query_url = Constants.URL_BGG_NAME_SEARCH + name;
                // Need to replace spaces with the HTML code %20
                query_url = query_url.replace(" ", "%20");
                loadPage();
            }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).create().show();
    }

    // Call the DownLoadXmlTask to populate ArrayList
    private void loadPage() {
        new DownLoadXmlTask().execute(query_url);
    }

    private class DownLoadXmlTask extends AsyncTask<String, Void, List<Game>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SearchListView.this, "Wait", "Downloading...");
        }
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
            progressDialog.dismiss();
            gameObjectsArrayList = games;
            GameArrayAdapter adapter = new GameArrayAdapter(SearchListView.this, gameObjectsArrayList);

            // Set the ListView
            collectionListView = (ListView) findViewById(R.id.collectionListView);
            collectionListView.setAdapter(adapter);

            collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Set the name and id of the game to be displayed
                    Game game = gameObjectsArrayList.get(position);
                    name = game.get_name();
                    game_id = String.valueOf(game.get_bgg_id());

                    intent = new Intent(SearchListView.this, SearchDetails.class);
                    intent.putExtra(Constants.EXTRAS_ID, game_id);
                    intent.putExtra(Constants.EXTRAS_NAME, name);
                    startActivity(intent);
                }
            });
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
}
