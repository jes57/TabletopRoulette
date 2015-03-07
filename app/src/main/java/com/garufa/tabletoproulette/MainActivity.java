package com.garufa.tabletoproulette;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private final static int COLLECTION_SCREEN = 0, GAME_PICKER = 1, HELP = 2, SOURCES = 3, ABOUT = 4;

    private DrawerLayout drawer_layout;
    private ListView drawer_list;
    private ActionBarDrawerToggle drawer_toggle;

    private CharSequence drawer_title;
    private CharSequence title;
    private String[] menu_selections, gamesArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawer();
        // Get the array for the listView
        Resources res = getResources();
        gamesArray = res.getStringArray(R.array.games_array);

        selectItem(COLLECTION_SCREEN);
    }

    private void setupDrawer(){
        title = drawer_title = getTitle();
        menu_selections = getResources().getStringArray(R.array.menu_array);
        drawer_layout   = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_list     = (ListView) findViewById(R.id.left_drawer);

        drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menu_selections));

        // Set the list's click listener
        drawer_list.setOnItemClickListener(new DrawerItemClickListener());

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        if (position == 0){
            Fragment fragment = new LayoutFragment();
            Bundle args = new Bundle();
            args.putInt(LayoutFragment.MENU_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, the close the drawer
            drawer_list.setItemChecked(position, true);
            setTitle(gamesArray[position]);
            drawer_layout.closeDrawer(drawer_list);
        } else {
            Fragment fragment = new LayoutFragment();
            Bundle args = new Bundle();
            args.putInt(LayoutFragment.MENU_NUMBER, position);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, the close the drawer
            drawer_list.setItemChecked(position, true);
            setTitle(gamesArray[position]);
            drawer_layout.closeDrawer(drawer_list);
        }
    }

    @Override
    public void setTitle(CharSequence in_title) {
        title = in_title;
        getSupportActionBar().setTitle(title);
    }

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

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
//    public static class LayoutFragment extends Fragment {
//        public static final String MENU_NUMBER = "menu_number";
//
//        public LayoutFragment() {
//            // Empty constructor required for fragment subclasses
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView;
//            int i = getArguments().getInt(MENU_NUMBER);
//            String screen = getResources().getStringArray(R.array.menu_array)[i];
//            // Select the screen based on position
//            switch (i){
//                case COLLECTION_SCREEN:
//                    rootView = inflater.inflate(R.layout.selection_layout, container, false);
//                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork2))
//                            .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                    break;
//                case GAME_PICKER:
//                    rootView = inflater.inflate(R.layout.selection_layout, container, false);
//                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork2))
//                            .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                    break;
//                case HELP:
//                    rootView = inflater.inflate(R.layout.selection_layout, container, false);
//                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork2))
//                            .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                    break;
//                case SOURCES:
//                    rootView = inflater.inflate(R.layout.selection_layout, container, false);
//                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork2))
//                            .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                    break;
//                case ABOUT:
//                    rootView = inflater.inflate(R.layout.selection_layout, container, false);
//                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork2))
//                            .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                    break;
//                default:
//                    rootView = inflater.inflate(R.layout.selection_layout, container, false);
//                    new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork2))
//                            .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                    break;
//            }
////            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
////                    "drawable", getActivity().getPackageName());
////            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
//            getActivity().setTitle(screen);
//            return rootView;
//        }
//    }
}
