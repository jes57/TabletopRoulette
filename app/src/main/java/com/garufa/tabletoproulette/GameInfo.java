package com.garufa.tabletoproulette;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.ImageView;

        import java.io.IOException;
        import java.io.InputStream;
        import java.net.MalformedURLException;
        import java.net.URL;

/**
 * Created by Jason on 2/18/2015.
 */
public class GameInfo extends ActionBarActivity {

    private Intent intent;
    ImageView game_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_info_layout);

        game_image = (ImageView) findViewById(R.id.imageView_game_artwork);

        new ImageLoadTask("http://cf.geekdo-images.com/images/pic1904079_t.jpg", game_image).execute();
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
            case R.id.action_addGame:
                intent = new Intent(GameInfo.this, AddGame.class);
                startActivity(intent); break;
            case R.id.action_mainActivity:
                intent = new Intent(GameInfo.this, MainActivity.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }
}


