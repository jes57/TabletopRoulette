package com.garufa.tabletoproulette;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.io.InputStream;
        import java.net.MalformedURLException;
        import java.net.URL;

/**
 * Created by Jason on 2/18/2015.
 */
public class GameInfo extends ActionBarActivity {
    public final static int COLLECTION_SCREEN = 0, GAME_PICKER = 1, HELP = 2, SOURCES = 3,
            ABOUT = 4;
    private String[] menu_selections;
    private DrawerLayout drawer_layout;
    private ListView drawer_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_info_layout);

        setupDrawer();

        new DownloadImageTask((ImageView) findViewById(R.id.imageView_game_artwork)).execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
    }

    private void setupDrawer(){
        menu_selections = getResources().getStringArray(R.array.menu_array);
        drawer_layout   = (DrawerLayout) findViewById(R.id.game_info_drawer_layout);
        drawer_list     = (ListView) findViewById(R.id.game_info_left_drawer);

        // Set the adapter for the list view
        drawer_list.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.drawer_list_item,
                menu_selections
        ));

        // Set the list's click listener
        drawer_list.setOnItemClickListener(new DrawerItemClickListener());

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent gameIntent;

            String gamePicked = "You selected " +
                    String.valueOf(parent.getItemAtPosition(position));

            Toast.makeText(GameInfo.this, gamePicked, Toast.LENGTH_SHORT).show();
            switch (position){
                case COLLECTION_SCREEN:
                    gameIntent = new Intent(GameInfo.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                case GAME_PICKER:
                    gameIntent = new Intent(GameInfo.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                case HELP:
                    gameIntent = new Intent(GameInfo.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                case SOURCES:
                    gameIntent = new Intent(GameInfo.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                case ABOUT:
                    gameIntent = new Intent(GameInfo.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                default:
                    gameIntent = new Intent(GameInfo.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
            }

//                gameIntent.putExtra("game", position);
            startActivity(gameIntent);
            GameInfo.this.finish();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


