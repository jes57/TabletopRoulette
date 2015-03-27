package com.garufa.tabletoproulette;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.text.Html;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import org.xmlpull.v1.XmlPullParserException;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.List;

/**
 * Created by Jason on 2/18/2015.
 */
public class GameInfo extends ActionBarActivity {
    private static final String
            TAG = "XML Parser",
            GAME_ID = "148228";
//            QUERY_URL = Constants.URL_BGG_ID_SEARCH + GAME_ID + Constants.URL_STATS;

    private String /*game_name,*/ query_url;
//    int game_id, bgg_id;
    private Intent intent, intent_extras;
    TextView textView_description, textView_title, textView_details,
            textView_players, textView_playtime, textView_mechanic, textView_rating;
    ImageView imageView;
    Button button_add;
    Bitmap image;
    DBHandler dbHandler;
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        setContentView(R.layout.game_info_layout);
        textView_description = (TextView) findViewById(R.id.info_textView_description);
        textView_title       = (TextView) findViewById(R.id.info_textView_title);
        textView_rating      = (TextView) findViewById(R.id.info_textView_rating);
        textView_players     = (TextView) findViewById(R.id.info_textView_players);
        textView_playtime    = (TextView) findViewById(R.id.info_textView_playtime);
        textView_mechanic    = (TextView) findViewById(R.id.info_textView_mechanic);
        imageView            = (ImageView) findViewById(R.id.info_imageView_game_artwork);
        button_add           = (Button) findViewById(R.id.info_button_add);

        // Hide the button
        button_add.setVisibility(View.GONE);

        // Retrieve the extras to determine game
        intent_extras        = getIntent();
        Bundle bundle = intent_extras.getExtras();
        if (bundle != null){
            int game_id = Integer.parseInt(bundle.getString(Constants.EXTRAS_ID));
            int bgg_id = Integer.parseInt(bundle.getString(Constants.EXTRAS_BGG_ID));
            String game_name = bundle.getString(Constants.EXTRAS_NAME);

            // Find the game in the database
            dbHandler = new DBHandler(this, null, null, DBHandler.DATABASE_VERSION);
            game = dbHandler.findGame(game_name);

            // Set players and time strings
            String players, time;
            if (game.get_min_players() == game.get_max_players()){
                players = String.valueOf(game.get_min_players());
            } else {
                players = game.get_min_players() + " - " + game.get_max_players();
            }
            if (game.get_min_play_time() == game.get_max_play_time()){
                time = String.valueOf(game.get_min_play_time());
            } else {
                time    = game.get_min_play_time() + " - " + game.get_max_play_time();
            }

            // Set the views to the data in the selected game
            textView_title.setText(game.get_name());
            textView_description.setText(Html.fromHtml(game.get_description()));
            textView_rating.setText(String.valueOf(game.get_rating()));
            textView_playtime.setText(time);
            textView_players.setText(players);
            textView_mechanic.setText(game.get_game_mechanic());

            // Set the ImageView
            String game_id_string = String.valueOf(game.get_bgg_id());
            imageView.setImageBitmap(get_thumbnail(game_id_string));
        }

    }

    // Retrieve the thumbnail stored locally
    private Bitmap get_thumbnail(String game_id) {
        String file_name = game_id + Constants.FILE_TYPE;

        try {
            File file_path = getFileStreamPath(file_name);
            FileInputStream inputStream = new FileInputStream(file_path);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(getResources(), R.drawable.tabletoproulet);
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
                intent = new Intent(GameInfo.this, CollectionListView.class);
                startActivity(intent); break;
            case R.id.action_new_game:
                intent = new Intent(GameInfo.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_query:
                intent = new Intent(GameInfo.this, QueryGames.class);
                startActivity(intent); break;
            case R.id.action_mainActivity:
                intent = new Intent(GameInfo.this, MainActivity.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }
}


