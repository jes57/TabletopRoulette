package com.garufa.tabletoproulette;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with no view to show a progress dialog and download games from user collection
 */
public class TestListView extends BaseActivity {

    private Intent intent;
    private static String TAG = "Bulk Add Games...";

    String name, game_id, query_url;
    ListView collectionListView;
    List<Game> gameObjectsArrayList;
    Game game_to_add;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    Bitmap image;
//    DBHandler dbHandler;
    DatabaseHelper dbHandler;

    // Dynamic Layout
    Button button_addGames;
    LinearLayout layout;
    LinearLayout.LayoutParams layoutParams;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize();
    }

    private void initialize() {
        setContentView(R.layout.collection_layout);

        DatabaseHelper dbHandler = DatabaseHelper.getInstance(this);
//        dbHandler = new DBHandler(TestListView.this, null, null, DBHandler.DATABASE_VERSION);


        Intent intent_extras        = getIntent();
        Bundle bundle = intent_extras.getExtras();
        if (bundle != null) {
            query_url = bundle.getString(Constants.EXTRAS_URL);
            // Need to replace spaces with the HTML code %20
            query_url = query_url.replace(" ", "%20");
        } else {
            query_url = Constants.URL_BGG_TEST_ID;
        }


        addOrDelete();
    }


    // Confirm the download of a new set of games
    protected void addOrDelete() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage("Would you like to add to your collection or replace it?")
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true)
                .setPositiveButton("Add to", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        loadPage();
                    }
                })
                .setNegativeButton("Replace", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeGames();
                        loadPage();
                    }
                })
                .create().show();
    }

    private void removeGames() {
        dbHandler = DatabaseHelper.getInstance(this);
//        dbHandler = new DBHandler(this, null, null, DBHandler.DATABASE_VERSION);
//        dbHandler.deleteAll();
//        showToast("Delete successful!");
        if (!dbHandler.deleteAll()) {
            showToast("Unable to remove 1 or more games");
        } else {
            showToast("Delete successful!");
        }
    }


    // Call the DownLoadXmlTask to populate ArrayList
    private void loadPage() {
        new DownLoadXmlTask().execute(query_url);
    }

    private class DownLoadXmlTask extends AsyncTask<String, Void, List<Game>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TestListView.this, "Wait", "Downloading...");
        }
        @Override
        protected List<Game> doInBackground(String... urls) {
            try {
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
            gameObjectsArrayList = checkForDuplicates(games);
            final GameArrayAdapter adapter = new GameArrayAdapter(TestListView.this,
                    R.layout.adapter_item_simple, gameObjectsArrayList);

            // Set the ListView
            collectionListView = (ListView) findViewById(R.id.collectionListView);
            collectionListView.setAdapter(adapter);
            collectionListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            collectionListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    adapter.toggleSelection(position);
                    final int checkedCount = adapter.getSelectedCount();
                    mode.setTitle(checkedCount + " selected");
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.add:

                            SparseBooleanArray selected = adapter.getmSelectedItemsIds();
                            List<Game> gameList = new ArrayList<Game>();
                            for (int i = (selected.size() - 1); i >= 0; i--){
                                Game selectedGame = adapter.getItem(selected.keyAt(i));
                                gameList.add(selectedGame);
//                                adapter.addFromXml(selectedGame);
                                adapter.remove(selectedGame);
//                                String query_url = Constants.URL_BGG_ID_SEARCH
//                                        + selectedGame.get_bgg_id()
//                                        + Constants.URL_STATS;
//                                try {
//                                    Game g = insertXmlFromUrl(query_url);
//                                    gameList.add(g);
//                                    adapter.remove(selectedGame);
//                                } catch (IOException e) {
//                                    Log.e(TAG, "Unable to load data: IOException.");
//                                } catch (XmlPullParserException e) {
//                                    Log.e(TAG, "Unable to load data: XmlPullParserException");
//                                }
//                                dbHandler = DatabaseHelper.getInstance(TestListView.this);
//                                dbHandler.addGameBulk(gameList);

                            }
                            dbHandler.addGameBulk(gameList, false);
                            mode.finish();
                            return true;
                        case R.id.select_all:
                            adapter.selectAll();
                            final int checkedCount = adapter.getSelectedCount();
                            mode.setTitle(checkedCount + " selected");
                            return false;
                        default: return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapter.removeSelection();
                }
            });

            collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Set the name and id of the game to be displayed
                    Game game = gameObjectsArrayList.get(position);
                    name = game.get_name();
                    game_id = String.valueOf(game.get_bgg_id());

                    intent = new Intent(TestListView.this, SearchDetails.class);
                    intent.putExtra(Constants.EXTRAS_ID, game_id);
                    intent.putExtra(Constants.EXTRAS_NAME, name);
                    startActivity(intent);
                }
            });
            progressDialog.dismiss();
        }
    }

    private List<Game> checkForDuplicates(List<Game> games) {
        dbHandler = DatabaseHelper.getInstance(this);
        for (Game g : new ArrayList<Game>(games)){
            if (dbHandler.gameExists(Constants.COLUMN_GAME_NAME,
                    g.get_name())){
                games.remove(g);
            }
        }
        return games;
    }

    private class InsertXmlTask extends AsyncTask<String, Void, Game> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress("Inserting...");
        }
        @Override
        protected Game doInBackground(String... urls) {
            try {
                return insertXmlFromUrl(urls[0]);
            } catch (IOException e) {
                return new Game("N/A", "Unable to load data: IOException");
            } catch (XmlPullParserException e) {
                return new Game("N/A", "Unable to load data: XmlPullParserException");
            }
        }

        @Override
        protected void onPostExecute(Game g) {
            final Game game = g;
            dbHandler.addGame(game);
            new ImageLoadTask(String.valueOf(game.get_bgg_id()), new AsyncResponse() {
                @Override
                public void processFinish(Bitmap output) {
                    saveImageInternal(output, String.valueOf(game.get_bgg_id()));
                    dismissProgress();
                }
            });
    }

}

    private boolean saveImageInternal(Bitmap image, String bgg_id) {
        String file_name = bgg_id + Constants.FILE_TYPE;
        try {
            // Compress the image to write to OutputStream
            FileOutputStream outputStream = openFileOutput(file_name, Context.MODE_PRIVATE);

            // Write the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Game insertXmlFromUrl(String url_string) throws XmlPullParserException, IOException {
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

    private List<Game> loadXmlFromUrl(String url_string) throws XmlPullParserException, IOException {
        InputStream stream = null;
        BoardGameGeekXmlParser boardGameGeekXmlParser = new BoardGameGeekXmlParser();
        List<Game> games = null;

        try {
            stream = downloadUrl(url_string);
            games = boardGameGeekXmlParser.parse_user_collection(stream);
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
