package com.garufa.tabletoproulette;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

/**
 * Created by Jason on 3/28/2015.
 */
public class QueryRandom extends ActionBarActivity {
    private Intent intent, intent_extras;
    TextView textView_description, textView_title, textView_details,
            textView_players, textView_playtime, textView_mechanic, textView_rating;
    ImageView imageView;
    Button button_add;
    RatingBar ratingBar;
    Bitmap image;
    DBHandler dbHandler;
    Game game;
    String players, time, rating, mechanic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        setContentView(R.layout.game_info_layout);
        setContentView(R.layout.game_info_layout);
        textView_description = (TextView) findViewById(R.id.info_textView_description);
        textView_title       = (TextView) findViewById(R.id.info_textView_title);
        textView_rating      = (TextView) findViewById(R.id.info_textView_rating);
        textView_players     = (TextView) findViewById(R.id.info_textView_players);
        textView_playtime    = (TextView) findViewById(R.id.info_textView_playtime);
        textView_mechanic    = (TextView) findViewById(R.id.info_textView_mechanic);
        imageView            = (ImageView) findViewById(R.id.info_imageView_game_artwork);
        ratingBar            = (RatingBar) findViewById(R.id.info_ratingBar);
        button_add           = (Button) findViewById(R.id.info_button_add);

        // Hide the button
        button_add.setText("Roll Again");
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_screen();
            }
        });

        // Retrieve the extras to determine game
        dbHandler = new DBHandler(this, null, null, DBHandler.DATABASE_VERSION);
        set_parameters();
        set_screen();
    }

    private void set_screen() {
        game = dbHandler.getRandomGame(players, time, rating, mechanic);
        String gamePicked = game.get_name() + " was selected.";
        Toast.makeText(QueryRandom.this, gamePicked, Toast.LENGTH_SHORT).show();
        // Set players and time strings
        String players_string, time_string;
        if (game.get_min_players() == game.get_max_players()){
            players_string = String.valueOf(game.get_min_players());
        } else {
            players_string = game.get_min_players() + " - " + game.get_max_players();
        }
        if (game.get_min_play_time() == game.get_max_play_time()){
            time_string = String.valueOf(game.get_min_play_time());
        } else {
            time_string = game.get_min_play_time() + " - " + game.get_max_play_time();
        }

        // Set the views to the data in the selected game
        textView_title.setText(game.get_name());
        textView_description.setText(Html.fromHtml(game.get_description()));
        textView_rating.setText(String.valueOf(game.get_rating()));
        textView_playtime.setText(time_string);
        textView_players.setText(players_string);
        textView_mechanic.setText(game.get_game_mechanic());
        ratingBar.setRating(Float.parseFloat(String.valueOf(game.get_rating())));

        // Set the ImageView
        String game_id_string = String.valueOf(game.get_bgg_id());
        imageView.setImageBitmap(get_thumbnail(game_id_string));
    }

    private void set_parameters() {
        intent_extras = getIntent();
        Bundle bundle = intent_extras.getExtras();
        if (bundle != null) {
            players = bundle.getString(Constants.EXTRAS_PLAYERS);
            time = bundle.getString(Constants.EXTRAS_TIME);
            rating = bundle.getString(Constants.EXTRAS_RATING);
            mechanic = bundle.getString(Constants.EXTRAS_MECHANIC);
        } else {
            players = time = rating = mechanic = "";
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
                intent = new Intent(QueryRandom.this, CollectionListView.class);
                startActivity(intent); break;
            case R.id.action_new_game:
                intent = new Intent(QueryRandom.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_query:
                intent = new Intent(QueryRandom.this, QueryGames.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }
}
