package com.garufa.tabletoproulette;

/**
 * Created by Jason on 2/28/2015.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class LayoutFragment extends Fragment {
    public static final String MENU_NUMBER = "menu_number";
    public final static int COLLECTION_SCREEN = 0, GAME_PICKER = 1, HELP = 2, SOURCES = 3,
            ABOUT = 4, GAME_INFO = 5;

    public LayoutFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        int i = getArguments().getInt(MENU_NUMBER);
        String screen = getResources().getStringArray(R.array.menu_array)[i];
        // Select the screen based on position
//        switch (i){
//            case COLLECTION_SCREEN:
//                rootView = inflater.inflate(R.layout.collection_layout, container, false);
//                break;
//            case GAME_PICKER:
//                rootView = inflater.inflate(R.layout.game_info_layout, container, false);
//                new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork))
//                        .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                break;
//            case HELP:
//                rootView = inflater.inflate(R.layout.game_info_layout, container, false);
//                new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork))
//                        .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                break;
//            case SOURCES:
//                rootView = inflater.inflate(R.layout.game_info_layout, container, false);
//                new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork))
//                        .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                break;
//            case ABOUT:
//                rootView = inflater.inflate(R.layout.game_info_layout, container, false);
//                new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork))
//                        .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                break;
//            case GAME_INFO:
//                rootView = inflater.inflate(R.layout.game_info_layout, container, false);
//                new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork))
//                        .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                break;
//            default:
//                rootView = inflater.inflate(R.layout.game_info_layout, container, false);
//                new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork))
//                        .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
//                break;
//        }
//            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                    "drawable", getActivity().getPackageName());
//            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
        rootView = inflater.inflate(R.layout.selection_layout, container, false);
        new DownloadImageTask((ImageView) rootView.findViewById(R.id.imageView_game_artwork))
                .execute("http://cf.geekdo-images.com/images/pic1904079_t.jpg");
        getActivity().setTitle(screen);
        return rootView;
    }
}
