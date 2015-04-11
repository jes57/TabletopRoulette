package com.garufa.tabletoproulette;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.text.Html;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import java.io.File;
        import java.io.FileInputStream;

/**
 * Created by Jason on 2/18/2015.
 */
public class GameInfo extends BaseActivity {
    private static final String
            TAG = "XML Parser",
            GAME_ID = "148228";
//            QUERY_URL = Constants.URL_BGG_ID_SEARCH + GAME_ID + Constants.URL_STATS;

    private String /*game_name,*/ query_url;
//    int game_id, bgg_id;
    private Intent intent, intent_extras;
    TextView textView_description, textView_title, textView_details, textView_published,
            textView_players, textView_playtime, textView_mechanic, textView_rating, textView_year;
    ImageView imageView;
    Button button_add;
    RatingBar ratingBar;
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
        textView_year        = (TextView) findViewById(R.id.info_year);
        textView_published = (TextView) findViewById(R.id.info_published);
        ratingBar            = (RatingBar) findViewById(R.id.info_ratingBar);
        button_add           = (Button) findViewById(R.id.info_button_add);

        // Hide the button
        button_add.setVisibility(View.GONE);

        // Retrieve the extras to determine game
        intent_extras        = getIntent();
        Bundle bundle = intent_extras.getExtras();
        if (bundle != null){
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
            ratingBar.setRating(Float.parseFloat(String.valueOf(game.get_rating())));
            String year = String.valueOf(game.get_year());
            if (year.equals("0")) {
                textView_year.setVisibility(View.GONE);
                textView_published.setVisibility(View.GONE);
            } else {
                textView_year.setText(year);
            }

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
}


